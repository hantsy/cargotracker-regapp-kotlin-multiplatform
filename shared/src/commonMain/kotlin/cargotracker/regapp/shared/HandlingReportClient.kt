package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class HandlingReport(
    val completionTime: String,
    val trackingId: String,
    val eventType: String,
    val unLocode: String,
    val voyageNumber: String? = null,
)

@Serializable
sealed interface HandlingResponse {

    @Serializable
    open class Success : HandlingResponse {
        override fun toString(): String {
            return "OK";
        }
    }

    @Serializable
    data class Error(val message: String) : HandlingResponse
}

 const val DEFAULT_HANDLING_REPORT_SERVICE_URL =
    "http://localhost:8080/cargo-tracker/rest/handling/reports"

private val httpClient = createHttpClient() {
    install(ContentNegotiation) {
        json(Json { isLenient = true; ignoreUnknownKeys = true })
    }
}

class HandlingReportClient(val client: HttpClient) {
    suspend fun submitReport(report: HandlingReport): HandlingResponse {
        val handlingReportUrl = getEnvVariable("HANDLING_REPORT_SERVICE_URL")
            ?: DEFAULT_HANDLING_REPORT_SERVICE_URL

        val response = client.post(handlingReportUrl) {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(report)
        }

        return if (response.status == HttpStatusCode.OK) {
            response.body(typeInfo<HandlingResponse.Success>())
        } else {
            response.body(typeInfo<HandlingResponse.Error>())
        }
    }
}

suspend fun submitHandlingReport(
    report: HandlingReport,
    block: HandlingResponse.() -> Unit = {},
) {
    val response = HandlingReportClient(httpClient).submitReport(report)
    block.invoke(response)
}
