package com.jn.winremote.protocol

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Round-trips every message shape in PROTOCOL.md §4 through the real
 * kotlinx.serialization codec, checking exact wire field names (not just
 * "it decodes to *something*") against the literal JSON in the spec.
 */
class ProtocolCodecTest {

    private fun parse(json: String): JsonObject = Json.parseToJsonElement(json).jsonObject

    // -- Client -> Server --------------------------------------------------

    @Test
    fun `hello encodes exact wire shape`() {
        val encoded = encodeClientMessage(ClientMessage.Hello())
        val obj = parse(encoded)
        assertEquals("hello", obj["type"]?.toString()?.trim('"'))
        assertEquals("1", obj["proto_version"].toString())
        assertEquals("\"android\"", obj["client"].toString())
        assertEquals("\"1.0.0\"", obj["app_version"].toString())
    }

    @Test
    fun `pair_request round trip`() {
        val msg = ClientMessage.PairRequest(
            requestId = "req-1",
            pairingCode = "7F3K-9QRT",
            deviceName = "Jake Pixel 8",
        )
        val encoded = encodeClientMessage(msg)
        val obj = parse(encoded)
        assertEquals("pair_request", obj["type"]!!.toString().trim('"'))
        assertEquals("\"req-1\"", obj["request_id"].toString())
        assertEquals("\"7F3K-9QRT\"", obj["pairing_code"].toString())
        assertEquals("\"Jake Pixel 8\"", obj["device_name"].toString())
    }

    @Test
    fun `auth_request and auth_response use snake_case keys`() {
        val authRequest = encodeClientMessage(ClientMessage.AuthRequest("r1", "3f9c"))
        val authRequestObj = parse(authRequest)
        assertEquals("auth_request", authRequestObj["type"]!!.toString().trim('"'))
        assertEquals("\"3f9c\"", authRequestObj["device_id"].toString())

        val authResponse = encodeClientMessage(ClientMessage.AuthResponse("r1", "aGVsbG8="))
        val authResponseObj = parse(authResponse)
        assertEquals("auth_response", authResponseObj["type"]!!.toString().trim('"'))
        assertEquals("\"aGVsbG8=\"", authResponseObj["hmac_b64"].toString())
    }

    @Test
    fun `subscribe carries topics list and no request_id`() {
        val encoded = encodeClientMessage(ClientMessage.Subscribe(listOf("metrics", "file_events", "alerts")))
        val obj = parse(encoded)
        assertEquals("subscribe", obj["type"]!!.toString().trim('"'))
        assertTrue(obj["topics"].toString().contains("metrics"))
        assertNull(obj["request_id"])
    }

    @Test
    fun `kill_process carries pid`() {
        val encoded = encodeClientMessage(ClientMessage.KillProcess("r2", 1234))
        val obj = parse(encoded)
        assertEquals("kill_process", obj["type"]!!.toString().trim('"'))
        assertEquals("1234", obj["pid"].toString())
    }

    @Test
    fun `list_file_events carries all filter fields`() {
        val msg = ClientMessage.ListFileEvents(
            requestId = "r3",
            fromTs = 100,
            toTs = 200,
            op = "created",
            minSizeBytes = 1024,
            pathPrefix = "C:\\Users\\jake\\Downloads",
            limit = 50,
            offset = 10,
        )
        val obj = parse(encodeClientMessage(msg))
        assertEquals("list_file_events", obj["type"]!!.toString().trim('"'))
        assertEquals("100", obj["from_ts"].toString())
        assertEquals("200", obj["to_ts"].toString())
        assertEquals("\"created\"", obj["op"].toString())
        assertEquals("1024", obj["min_size_bytes"].toString())
        assertEquals("50", obj["limit"].toString())
        assertEquals("10", obj["offset"].toString())
    }

    // -- Server -> Client ----------------------------------------------------

    @Test
    fun `decodes hello_ack`() {
        val json = """{"type":"hello_ack","proto_version":1,"agent_version":"1.0.0","hostname":"DESKTOP-ABC"}"""
        val msg = decodeServerMessage(json)
        assertTrue(msg is ServerMessage.HelloAck)
        val ack = msg as ServerMessage.HelloAck
        assertEquals(1, ack.protoVersion)
        assertEquals("1.0.0", ack.agentVersion)
        assertEquals("DESKTOP-ABC", ack.hostname)
    }

    @Test
    fun `decodes pair_success and pair_failed`() {
        val success = decodeServerMessage(
            """{"type":"pair_success","request_id":"r1","device_id":"3f9c","device_secret_b64":"c2VjcmV0"}"""
        )
        assertTrue(success is ServerMessage.PairSuccess)
        assertEquals("3f9c", (success as ServerMessage.PairSuccess).deviceId)
        assertEquals("c2VjcmV0", success.deviceSecretB64)

        val failed = decodeServerMessage("""{"type":"pair_failed","request_id":"r1","reason":"invalid_code"}""")
        assertTrue(failed is ServerMessage.PairFailed)
        assertEquals("invalid_code", (failed as ServerMessage.PairFailed).reason)
    }

    @Test
    fun `decodes auth handshake messages`() {
        val challenge = decodeServerMessage("""{"type":"auth_challenge","request_id":"r1","nonce":"bm9uY2U="}""")
        assertTrue(challenge is ServerMessage.AuthChallenge)
        assertEquals("bm9uY2U=", (challenge as ServerMessage.AuthChallenge).nonce)

        val success = decodeServerMessage("""{"type":"auth_success","request_id":"r1"}""")
        assertTrue(success is ServerMessage.AuthSuccess)

        val failed = decodeServerMessage("""{"type":"auth_failed","request_id":"r1","reason":"bad_hmac"}""")
        assertTrue(failed is ServerMessage.AuthFailed)
        assertEquals("bad_hmac", (failed as ServerMessage.AuthFailed).reason)
    }

    @Test
    fun `decodes metrics with nested ram and disks`() {
        val json = """
            {"type":"metrics","ts":1234567890,"cpu_percent":23.4,
             "ram":{"total_bytes":1000,"used_bytes":400},
             "disks":[{"volume":"C:\\","total_bytes":2000,"used_bytes":1200,"free_bytes":800}]}
        """.trimIndent()
        val msg = decodeServerMessage(json)
        assertTrue(msg is ServerMessage.Metrics)
        val metrics = msg as ServerMessage.Metrics
        assertEquals(1234567890L, metrics.ts)
        assertEquals(23.4, metrics.cpuPercent, 0.0001)
        assertEquals(1000L, metrics.ram.totalBytes)
        assertEquals(400L, metrics.ram.usedBytes)
        assertEquals(1, metrics.disks.size)
        assertEquals("C:\\", metrics.disks[0].volume)
        assertEquals(800L, metrics.disks[0].freeBytes)
    }

    @Test
    fun `decodes file_event with null old_path`() {
        val json = """
            {"type":"file_event","ts":10,"op":"created",
             "path":"C:\\Users\\jake\\Downloads\\big.zip","old_path":null,
             "size_bytes":500,"is_dir":false}
        """.trimIndent()
        val msg = decodeServerMessage(json)
        assertTrue(msg is ServerMessage.FileEvent)
        val event = msg as ServerMessage.FileEvent
        assertEquals("created", event.op)
        assertNull(event.oldPath)
        assertEquals(500L, event.sizeBytes)
    }

    @Test
    fun `decodes alert with arbitrary context object`() {
        val json = """
            {"type":"alert","ts":10,"id":"a1","kind":"large_file","severity":"warning",
             "message":"Big file detected","context":{"path":"C:\\big.zip","size_bytes":600000000}}
        """.trimIndent()
        val msg = decodeServerMessage(json)
        assertTrue(msg is ServerMessage.Alert)
        val alert = msg as ServerMessage.Alert
        assertEquals("large_file", alert.kind)
        assertEquals("warning", alert.severity)
        assertTrue(alert.context.toString().contains("big.zip"))
    }

    @Test
    fun `decodes process_list with protected flag`() {
        val json = """
            {"type":"process_list","request_id":"r5","items":[
              {"pid":1234,"name":"chrome.exe","exe":"C:\\chrome.exe","user":"DESKTOP\\jake",
               "cpu_percent":4.2,"ram_bytes":184320000,"protected":false}
            ]}
        """.trimIndent()
        val msg = decodeServerMessage(json)
        assertTrue(msg is ServerMessage.ProcessList)
        val list = msg as ServerMessage.ProcessList
        assertEquals(1, list.items.size)
        assertEquals(1234, list.items[0].pid)
        assertEquals(false, list.items[0].isProtected)
    }

    @Test
    fun `decodes action_result success and failure variants`() {
        val success = decodeServerMessage(
            """{"type":"action_result","request_id":"r6","action":"kill_process","success":true}"""
        )
        assertTrue(success is ServerMessage.ActionResult)
        assertEquals(true, (success as ServerMessage.ActionResult).success)
        assertNull(success.reason)

        val failure = decodeServerMessage(
            """{"type":"action_result","request_id":"r6","action":"kill_process","success":false,
                "reason":"critical_process_protected"}"""
        )
        assertTrue(failure is ServerMessage.ActionResult)
        assertEquals("critical_process_protected", (failure as ServerMessage.ActionResult).reason)

        val launch = decodeServerMessage(
            """{"type":"action_result","request_id":"r7","action":"launch_app","success":true,"pid":5678}"""
        )
        assertTrue(launch is ServerMessage.ActionResult)
        assertEquals(5678, (launch as ServerMessage.ActionResult).pid)
    }

    @Test
    fun `decodes top_growth_result`() {
        val json = """
            {"type":"top_growth_result","request_id":"r8","window":"1h","items":[
              {"path":"C:\\Users\\jake\\Videos","is_dir":true,"delta_bytes":1073741824,
               "size_before":100,"size_after":1073741924}
            ]}
        """.trimIndent()
        val msg = decodeServerMessage(json)
        assertTrue(msg is ServerMessage.TopGrowthResult)
        val result = msg as ServerMessage.TopGrowthResult
        assertEquals("1h", result.window)
        assertEquals(1073741824L, result.items[0].deltaBytes)
    }

    @Test
    fun `decodes history items`() {
        val json = """
            {"type":"history","request_id":"r9","total":1,"items":[
              {"ts":10,"device_id":"3f9c","device_name":"Jake Pixel 8","action":"kill_process",
               "success":true,"detail":"chrome.exe (pid 1234)","reason":null}
            ]}
        """.trimIndent()
        val msg = decodeServerMessage(json)
        assertTrue(msg is ServerMessage.HistoryResult)
        val result = msg as ServerMessage.HistoryResult
        assertEquals(1, result.total)
        assertEquals("kill_process", result.items[0].action)
        assertNull(result.items[0].reason)
    }

    @Test
    fun `decodes error message`() {
        val json = """{"type":"error","request_id":"r10","reason":"bad_request","message":"nope"}"""
        val msg = decodeServerMessage(json)
        assertTrue(msg is ServerMessage.Error)
        assertEquals("bad_request", (msg as ServerMessage.Error).reason)
    }

    @Test
    fun `unknown message type decodes to null instead of throwing`() {
        val json = """{"type":"some_future_message","foo":"bar"}"""
        assertNull(decodeServerMessage(json))
    }

    @Test
    fun `malformed json decodes to null instead of throwing`() {
        assertNull(decodeServerMessage("{not json"))
    }

    @Test
    fun `requestIdOrNull covers request-carrying and push messages`() {
        assertEquals("r1", ServerMessage.AuthSuccess("r1").requestIdOrNull())
        assertNull(
            ServerMessage.Metrics(ts = 1, cpuPercent = 1.0, ram = RamInfo(1, 1)).requestIdOrNull()
        )
    }
}
