using System.Diagnostics;
using System.Runtime.InteropServices;
using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Metrics;

/// <summary>
/// Real (non-simulated) system metrics for both target platforms.
/// Windows path uses kernel32 P/Invoke (GetSystemTimes / GlobalMemoryStatusEx) — this is what
/// actually ships and runs on the user's PC. The Linux path (/proc/stat, /proc/meminfo) exists
/// so the exact same class is exercised by real, non-mocked data while developing and testing
/// in this Linux container; disk figures use <see cref="DriveInfo"/>, which is genuinely
/// cross-platform and identical on both OSes.
/// </summary>
public sealed class SystemMetricsProvider : ISystemMetricsProvider
{
    private readonly Stopwatch _clock = Stopwatch.StartNew();
    private CpuSample? _lastSample;
    private readonly object _gate = new();

    public SystemMetrics GetMetrics()
    {
        var cpu = SampleCpu();
        var mem = OperatingSystem.IsWindows() ? ReadWindowsMemory() : ReadLinuxMemory();
        var disks = ReadDisks();

        return new SystemMetrics(
            Ts: DateTimeOffset.UtcNow.ToUnixTimeMilliseconds(),
            Cpu: cpu,
            Memory: mem,
            Disks: disks,
            MachineName: Environment.MachineName,
            OsVersion: RuntimeInformation.OSDescription,
            Uptime: TimeSpan.FromMilliseconds(Environment.TickCount64));
    }

    private static IReadOnlyList<DiskMetrics> ReadDisks()
    {
        var result = new List<DiskMetrics>();
        foreach (var drive in DriveInfo.GetDrives())
        {
            if (!drive.IsReady) continue;
            if (drive.DriveType is not (DriveType.Fixed or DriveType.Network)) continue;
            try
            {
                var total = drive.TotalSize;
                var free = drive.AvailableFreeSpace;
                var used = total - free;
                var freeRatio = total > 0 ? (double)free / total : 1.0;
                result.Add(new DiskMetrics(
                    Name: drive.Name,
                    VolumeLabel: SafeLabel(drive),
                    TotalBytes: total,
                    FreeBytes: free,
                    UsedBytes: used,
                    IsLowSpace: freeRatio < 0.10 || free < 5L * 1024 * 1024 * 1024));
            }
            catch (IOException)
            {
                // Drive became unavailable between IsReady check and read (e.g. removable media). Skip it.
            }
        }
        return result;
    }

    private static string SafeLabel(DriveInfo drive)
    {
        try { return string.IsNullOrWhiteSpace(drive.VolumeLabel) ? drive.Name : drive.VolumeLabel; }
        catch (IOException) { return drive.Name; }
    }

    // ---- CPU ----------------------------------------------------------

    private readonly record struct CpuSample(long Idle, long Total, long TimestampMs);

    private CpuMetrics SampleCpu()
    {
        var (idle, total) = OperatingSystem.IsWindows() ? ReadWindowsCpuTimes() : ReadLinuxCpuTimes();
        var nowMs = _clock.ElapsedMilliseconds;

        lock (_gate)
        {
            var previous = _lastSample;
            _lastSample = new CpuSample(idle, total, nowMs);

            if (previous is null || total <= previous.Value.Total)
            {
                // First sample, or counters reset (e.g. container clock jump): report 0 rather than garbage.
                return new CpuMetrics(0, Array.Empty<double>(), Environment.ProcessorCount);
            }

            var idleDelta = idle - previous.Value.Idle;
            var totalDelta = total - previous.Value.Total;
            var busyPercent = totalDelta > 0 ? 100.0 * (1.0 - (double)idleDelta / totalDelta) : 0.0;
            busyPercent = Math.Clamp(busyPercent, 0, 100);
            return new CpuMetrics(busyPercent, Array.Empty<double>(), Environment.ProcessorCount);
        }
    }

    private static (long idle, long total) ReadWindowsCpuTimes()
    {
        if (!NativeMethods.GetSystemTimes(out var idleTime, out var kernelTime, out var userTime))
        {
            return (0, 0);
        }

        long idle = ToInt64(idleTime);
        long kernel = ToInt64(kernelTime);
        long user = ToInt64(userTime);
        // kernelTime includes idleTime on Windows; total = kernel(includes idle) + user.
        return (idle, kernel + user);
    }

    private static long ToInt64(NativeMethods.FILETIME ft)
        => ((long)ft.dwHighDateTime << 32) | (uint)ft.dwLowDateTime;

    private static (long idle, long total) ReadLinuxCpuTimes()
    {
        try
        {
            var line = File.ReadLines("/proc/stat").First(l => l.StartsWith("cpu "));
            var parts = line.Split(' ', StringSplitOptions.RemoveEmptyEntries | StringSplitOptions.TrimEntries);
            // cpu  user nice system idle iowait irq softirq steal guest guest_nice
            var values = parts.Skip(1).Select(long.Parse).ToArray();
            long idle = values[3] + (values.Length > 4 ? values[4] : 0); // idle + iowait
            long total = values.Sum();
            return (idle, total);
        }
        catch (Exception ex) when (ex is IOException or UnauthorizedAccessException or FormatException)
        {
            return (0, 0);
        }
    }

    private static MemoryMetrics ReadWindowsMemory()
    {
        var status = new NativeMethods.MEMORYSTATUSEX { dwLength = (uint)Marshal.SizeOf<NativeMethods.MEMORYSTATUSEX>() };
        if (!NativeMethods.GlobalMemoryStatusEx(ref status))
        {
            return new MemoryMetrics(0, 0, 0);
        }

        long total = (long)status.ullTotalPhys;
        long avail = (long)status.ullAvailPhys;
        return new MemoryMetrics(total, total - avail, avail);
    }

    private static MemoryMetrics ReadLinuxMemory()
    {
        try
        {
            long totalKb = 0, availKb = 0;
            foreach (var line in File.ReadLines("/proc/meminfo"))
            {
                if (line.StartsWith("MemTotal:")) totalKb = ExtractKb(line);
                else if (line.StartsWith("MemAvailable:")) availKb = ExtractKb(line);
            }
            long total = totalKb * 1024;
            long avail = availKb * 1024;
            return new MemoryMetrics(total, total - avail, avail);
        }
        catch (Exception ex) when (ex is IOException or UnauthorizedAccessException or FormatException)
        {
            return new MemoryMetrics(0, 0, 0);
        }
    }

    private static long ExtractKb(string procMeminfoLine)
    {
        var digits = procMeminfoLine.Where(char.IsDigit).ToArray();
        return digits.Length == 0 ? 0 : long.Parse(new string(digits));
    }

    private static class NativeMethods
    {
        [StructLayout(LayoutKind.Sequential)]
        public struct FILETIME
        {
            public int dwLowDateTime;
            public int dwHighDateTime;
        }

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
        public struct MEMORYSTATUSEX
        {
            public uint dwLength;
            public uint dwMemoryLoad;
            public ulong ullTotalPhys;
            public ulong ullAvailPhys;
            public ulong ullTotalPageFile;
            public ulong ullAvailPageFile;
            public ulong ullTotalVirtual;
            public ulong ullAvailVirtual;
            public ulong ullAvailExtendedVirtual;
        }

        [DllImport("kernel32.dll", SetLastError = true)]
        public static extern bool GetSystemTimes(out FILETIME lpIdleTime, out FILETIME lpKernelTime, out FILETIME lpUserTime);

        [DllImport("kernel32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.Bool)]
        public static extern bool GlobalMemoryStatusEx(ref MEMORYSTATUSEX lpBuffer);
    }
}
