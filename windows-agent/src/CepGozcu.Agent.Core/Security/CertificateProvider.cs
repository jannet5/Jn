using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;

namespace CepGozcu.Agent.Core.Security;

/// <summary>
/// Generates (once) and reloads a self-signed TLS certificate used for the agent's local
/// HTTPS/WebSocket listener. There is no CA involved on purpose: pairing hands the phone the
/// certificate's SHA-256 fingerprint out of band (QR code / manual confirmation) and the phone
/// pins that exact fingerprint from then on (see Android `PinnedTrustManager`). A real CA would
/// buy nothing here since nothing outside the pairing flow is ever meant to trust this cert.
/// </summary>
public sealed class CertificateProvider
{
    private readonly string _pfxPath;
    private readonly string _passwordPath;

    public CertificateProvider(string storageDirectory)
    {
        Directory.CreateDirectory(storageDirectory);
        _pfxPath = Path.Combine(storageDirectory, "agent-tls.pfx");
        _passwordPath = Path.Combine(storageDirectory, "agent-tls.pw");
    }

    public X509Certificate2 GetOrCreate()
    {
        if (File.Exists(_pfxPath) && File.Exists(_passwordPath))
        {
            try
            {
                var password = ReadPassword();
#pragma warning disable SYSLIB0057 // classic X509Certificate2 constructor; X509CertificateLoader requires net9+
                return new X509Certificate2(_pfxPath, password, X509KeyStorageFlags.Exportable);
#pragma warning restore SYSLIB0057
            }
            catch (CryptographicException)
            {
                // Corrupt/unreadable store (e.g. moved to another machine without DPAPI access) — regenerate.
            }
        }

        return CreateAndPersist();
    }

    public static string Fingerprint(X509Certificate2 cert) => Convert.ToHexString(cert.GetCertHash(HashAlgorithmName.SHA256));

    private X509Certificate2 CreateAndPersist()
    {
        using var ecdsa = ECDsa.Create(ECCurve.NamedCurves.nistP256);
        var request = new CertificateRequest(
            $"CN=CepGozcu-Agent-{Environment.MachineName}",
            ecdsa,
            HashAlgorithmName.SHA256);

        request.CertificateExtensions.Add(
            new X509KeyUsageExtension(X509KeyUsageFlags.DigitalSignature | X509KeyUsageFlags.KeyEncipherment, critical: true));
        request.CertificateExtensions.Add(
            new X509EnhancedKeyUsageExtension(new OidCollection { new Oid("1.3.6.1.5.5.7.3.1") }, critical: false)); // serverAuth

        var notBefore = DateTimeOffset.UtcNow.AddDays(-1);
        var notAfter = DateTimeOffset.UtcNow.AddYears(5);
        var cert = request.CreateSelfSigned(notBefore, notAfter);

        var password = Convert.ToBase64String(RandomNumberGenerator.GetBytes(32));
        var pfxBytes = cert.Export(X509ContentType.Pfx, password);
        File.WriteAllBytes(_pfxPath, pfxBytes);
        WritePassword(password);

#pragma warning disable SYSLIB0057
        return new X509Certificate2(pfxBytes, password, X509KeyStorageFlags.Exportable);
#pragma warning restore SYSLIB0057
    }

    private void WritePassword(string password)
    {
        var bytes = System.Text.Encoding.UTF8.GetBytes(password);
        if (OperatingSystem.IsWindows())
        {
            bytes = ProtectedData.Protect(bytes, null, DataProtectionScope.LocalMachine);
        }
        File.WriteAllBytes(_passwordPath, bytes);
    }

    private string ReadPassword()
    {
        var bytes = File.ReadAllBytes(_passwordPath);
        if (OperatingSystem.IsWindows())
        {
            bytes = ProtectedData.Unprotect(bytes, null, DataProtectionScope.LocalMachine);
        }
        return System.Text.Encoding.UTF8.GetString(bytes);
    }
}
