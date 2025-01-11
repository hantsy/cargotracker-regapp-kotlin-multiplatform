package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class HandlingReportClientTest {

    val report = HandlingReport(
        completionTime = "1/10/2025 27:59",
        trackingId = "AAA",
        eventType = "LOAD",
        unLocode = "CHSHA",
        voyageNumber = "A0123"
    )

    fun createHttpClient(mockEngine: MockEngine): HttpClient {
        return HttpClient(mockEngine){
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
        }
    }

    @Test
    fun `submit report`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{}""",
                status = HttpStatusCode.Companion.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HandlingReportClient(createHttpClient(mockEngine))
        val result = client.submitReport(report)
        result is HandlingResponse.Success
        assertEquals("OK", result.toString())
    }

    @Test
    fun `submit report(with respond error)`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "message":"err"
                    }
                """.trimIndent(),
                status = HttpStatusCode.Companion.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HandlingReportClient(createHttpClient(mockEngine))
        val result = client.submitReport(report)
        result is HandlingResponse.Error
        assertEquals("err", (result as HandlingResponse.Error).message)
    }

}