package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
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

    @Test
    fun `submit report`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """""",
                status = HttpStatusCode.Companion.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HandlingReportClient(HttpClient(mockEngine))
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
        val client = HandlingReportClient(HttpClient(mockEngine))
        val result = client.submitReport(report)
        result is HandlingResponse.Error
        assertEquals("err", (result as HandlingResponse.Error).message)
    }

}