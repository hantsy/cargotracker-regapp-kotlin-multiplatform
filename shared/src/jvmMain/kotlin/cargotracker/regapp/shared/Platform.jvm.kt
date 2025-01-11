package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.Logging
import java.util.concurrent.TimeUnit

actual fun platform() = System.getProperty("java.vm.name")
actual fun getEnvVariable(key: String): String? = System.getenv()[key]
actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)
    install(Logging)
    engine {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(0, TimeUnit.SECONDS)
        }
    }
}