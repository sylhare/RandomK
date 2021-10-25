package com.github.sylhare.demo

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class getKTypeTest {

    @Test
    fun `getKType test`() {
        Assertions.assertEquals(typeOf<List<Map<String, Array<Double>>>>(), getKType<List<Map<String, Array<Double>>>>())
        Assertions.assertEquals(typeOf<Array<Array<String>>>(), getKType<Array<Array<String>>>())
        Assertions.assertEquals(typeOf<Unit>(), getKType<Unit>())
    }

    @Test
    fun `getKType differences test`() {
        Assertions.assertNotEquals(typeOf<Array<*>>(), getKType<Array<*>>()) // typeOf transform "*" into "Any"
        Assertions.assertNotEquals(typeOf<List<*>>(), getKType<List<*>>())
    }
}
