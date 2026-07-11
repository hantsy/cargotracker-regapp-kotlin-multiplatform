package org.cargotracker.regapp.client

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
import kotlinx.serialization.json.Json

const val DEFAULT_HANDLING_REPORT_SERVICE_URL =
    "http://localhost:8080/cargo-tracker/rest/handling/reports"

class HandlingReportClient(
    private val client: HttpClient,
    private val baseUrl: String = getEnvVariable("HANDLING_REPORT_SERVICE_URL")
        ?: DEFAULT_HANDLING_REPORT_SERVICE_URL,
) {
    suspend fun submitReport(report: HandlingReport): HandlingResponse {
        val response = client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(report)
        }

        return if (response.status == HttpStatusCode.OK) {
            HandlingResponse.Success
        } else {
            response.body(typeInfo<HandlingResponse.Error>())
        }
    }

    companion object {
        fun create(
            baseUrl: String = getEnvVariable("HANDLING_REPORT_SERVICE_URL")
                ?: DEFAULT_HANDLING_REPORT_SERVICE_URL,
        ): HandlingReportClient {
            val httpClient = createHttpClient {
                install(ContentNegotiation) {
                    json(Json { isLenient = true; ignoreUnknownKeys = true })
                }
            }
            return HandlingReportClient(httpClient, baseUrl)
        }
    }
}
