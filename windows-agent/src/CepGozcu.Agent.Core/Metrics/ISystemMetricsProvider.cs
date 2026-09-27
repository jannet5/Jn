using CepGozcu.Agent.Core.Protocol;

namespace CepGozcu.Agent.Core.Metrics;

public interface ISystemMetricsProvider
{
    SystemMetrics GetMetrics();
}
