package org.cargotracker.regapp.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

actual fun platform() = "Wasm-JS"
actual fun getEnvVariable(key: String): String? = null // Env variables in JS are handled differently
actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient {
    config(this)
}
