package io.boonlogic.soul_land.ff4j

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SetupTest {

    @Test
    fun `3 + 4 should be 7`() {

        val given = 3
        val result = given + 4
        val expected = 7

        Assertions.assertEquals(expected, result)
    }

}
