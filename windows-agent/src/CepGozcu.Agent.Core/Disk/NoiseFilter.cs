using System.Text.RegularExpressions;

namespace CepGozcu.Agent.Core.Disk;

/// <summary>
/// Decides which folders are "noise": real disk usage that should still count towards folder
/// and drive totals, but whose internal churn (thousands of tiny temp-file writes per minute)
/// would drown out everything else in the change history if reported file-by-file. Matched
/// subtrees are still measured for size, just not diffed/logged per file — see DiskScanner.
/// </summary>
public sealed class NoiseFilter
{
    // Directory patterns: written as `\name\`, so they match anything *under* that folder. To also
    // match the folder path itself (with nothing after it yet), IsNoise pads a trailing separator
    // on to the candidate path before testing these — see below.
    private readonly List<Regex> _directoryPatterns;

    // File-name patterns: anchored with `$`, so padding a trailing separator on would break them —
    // tested against the raw (unpadded) path instead.
    private readonly List<Regex> _filePatterns;

    public NoiseFilter(IEnumerable<string>? extraGlobs = null)
    {
        var directoryDefaults = new[]
        {
            @"\\Windows\\Temp\\",
            @"\\Temp\\",
            @"\\AppData\\Local\\Temp\\",
            @"\\AppData\\Local\\Microsoft\\Windows\\INetCache\\",
            @"\\AppData\\Local\\Microsoft\\Windows\\WebCache\\",
            @"\\AppData\\Local\\.*\\Cache\\",
            @"\\AppData\\Local\\.*\\Code Cache\\",
            @"\\AppData\\Local\\Packages\\.*\\AC\\Temp\\",
            @"\\Google\\Chrome\\User Data\\.*\\Cache\\",
            @"\\Mozilla\\Firefox\\Profiles\\.*\\cache2\\",
            @"\\\$Recycle\.Bin\\",
            @"\\System Volume Information\\",
            @"\\Windows\\SoftwareDistribution\\",
            @"\\Windows\.old\\",
            @"\\node_modules\\",
            @"\\\.git\\",
            @"\\\.gradle\\",
            @"\\\.cache\\",
        };
        var fileDefaults = new[]
        {
            @"pagefile\.sys$",
            @"hiberfil\.sys$",
            @"swapfile\.sys$",
            @"desktop\.ini$",
            @"thumbs\.db$",
        };

        _directoryPatterns = directoryDefaults
            .Concat(extraGlobs ?? Enumerable.Empty<string>())
            .Select(Compile)
            .ToList();
        _filePatterns = fileDefaults.Select(Compile).ToList();
    }

    private static Regex Compile(string pattern) => new(pattern, RegexOptions.IgnoreCase | RegexOptions.Compiled);

    public bool IsNoise(string fullPath)
    {
        var normalized = fullPath.Replace('/', '\\');
        if (_filePatterns.Any(p => p.IsMatch(normalized))) return true;

        var padded = normalized.EndsWith('\\') ? normalized : normalized + '\\';
        return _directoryPatterns.Any(p => p.IsMatch(padded));
    }
}
