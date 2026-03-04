package org.cargotracker.regapp.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JvmHandlingReportClientTest {

    private lateinit var server: MockWebServer
    private lateinit var client: HandlingReportClient

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        val httpClient = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
        }
        client = HandlingReportClient(httpClient)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private val report = HandlingReport(
        completionTime = LocalDateTime(2025, 3, 4, 12, 0),
        trackingId = "AAA",
        eventType = EventType.LOAD,
        unLocode = "CHSHA",
        voyageNumber = "A0123"
    )

    @Test
    fun testSubmitReport() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(200))

        // System property used for JVM platform getEnvVariable implementation
        System.setProperty("HANDLING_REPORT_SERVICE_URL", server.url("/").toString())

        val result = client.submitReport(report)
        
        assertTrue(result is HandlingResponse.Success)

        val recordedRequest = server.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/", recordedRequest.path)
    }

    @Test
    fun testSubmitReportWithError() = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody("""{"message": "err"}""")
                .setHeader("Content-Type", "application/json")
        )

        System.setProperty("HANDLING_REPORT_SERVICE_URL", server.url("/").toString())

        val result = client.submitReport(report)
        
        assertTrue(result is HandlingResponse.Error)
        assertEquals("err", result.message)
    }
}
