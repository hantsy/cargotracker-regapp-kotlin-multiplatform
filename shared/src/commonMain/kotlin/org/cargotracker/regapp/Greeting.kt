package org.cargotracker.regapp

class Greeting {
    private val platform = platform()

    fun greet(): String {
        return "Hello, $platform!"
    }
}