package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js

actual fun platform() = "NodeJS"
actual fun getEnvVariable(key: String): String? =
    (js("process.env") as Map<String, String>)[key]

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Js) {
    config(this)
    engine {

    }
}