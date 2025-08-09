package com.example.kioskapp.trigger

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.nanohttpd.protocols.http.IHTTPSession
import org.nanohttpd.protocols.http.NanoHTTPD
import org.nanohttpd.protocols.http.response.FixedStatusCode
import org.nanohttpd.protocols.http.response.Response
import org.nanohttpd.protocols.http.response.Status
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Embedded HTTP trigger that exposes a small REST API on a configurable port.
 * Clients can toggle the trigger state by sending a JSON payload to the
 * `/trigger` endpoint.  The JSON should have the form `{ "state": "on" }`
 * or `{ "state": "off" }`.  A health check is available at `/health` and
 * returns HTTP 200.  The trigger will be inactive until [start] is called.
 */
class HttpTrigger(private val port: Int = 8765) : TriggerSource {
    private val _events = MutableStateFlow(false)
    override val events: StateFlow<Boolean> = _events.asStateFlow()

    private var server: NanoHTTPD? = null

    override fun start() {
        if (server != null) return
        server = object : NanoHTTPD(port) {
            override fun serve(session: IHTTPSession): Response {
                val uri = session.uri
                val method = session.method
                return when {
                    uri == "/health" && method == Method.GET -> Response.newFixedLengthResponse(Status.OK, "text/plain", "OK")
                    uri == "/trigger" && method == Method.POST -> {
                        // Read the body into a string
                        val len = session.headers["content-length"]?.toIntOrNull() ?: 0
                        val body = CharArray(len)
                        session.inputStream.read(body)
                        val payload = String(body).trim().lowercase()
                        Log.d("HttpTrigger", "Received payload: $payload")
                        when {
                            "\"on\"" in payload || "on" in payload -> _events.value = true
                            "\"off\"" in payload || "off" in payload -> _events.value = false
                        }
                        Response.newFixedLengthResponse(Status.OK, "text/plain", "OK")
                    }
                    else -> Response.newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "Not Found")
                }
            }
        }
        try {
            server?.start(0, true)
        } catch (e: Exception) {
            Log.e("HttpTrigger", "Failed to start HTTP server", e)
        }
    }

    override fun stop() {
        server?.stop()
        server = null
    }
}