using CepGozcu.Agent.Core.Protocol;
using Microsoft.Extensions.Hosting;

namespace CepGozcu.Agent.Core.Metrics;

/// <summary>Samples CPU/RAM/disk on a short interval and hands each sample to the WS hub to push.</summary>
public sealed class MetricsBroadcastService : BackgroundService
{
    private readonly ISystemMetricsProvider _provider;
    private readonly Action<SystemMetrics> _publish;
    private readonly TimeSpan _interval;

    public MetricsBroadcastService(ISystemMetricsProvider provider, Action<SystemMetrics> publish, TimeSpan? interval = null)
    {
        _provider = provider;
        _publish = publish;
        _interval = interval ?? TimeSpan.FromSeconds(2);
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        using var timer = new PeriodicTimer(_interval);
        do
        {
            try
            {
                _publish(_provider.GetMetrics());
            }
            catch (Exception ex) when (ex is IOException or UnauthorizedAccessException)
            {
                // Transient hardware-read failure — skip this tick, try again next interval.
            }
        }
        while (!stoppingToken.IsCancellationRequested && await timer.WaitForNextTickAsync(stoppingToken));
    }
}
