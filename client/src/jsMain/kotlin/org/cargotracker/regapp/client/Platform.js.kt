package org.cargotracker.regapp.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js

actual fun platform() = "JS"
actual fun getEnvVariable(key: String): String? = null // Env variables in JS browser are handled differently
actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Js) {
    config(this)
}
