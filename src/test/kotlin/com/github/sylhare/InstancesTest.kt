package com.github.sylhare

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class InstancesTest {

    @Test
    fun `Creates single instance using an empty constructor JVM only`() {
        val a: MockClasses.A = makeRandomInstanceJVM()
        Assertions.assertEquals(a::class.java, MockClasses.A::class.java)
    }

    @Test
    fun `Creates single instance using an empty constructor`() {
        val a: MockClasses.A = makeRandomInstanceNoArgs()
        Assertions.assertEquals(a::class.java, MockClasses.A::class.java)
    }

    @Test
    fun `Creates single instance from KType`() {
        val a: MockClasses.A = makeRandomInstance(typeOf<MockClasses.A>()) as MockClasses.A
        Assertions.assertEquals(a::class.java, MockClasses.A::class.java)
    }
}
