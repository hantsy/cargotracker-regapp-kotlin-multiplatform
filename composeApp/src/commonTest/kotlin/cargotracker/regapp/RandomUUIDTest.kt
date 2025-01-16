package cargotracker.regapp

import kotlin.test.Test
import kotlin.test.assertNotNull

class RandomUUIDTest {

    @Test
    fun `generate random UUID`() {
        val randomUUID = randomUUID()
        println("generated randomUUID: $randomUUID")

        assertNotNull(randomUUID)
    }
}