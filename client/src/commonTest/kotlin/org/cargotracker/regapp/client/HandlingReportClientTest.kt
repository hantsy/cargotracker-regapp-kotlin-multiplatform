package org.cargotracker.regapp.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.LocalDateTime
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HandlingReportClientTest {

    @Test
    fun testSubmitReportSuccess() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
        }
        val client = HandlingReportClient(httpClient)
        val report = HandlingReport(
            completionTime = LocalDateTime(2025, 3, 4, 12, 0),
            trackingId = "ABC-123",
            eventType = EventType.LOAD,
            unLocode = "CNHKG"
        )
        val response = client.submitReport(report)
        assertEquals(HandlingResponse.Success, response)
    }

    @Test
    fun testSubmitReportError() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"message": "Invalid Tracking ID"}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
        }
        val client = HandlingReportClient(httpClient)
        val report = HandlingReport(
            completionTime = LocalDateTime(2025, 3, 4, 12, 0),
            trackingId = "INVALID",
            eventType = EventType.LOAD,
            unLocode = "CNHKG"
        )
        val response = client.submitReport(report)
        assertTrue(response is HandlingResponse.Error)
        assertEquals("Invalid Tracking ID", response.message)
    }
}
