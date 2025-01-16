package cargotracker.regapp

import kotlin.test.Test
import kotlin.test.assertTrue

class AndroidPlatformTest {

    @Test
    fun `read platform on Android`() {
        val platform = getPlatform()

        assertTrue(platform.name.startsWith("Android"))
    }
}