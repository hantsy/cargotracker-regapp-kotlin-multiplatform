package cargotracker.regapp

import kotlin.test.Test
import kotlin.test.assertTrue

class DesktopPlatformTest {

    @Test
    fun `read platform on Desktop(jvm)`() {
        val platform = getPlatform()
        assertTrue(platform.name.startsWith("Java"))
    }
}