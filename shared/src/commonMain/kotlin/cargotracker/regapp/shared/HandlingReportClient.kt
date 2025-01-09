package cargotracker.regapp.shared

import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class HandlingReport(
    val completionTime: String,
    val trackingId: String,
    val eventType: String,
    val unLocode: String,
    val voyageNumber: String? = null,
)

@kotlinx.serialization.Serializable
sealed interface HandlingResponse {

    class Success : HandlingResponse {
        override fun toString(): String {
            return "OK";
        }
    }

    data class Error(val message: String) : HandlingResponse
}

private const val DEFAULT_BASE_URL = "http://localhost:8080/cargo-tracker"

private val client = httpClient() {
    install(ContentNegotiation) {
        json(Json { isLenient = true; ignoreUnknownKeys = true })
    }
}

suspend fun submitHandlingReport(report: HandlingReport): HandlingResponse {
    val baseUrl = getEnvVariable("HANDLING_REPORT_SERVICE_URL") ?: DEFAULT_BASE_URL

    val response = client.request("$baseUrl/rest/handling/reports") {
        method = HttpMethod.Get
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