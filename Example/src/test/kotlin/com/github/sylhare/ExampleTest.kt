package com.github.sylhare

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@ExperimentalStdlibApi
internal class ExampleTest {

    private val example: Example = randomK()
    private val other = randomK<OtherExample>()

    @Test
    fun `Dummy example using randomK in a test`() {
        val presenter = Presenter()
        assertEquals(null, presenter.other)
        presenter.other = randomK<OtherExample>()
        assertNotNull(presenter.other)
    }

    @Test
    fun `Example dummy test`() {
        assertEquals(example, example)
        assertEquals(example::class, Example::class)
    }

    @Test
    fun `OtherExample dummy test`() {
        assertEquals(other, other)
        assertEquals(other::class, OtherExample::class)
    }
}
