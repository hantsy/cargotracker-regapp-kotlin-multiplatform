package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.reflect.TypeInfo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class JvmHandlingReportClientTest {

    val report = HandlingReport(
        completionTime = "1/10/2025 27:59",
        trackingId = "AAA",
        eventType = "LOAD",
        unLocode = "CHSHA",
        voyageNumber = "A0123"
    )

    @Test
    fun `submit report`() = runTest {
        val mockHttpClient: HttpClient = mockk()
        val mockResponse: HttpResponse = mockk()

        coEvery { mockResponse.status } returns HttpStatusCode.OK
        coEvery { mockResponse.body<HandlingResponse.Success>(any(TypeInfo::class)) } returns HandlingResponse.Success()
        val mockFunc: HttpRequestBuilder.() -> Unit = mockk(relaxed = true)

        coEvery { mockFunc.invoke(any())} returns Unit

        coEvery { mockHttpClient.post(any<String>(), mockFunc) } returns mockResponse

        val client = HandlingReportClient(mockHttpClient)
        val result = client.submitReport(report)
        result is HandlingResponse.Success
        assertEquals("OK", result.toString())
    }

    @Test
    fun `submit report(with respond error)`() = runTest {
        val mockHttpClient: HttpClient = mockk()
        val mockResponse: HttpResponse = mockk()

        coEvery { mockResponse.status } returns HttpStatusCode.BadRequest
        coEvery { mockResponse.body<HandlingResponse.Error>(any(TypeInfo::class)) } returns HandlingResponse.Error(
            "err"
        )
        val mockFunc: HttpRequestBuilder.() -> Unit = mockk(relaxed = true)
        coEvery { mockHttpClient.post(any<String>(), mockFunc) } returns mockResponse

        val client = HandlingReportClient(mockHttpClient)
        val result = client.submitReport(report)
        result is HandlingResponse.Error
        assertEquals("err", (result as HandlingResponse.Error).message)
    }

}