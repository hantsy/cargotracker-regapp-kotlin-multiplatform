package cargotracker.regapp

import kotlin.test.Test
import kotlin.test.assertTrue

class IOSPlatformTest {

    @Test
    fun `read platform on IOS`() {
        val platform = getPlatform()
        assertTrue(platform.name.startsWith("IOS"))
    }
}