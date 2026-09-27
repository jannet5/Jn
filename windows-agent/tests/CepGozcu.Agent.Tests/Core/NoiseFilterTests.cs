using CepGozcu.Agent.Core.Disk;
using Xunit;

namespace CepGozcu.Agent.Tests.Core;

public class NoiseFilterTests
{
    private readonly NoiseFilter _filter = new();

    [Theory]
    [InlineData(@"C:\Users\jake\AppData\Local\Temp\abc.tmp")]
    [InlineData(@"C:\Windows\Temp\setup.log")]
    [InlineData(@"C:\Users\jake\Projects\app\node_modules\left-pad\index.js")]
    [InlineData(@"C:\Users\jake\Projects\app\.git\objects\pack\pack-abc.pack")]
    [InlineData(@"C:\pagefile.sys")]
    [InlineData(@"C:\Users\jake\AppData\Local\Google\Chrome\User Data\Default\Cache\f_000001")]
    public void FlagsKnownNoisyPaths(string path)
    {
        Assert.True(_filter.IsNoise(path));
    }

    [Theory]
    [InlineData(@"C:\Users\jake\Documents\report.docx")]
    [InlineData(@"C:\Users\jake\Downloads\movie.mp4")]
    [InlineData(@"D:\Projects\app\src\Program.cs")]
    public void DoesNotFlagOrdinaryUserFiles(string path)
    {
        Assert.False(_filter.IsNoise(path));
    }

    [Fact]
    public void ExtraGlobsAreHonored()
    {
        var filter = new NoiseFilter(new[] { @"\\MyNoisyFolder\\" });
        Assert.True(filter.IsNoise(@"C:\Users\jake\MyNoisyFolder\file.bin"));
    }
}
