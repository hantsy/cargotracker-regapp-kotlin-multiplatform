package cargotracker.regapp

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
@OptIn(ExperimentalUuidApi::class)
actual fun randomUUID(): String = Uuid.random().toString()