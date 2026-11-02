package ch.ttt.demo_github_actions

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