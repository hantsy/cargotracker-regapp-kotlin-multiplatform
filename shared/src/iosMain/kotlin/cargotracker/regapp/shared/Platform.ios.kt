package cargotracker.regapp.shared

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin
import platform.Foundation.NSProcessInfo
actual fun platform() = "iOS"
actual fun getEnvVariable(key: String): String? = NSProcessInfo.processInfo.environment[key] as String?
actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {
    config(this)
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}