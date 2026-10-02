// Command agent is the WinRemoteMonitor Windows agent CLI entrypoint.
//
// Subcommands / flags:
//
//	agent [--config path]                 run the WS/TLS server (default)
//	agent --pair [--config path]          print a fresh pairing code, QR PNG, cert fingerprint, IP:port
//	agent devices list [--config path]    list paired devices
//	agent devices revoke <id> [--config path]
//
// See windows-agent/README.md for full usage.
package main

import (
	"context"
	"flag"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"path/filepath"
	"strings"
	"syscall"
	"time"

	"github.com/jake/winremotemonitor-agent/internal/agent"
	"github.com/jake/winremotemonitor-agent/internal/core"
)

const agentVersion = "1.0.0"

func main() {
	if len(os.Args) > 1 && os.Args[1] == "devices" {
		runDevicesCommand(os.Args[2:])
		return
	}

	fs := flag.NewFlagSet("agent", flag.ExitOnError)
	configPath := fs.String("config", "", "path to config.json (default: built-in defaults)")
	allowedAppsPath := fs.String("allowed-apps", "", "path to allowed_apps.json (overrides config)")
	dataDirFlag := fs.String("data-dir", "", "override the agent's data directory (default: %ProgramData%\\WinRemoteMonitor or ~/.winremotemonitor)")
	pair := fs.Bool("pair", false, "print a fresh pairing code + QR PNG + cert fingerprint, then exit")
	qrOut := fs.String("qr-out", "pairing-qr.png", "where to write the pairing QR PNG (used with --pair)")
	fs.Parse(os.Args[1:])

	cfgFile := resolveConfigPath(*configPath)
	cfg, err := core.LoadConfig(cfgFile)
	if err != nil {
		log.Fatalf("loading config: %v", err)
	}
	if cfgFile != "" {
		log.Printf("using config %s", cfgFile)
	}
	dataDir := agent.DataDir(*dataDirFlag)
	if cfg.DataDir != "" && cfg.DataDir != "." {
		dataDir = cfg.DataDir
	}
	if *dataDirFlag != "" {
		dataDir = *dataDirFlag
	}
	if err := os.MkdirAll(dataDir, 0700); err != nil {
		log.Fatalf("creating data dir %s: %v", dataDir, err)
	}

	allowedAppsFile := cfg.AllowedAppsPath
	if allowedAppsFile != "" && !filepath.IsAbs(allowedAppsFile) {
		allowedAppsFile = filepath.Join(configBaseDir(cfgFile), allowedAppsFile)
	}
	if *allowedAppsPath != "" {
		allowedAppsFile = *allowedAppsPath
	}
	allowList, err := loadOrEmptyAllowList(allowedAppsFile)
	if err != nil {
		log.Fatalf("loading allow-list: %v", err)
	}

	store, err := agent.OpenStore(agent.DBPath(dataDir))
	if err != nil {
		log.Fatalf("opening store: %v", err)
	}
	defer store.Close()

	serverKey, err := agent.EnsureServerKey(agent.ServerKeyPath(dataDir))
	if err != nil {
		log.Fatalf("loading server key: %v", err)
	}

	srv := agent.NewServer(cfg, store, allowList, serverKey)
	srv.AgentVersion = agentVersion

	if *pair {
		runPairCommand(srv, dataDir, *qrOut, cfg.Port)
		return
	}

	runServer(srv, cfg, dataDir)
}

// exeDir is the folder the agent's executable lives in. Relative config
// paths resolve against it rather than the working directory, which is
// System32 when started from Task Scheduler or a service wrapper.
func exeDir() string {
	exe, err := os.Executable()
	if err != nil {
		wd, _ := os.Getwd()
		return wd
	}
	if resolved, err := filepath.EvalSymlinks(exe); err == nil {
		exe = resolved
	}
	return filepath.Dir(exe)
}

// resolveConfigPath returns the explicit --config value, or config.json
// next to the executable if one exists there, or "" for built-in defaults.
func resolveConfigPath(flagVal string) string {
	if flagVal != "" {
		return flagVal
	}
	candidate := filepath.Join(exeDir(), "config.json")
	if _, err := os.Stat(candidate); err == nil {
		return candidate
	}
	return ""
}

func configBaseDir(cfgFile string) string {
	if cfgFile != "" {
		if abs, err := filepath.Abs(cfgFile); err == nil {
			return filepath.Dir(abs)
		}
	}
	return exeDir()
}

func loadOrEmptyAllowList(path string) (*core.AllowList, error) {
	if path == "" {
		return core.NewAllowList(nil)
	}
	if _, err := os.Stat(path); os.IsNotExist(err) {
		log.Printf("warning: allowed apps file %s does not exist; no apps will be launchable until it is created (see allowed_apps.example.json)", path)
		return core.NewAllowList(nil)
	}
	return core.LoadAllowList(path)
}

func runServer(srv *agent.Server, cfg core.Config, dataDir string) {
	cert, fingerprint, err := agent.EnsureCert(agent.CertPath(dataDir), agent.KeyPath(dataDir))
	if err != nil {
		log.Fatalf("loading/generating TLS cert: %v", err)
	}
	log.Printf("TLS certificate fingerprint (SHA-256 of DER public key): %s", fingerprint)

	addr := fmt.Sprintf(":%d", cfg.Port)
	ln, err := srv.Listen(addr, cert)
	if err != nil {
		log.Fatalf("listening: %v", err)
	}
	log.Printf("WinRemoteMonitor agent v%s listening on %s (wss://<host>:%d/ws)", agentVersion, addr, cfg.Port)
	log.Printf("Run `agent --pair` to generate a pairing code for the Android app.")

	scanner := &agent.SizeScanner{Roots: cfg.WatchedRoots}
	rt := &agent.Runtime{
		Server:           srv,
		Store:            srv.Store,
		Metrics:          srv.Metrics,
		Scanner:          scanner,
		Thresholds:       cfg.AlertThresholds,
		MetricsInterval:  time.Duration(cfg.MetricsIntervalSec) * time.Second,
		SnapshotInterval: time.Duration(cfg.SnapshotIntervalSec) * time.Second,
		ProcessInterval:  time.Duration(cfg.ProcessesIntervalSec) * time.Second,
	}

	if len(cfg.WatchedRoots) > 0 {
		watcher, err := agent.NewFileWatcher(cfg.IgnoreGlobs, rt.OnFileEvent)
		if err != nil {
			log.Fatalf("creating file watcher: %v", err)
		}
		for _, root := range cfg.WatchedRoots {
			if err := watcher.AddRoot(root); err != nil {
				log.Printf("warning: failed to watch root %s: %v", root, err)
			}
		}
		rt.Watcher = watcher
		defer watcher.Close()
	} else {
		log.Printf("no watched_roots configured; file monitoring is disabled until config.json sets some (see config.example.json)")
	}

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	rt.Start(ctx)

	mux := http.NewServeMux()
	mux.Handle("/ws", srv)
	httpServer := &http.Server{Handler: mux}
	go func() {
		if err := httpServer.Serve(ln); err != nil && err != http.ErrServerClosed {
			log.Printf("server stopped: %v", err)
		}
	}()

	waitForShutdownSignal()
	log.Println("shutting down")
	_ = httpServer.Close()
}

func waitForShutdownSignal() {
	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, os.Interrupt, syscall.SIGTERM)
	<-sigCh
}

func runPairCommand(srv *agent.Server, dataDir, qrOut string, port int) {
	code, err := srv.PairingMgr.IssueCode(time.Now())
	if err != nil {
		log.Fatalf("generating pairing code: %v", err)
	}
	cert, fingerprint, err := agent.EnsureCert(agent.CertPath(dataDir), agent.KeyPath(dataDir))
	if err != nil {
		log.Fatalf("loading/generating TLS cert: %v", err)
	}
	_ = cert
	ip := agent.PrimaryLocalIP()

	fmt.Println("=== WinRemoteMonitor Pairing ===")
	fmt.Printf("Pairing code:      %s  (expires in %s, single-use)\n", code, core.PairingCodeTTL)
	fmt.Printf("Connect to:        %s:%d\n", ip, port)
	fmt.Printf("Cert fingerprint:  %s\n", fingerprint)
	var others []string
	for _, c := range agent.LocalIPv4Candidates() {
		if c != ip {
			others = append(others, c)
		}
	}
	if len(others) > 0 {
		fmt.Printf("Other addresses:   %s  (try these in manual entry if the phone can't connect)\n", strings.Join(others, ", "))
	}
	fmt.Println()
	fmt.Printf("The phone must be on the same network, and Windows Firewall must allow\n")
	fmt.Printf("inbound TCP %d (accept the firewall prompt, or run once as admin:\n", port)
	fmt.Printf("  netsh advfirewall firewall add rule name=\"WinRemoteMonitor\" dir=in action=allow protocol=TCP localport=%d\n", port)
	fmt.Println(")")
	fmt.Println()
	fmt.Println("Enter the pairing code in the Android app, or scan the QR code below.")

	qrText := fmt.Sprintf(`{"host":%q,"port":%d,"pairing_code":%q,"fingerprint_sha256":%q}`, ip, port, code, fingerprint)
	png, err := agent.RenderPairingQRPNG(qrText, 320)
	if err != nil {
		log.Fatalf("rendering QR code: %v", err)
	}
	outPath := qrOut
	if !filepath.IsAbs(outPath) {
		wd, _ := os.Getwd()
		outPath = filepath.Join(wd, outPath)
	}
	if err := os.WriteFile(outPath, png, 0644); err != nil {
		log.Fatalf("writing QR PNG: %v", err)
	}
	fmt.Printf("QR code written to: %s\n", outPath)
	fmt.Println()
	fmt.Println("This code is saved to the agent's local database, so it works whether")
	fmt.Println("the agent server (`agent`, no --pair) is already running in the")
	fmt.Println("background or you start it right after this command - either way, the")
	fmt.Println("server must be reachable and started before the code expires.")
}

// runDevicesCommand implements `agent devices <list|revoke> [id] [--config
// path] [--data-dir path]`. Flags may appear anywhere among args (before
// or after the subcommand/id) since we scan for and strip them ourselves
// rather than relying on stdlib flag's "stop at first non-flag" parsing,
// which would otherwise choke on `agent devices revoke <id> --config x`.
func runDevicesCommand(args []string) {
	if len(args) == 0 {
		fmt.Println("usage: agent devices list|revoke <id> [--config path]")
		os.Exit(2)
	}

	var configPath, dataDirFlagVal string
	var positional []string
	for i := 0; i < len(args); i++ {
		switch {
		case args[i] == "--config" && i+1 < len(args):
			configPath = args[i+1]
			i++
		case args[i] == "--data-dir" && i+1 < len(args):
			dataDirFlagVal = args[i+1]
			i++
		default:
			positional = append(positional, args[i])
		}
	}
	if len(positional) == 0 {
		fmt.Println("usage: agent devices list|revoke <id> [--config path]")
		os.Exit(2)
	}
	sub := positional[0]

	cfg, err := core.LoadConfig(resolveConfigPath(configPath))
	if err != nil {
		log.Fatalf("loading config: %v", err)
	}
	dataDir := agent.DataDir(dataDirFlagVal)
	if cfg.DataDir != "" && cfg.DataDir != "." {
		dataDir = cfg.DataDir
	}
	if dataDirFlagVal != "" {
		dataDir = dataDirFlagVal
	}

	store, err := agent.OpenStore(agent.DBPath(dataDir))
	if err != nil {
		log.Fatalf("opening store: %v", err)
	}
	defer store.Close()

	switch sub {
	case "list":
		devices, err := store.ListDevices()
		if err != nil {
			log.Fatalf("listing devices: %v", err)
		}
		if len(devices) == 0 {
			fmt.Println("No paired devices.")
			return
		}
		for _, d := range devices {
			status := "active"
			if d.Revoked {
				status = "revoked"
			}
			fmt.Printf("%s  %-20s  created=%s  %s\n", d.ID, d.Name, time.Unix(d.CreatedAt, 0).Format(time.RFC3339), status)
		}
	case "revoke":
		if len(positional) < 2 {
			fmt.Println("usage: agent devices revoke <id>")
			os.Exit(2)
		}
		id := positional[1]
		if err := store.RevokeDevice(id); err != nil {
			log.Fatalf("revoking device: %v", err)
		}
		_ = store.InsertHistory(agent.HistoryEntry{
			TS: time.Now().Unix(), DeviceID: id, DeviceName: "",
			Action: core.ActionUnpair, Success: true, Detail: "revoked via CLI",
		})
		fmt.Printf("Device %s revoked.\n", id)
	default:
		fmt.Println("usage: agent devices list|revoke <id> [--config path]")
		os.Exit(2)
	}
}
