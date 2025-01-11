package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun platform(): String
expect fun getEnvVariable(key: String): String?
expect fun createHttpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient