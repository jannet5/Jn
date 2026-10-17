; WinRemote Monitor - Windows kurulum programı (Inno Setup 6)
; Derleme: ISCC.exe setup.iss  (önce ../build.sh ile dist/ içindeki exe'ler üretilir)

#define AppVersion "1.1.0"

[Setup]
AppId={{8B3F2C1E-5A7D-4E2B-9C61-3F0D2A9E7B44}
AppName=WinRemote Monitor
AppVersion={#AppVersion}
AppVerName=WinRemote Monitor {#AppVersion}
AppPublisher=WinRemote Monitor
VersionInfoVersion={#AppVersion}
DefaultDirName={autopf}\WinRemote Monitor
DefaultGroupName=WinRemote Monitor
DisableDirPage=yes
DisableProgramGroupPage=yes
DisableReadyPage=yes
PrivilegesRequired=admin
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
OutputDir=..\dist
OutputBaseFilename=WinRemoteMonitor-Kurulum
SetupIconFile=app.ico
UninstallDisplayIcon={app}\WinRemoteMonitor.exe
UninstallDisplayName=WinRemote Monitor
Compression=lzma2/max
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "tr"; MessagesFile: "compiler:Languages\Turkish.isl"

[Messages]
tr.FinishedHeadingLabel=Kurulum tamamlandı
tr.FinishedLabelNoIcons=WinRemote Monitor arka planda çalışıyor ve bilgisayar her açıldığında kendiliğinden başlayacak.%n%nŞimdi telefonunuzda WinRemote Monitor uygulamasını açın ve birazdan açılacak sayfadaki QR kodu okutun.
tr.FinishedLabel=WinRemote Monitor arka planda çalışıyor ve bilgisayar her açıldığında kendiliğinden başlayacak.%n%nŞimdi telefonunuzda WinRemote Monitor uygulamasını açın ve birazdan açılacak sayfadaki QR kodu okutun.

[Dirs]
; Ajan normal kullanıcı olarak çalışır; veri klasörü (eşleşmeler, sertifika,
; geçmiş) her kullanıcının yazabileceği şekilde önceden oluşturulur.
Name: "{commonappdata}\WinRemoteMonitor"; Permissions: users-modify

[Files]
Source: "..\dist\WinRemoteMonitor.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\dist\winremotemonitor-agent.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "config.json"; DestDir: "{app}"; Flags: onlyifdoesntexist
Source: "allowed_apps.json"; DestDir: "{app}"; Flags: onlyifdoesntexist

[Icons]
Name: "{group}\Telefonu Eşleştir"; Filename: "{app}\WinRemoteMonitor.exe"; Parameters: "--pair --open"; Comment: "Telefonu bağlamak için QR kod gösterir"
Name: "{commondesktop}\Telefonu Eşleştir"; Filename: "{app}\WinRemoteMonitor.exe"; Parameters: "--pair --open"; Comment: "Telefonu bağlamak için QR kod gösterir"
Name: "{group}\WinRemote Monitor'ü kaldır"; Filename: "{uninstallexe}"
Name: "{commonstartup}\WinRemote Monitor"; Filename: "{app}\WinRemoteMonitor.exe"

[Run]
; Program bazlı izin kuralları: Windows "erişime izin verilsin mi?" penceresini
; hiç göstermez (o pencerede İptal'e basılırsa Windows engelleme kuralı ekler).
Filename: "{sys}\netsh.exe"; Parameters: "advfirewall firewall delete rule name=""WinRemote Monitor"""; Flags: runhidden waituntilterminated
Filename: "{sys}\netsh.exe"; Parameters: "advfirewall firewall add rule name=""WinRemote Monitor"" dir=in action=allow protocol=TCP localport=8787 program=""{app}\WinRemoteMonitor.exe"" enable=yes"; Flags: runhidden waituntilterminated
Filename: "{sys}\netsh.exe"; Parameters: "advfirewall firewall add rule name=""WinRemote Monitor"" dir=in action=allow protocol=TCP localport=8787 program=""{app}\winremotemonitor-agent.exe"" enable=yes"; Flags: runhidden waituntilterminated
Filename: "{app}\WinRemoteMonitor.exe"; Flags: runasoriginaluser nowait
Filename: "{app}\WinRemoteMonitor.exe"; Parameters: "--pair --open"; Description: "Telefonumu şimdi bağla (QR kodu göster)"; Flags: postinstall runasoriginaluser nowait

[UninstallRun]
Filename: "{sys}\taskkill.exe"; Parameters: "/F /IM WinRemoteMonitor.exe"; Flags: runhidden waituntilterminated; RunOnceId: "KillAgent"
Filename: "{sys}\taskkill.exe"; Parameters: "/F /IM winremotemonitor-agent.exe"; Flags: runhidden waituntilterminated; RunOnceId: "KillAgentConsole"
Filename: "{sys}\netsh.exe"; Parameters: "advfirewall firewall delete rule name=""WinRemote Monitor"""; Flags: runhidden waituntilterminated; RunOnceId: "DeleteFirewallRule"

[UninstallDelete]
Type: filesandordirs; Name: "{commonappdata}\WinRemoteMonitor"

[Code]
// Güncelleme sırasında çalışan eski sürümü durdur, yoksa exe değiştirilemez.
function PrepareToInstall(var NeedsRestart: Boolean): String;
var
  ResultCode: Integer;
begin
  Exec(ExpandConstant('{sys}\taskkill.exe'), '/F /IM WinRemoteMonitor.exe', '', SW_HIDE, ewWaitUntilTerminated, ResultCode);
  Exec(ExpandConstant('{sys}\taskkill.exe'), '/F /IM winremotemonitor-agent.exe', '', SW_HIDE, ewWaitUntilTerminated, ResultCode);
  Result := '';
end;
