package cargotracker.regapp

import java.util.UUID

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun randomUUID(): String = UUID.randomUUID().toString()