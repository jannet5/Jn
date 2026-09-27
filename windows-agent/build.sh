#!/usr/bin/env bash
# Build script for the WinRemoteMonitor Windows agent.
#
# Runs `go vet`, the full test suite, then cross-compiles a Windows amd64
# binary (CGO-free, since all native-looking work is done via gopsutil/
# fsnotify/modernc.org-sqlite, none of which need cgo) and writes a
# SHA256SUMS.txt next to it.
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")"

echo "==> go vet ./..."
go vet ./...

echo "==> go test ./... -v"
go test ./... -v

echo "==> cross-compiling dist/winremotemonitor-agent.exe (GOOS=windows GOARCH=amd64 CGO_ENABLED=0)"
mkdir -p dist
GOOS=windows GOARCH=amd64 CGO_ENABLED=0 go build -o dist/winremotemonitor-agent.exe ./cmd/agent

echo "==> verifying dist/winremotemonitor-agent.exe is a real PE binary"
file dist/winremotemonitor-agent.exe

echo "==> writing dist/SHA256SUMS.txt"
(
  cd dist
  sha256sum winremotemonitor-agent.exe > SHA256SUMS.txt
)
cat dist/SHA256SUMS.txt

echo "==> build.sh completed successfully"
