/* Copyright (c) 2026 ttt. All rights reserved. */
package ch.ttt.demomaven

import org.junit.jupiter.api.Test
import kotlin.test.Ignore
import kotlin.test.fail

class FailingTest {
    @Ignore
    @Test
    fun `failing test`() {
        fail()
    }
}
