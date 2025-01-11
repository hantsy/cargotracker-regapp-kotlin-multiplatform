package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

actual fun platform() = "JS"
actual fun getEnvVariable(key: String): String? =
    (js("process.env") as Map<String, String>)[key]

actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Js) {
    config(this)
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println(">>>$message")
            }
        }
    }
    engine {
    }
}