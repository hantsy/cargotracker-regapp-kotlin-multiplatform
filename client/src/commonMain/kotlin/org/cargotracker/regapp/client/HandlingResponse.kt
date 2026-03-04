package org.cargotracker.regapp.client

import kotlinx.serialization.Serializable

@Serializable
sealed interface HandlingResponse {
    @Serializable
    data object Success : HandlingResponse {
        override fun toString(): String = "OK"
    }

    @Serializable
    data class Error(val message: String) : HandlingResponse
}