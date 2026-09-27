using System.Text.Json;
using System.Text.Json.Serialization;

namespace CepGozcu.Agent.Core.Protocol;

/// <summary>The one JsonSerializerOptions used for every wire message, on both ends of the socket.</summary>
public static class WireJson
{
    public static readonly JsonSerializerOptions Options = new(JsonSerializerDefaults.Web)
    {
        Converters = { new JsonStringEnumConverter(JsonNamingPolicy.CamelCase) },
    };
}
