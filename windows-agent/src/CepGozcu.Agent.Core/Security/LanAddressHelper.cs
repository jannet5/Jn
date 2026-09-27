using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;

namespace CepGozcu.Agent.Core.Security;

/// <summary>
/// Enumerates this machine's own LAN-reachable addresses (for the pairing QR) and, separately,
/// judges whether an *inbound* remote address is allowed to reach the agent at all. The agent
/// intentionally serves only private-network/loopback clients — see project README "Security
/// model" for why no WAN/relay path exists in v1.
/// </summary>
public static class LanAddressHelper
{
    public static IReadOnlyList<string> GetOwnPrivateIPv4Addresses()
    {
        var results = new List<string>();
        foreach (var nic in NetworkInterface.GetAllNetworkInterfaces())
        {
            if (nic.OperationalStatus != OperationalStatus.Up) continue;
            if (nic.NetworkInterfaceType is NetworkInterfaceType.Loopback or NetworkInterfaceType.Tunnel) continue;

            foreach (var addr in nic.GetIPProperties().UnicastAddresses)
            {
                if (addr.Address.AddressFamily != AddressFamily.InterNetwork) continue;
                if (IsPrivateIPv4(addr.Address)) results.Add(addr.Address.ToString());
            }
        }
        return results;
    }

    public static bool IsAllowedRemote(IPAddress? remote)
    {
        if (remote is null) return false;
        if (IPAddress.IsLoopback(remote)) return true;

        if (remote.IsIPv4MappedToIPv6) remote = remote.MapToIPv4();
        if (remote.AddressFamily == AddressFamily.InterNetwork) return IsPrivateIPv4(remote);

        // Unique local IPv6 (fc00::/7) — treat as LAN too.
        if (remote.AddressFamily == AddressFamily.InterNetworkV6)
        {
            var bytes = remote.GetAddressBytes();
            return (bytes[0] & 0xFE) == 0xFC;
        }
        return false;
    }

    private static bool IsPrivateIPv4(IPAddress address)
    {
        var b = address.GetAddressBytes();
        if (b[0] == 10) return true;                              // 10.0.0.0/8
        if (b[0] == 172 && b[1] >= 16 && b[1] <= 31) return true;  // 172.16.0.0/12
        if (b[0] == 192 && b[1] == 168) return true;               // 192.168.0.0/16
        if (b[0] == 169 && b[1] == 254) return true;                // link-local
        return false;
    }
}
