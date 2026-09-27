using CepGozcu.Agent.Core.Data;
using CepGozcu.Agent.Core.Security;
using CepGozcu.Agent.Tests.TestSupport;
using Xunit;

namespace CepGozcu.Agent.Tests.Core;

public class TokenServiceTests : IDisposable
{
    private readonly TempDb _db = new();
    private readonly DeviceRepository _devices;
    private readonly TokenService _tokens;

    public TokenServiceTests()
    {
        _devices = new DeviceRepository(_db.Factory);
        _tokens = new TokenService(_devices);
    }

    public void Dispose() => _db.Dispose();

    private string InsertDevice()
    {
        var id = Guid.NewGuid().ToString("N");
        _devices.Insert(new DeviceRecord(id, "Test Phone", "pk", null, null, "read,control", DateTimeOffset.UtcNow, null, false, null));
        return id;
    }

    [Fact]
    public void IssuedTokenValidatesToTheSameDevice()
    {
        var deviceId = InsertDevice();
        var issued = _tokens.IssueFor(deviceId);

        var resolved = _tokens.ValidateSession(issued.SessionToken);

        Assert.NotNull(resolved);
        Assert.Equal(deviceId, resolved!.Id);
    }

    [Fact]
    public void GarbageTokenDoesNotValidate()
    {
        Assert.Null(_tokens.ValidateSession("not-a-real-token"));
    }

    [Fact]
    public void RevokedDeviceTokenNoLongerValidates()
    {
        var deviceId = InsertDevice();
        var issued = _tokens.IssueFor(deviceId);

        _tokens.Revoke(deviceId);

        Assert.Null(_tokens.ValidateSession(issued.SessionToken));
    }

    [Fact]
    public void RefreshRotatesTheSessionToken()
    {
        var deviceId = InsertDevice();
        var issued = _tokens.IssueFor(deviceId);

        var refreshed = _tokens.Refresh(deviceId, issued.RefreshToken);

        Assert.NotNull(refreshed);
        Assert.NotEqual(issued.SessionToken, refreshed!.SessionToken);
        Assert.Null(_tokens.ValidateSession(issued.SessionToken)); // old token is gone
        Assert.NotNull(_tokens.ValidateSession(refreshed.SessionToken));
    }

    [Fact]
    public void RefreshFailsWithWrongRefreshToken()
    {
        var deviceId = InsertDevice();
        _tokens.IssueFor(deviceId);

        Assert.Null(_tokens.Refresh(deviceId, "wrong-refresh-token"));
    }
}
