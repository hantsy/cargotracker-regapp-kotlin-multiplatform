package org.cargotracker.regapp.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.java.Java

actual fun platform() = "JVM"
actual fun getEnvVariable(key: String): String? = System.getProperty(key) ?: System.getenv(key)
actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Java) {
    config(this)
}
