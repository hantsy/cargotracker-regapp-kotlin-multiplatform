package cargotracker.regapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun randomUUID(): String