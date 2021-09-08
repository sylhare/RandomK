package com.github.sylhare.random

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RandomKTest {

    class A
    class B(val a: A)
    class C(val a: A, val b: B)
    data class D(val a: A, val b: B, val c: C)
    class E {
        constructor(a: A)
        constructor() {
            throw Error("Do not use this one")
        }
    }
    class F(val hello: String)
    class G(val f1: F, val f2: F, val c: Char, val str: String, val l: Long)

    class L {
        val a: N by lazy { HELLO }
        lateinit var b: N
        constructor(str: String) {
            this.b = N(str)
        }
        companion object {
            val HELLO = N("hello")
        }
    }
    class M {
        private val a: A = A()
        private val b: B = B(A())
        private val c: C = C(a, b)
        var number: Int = 0
        constructor() { number = 1 }
        constructor(a: A) { number = 2 }
        constructor(a: A, b: B) { number = 3 }
        constructor(b: B) { number = 4 }
        constructor(c: C) { number = 5 }
        constructor(d: D) { number = D(a, b, c).hashCode() }
    }
    data class N (val value: String)
    sealed class S
    class P {
        private constructor()
    }

    @Test
    fun `Creates single instance using an empty constructor JVM only`() {
        val a: A = makeRandomInstanceJVM()
        assertEquals(a::class.java, A::class.java)
        assertTrue("A@" in a.toString(), "toString of A should contains \$A@ and it is $a")
    }

    @Test
    fun `Creates single instance using an empty constructor`() {
        val a: A = makeRandomInstanceNoArgs()
        assertEquals(a::class.java, A::class.java)
        assertTrue("A@" in a.toString(), "toString of A should contains \$A@ and it is $a")
    }

    @Test
    fun `Creates a single instance using empty constructor`() {
        val a: A = makeRandomInstance()
        assertEquals(a::class.java, A::class.java)
        assertTrue("A@" in a.toString(), "toString of A should contains \$A@ and it is $a")
    }


    @Test
    fun `Creates a single instance using constructor with 1 variable`() {
        val b: B = makeRandomInstance()
        assertEquals(b::class.java, B::class.java)
        assertTrue("\$B@" in b.toString(), "toString of B should contains \$B@ and it is $b")
    }

    @Test
    fun `Creates a single instance using constructor with 2 variables`() {
        val c: C = makeRandomInstance()
        assertEquals(c::class.java, C::class.java)
        assertTrue("\$C@" in c.toString(), "toString of C should contains \$C@ and it is $c")
    }


    @Test
    fun `Creates a single instance using constructor from data class`() {
        val d: D = makeRandomInstance()
        assertEquals(d::class.java, D::class.java)
        assertTrue("D(" in d.toString(), "toString of data class D should contains 'D(' and it is $d")
    }

    @Test
    fun `Skips constructors that cannot be used`() {
        val e: E = makeRandomInstance()
        assertEquals(e::class.java, E::class.java)
        assertTrue("\$E@" in e.toString(), "toString of E should contains \$E@ and it is $e")
    }

    @Test
    fun `With multiple constructors, takes the first one`() {
        val m: M = makeRandomInstance()
        assertEquals(m::class.java, M::class.java)
        assertEquals(m.number, 1)
    }

    @Test
    fun `With lateinit and lazy variables`() {
        val l: L = makeRandomInstance()
        assertNotEquals(l.b, L("tada").b)
        assertNotEquals(l.b.value, "hello")
        assertEquals(l::class.java, L::class.java)
    }

    @Test
    fun `Throws NoUsableConstructor for private constructor`() {
        assertThrows<NoUsableConstructor> { makeRandomInstance<P>() }
    }

    @Test
    fun `Throws NoUsableConstructor for a sealed class`() {
        assertThrows<NoUsableConstructor> { makeRandomInstance<S>() }
    }

    @Test
    fun `Creates number primitives`() {
        assertEquals(Int::class, makeRandomInstance<Int>()::class)
        assertEquals(Long::class, makeRandomInstance<Long>()::class)
        assertEquals(Double::class, makeRandomInstance<Double>()::class)
        assertEquals(Float::class, makeRandomInstance<Float>()::class)
    }

    @Test
    fun `Creates character primitives`() {
        val c = makeRandomInstance<Char>()
        assertEquals(Char::class, c::class)
        assertTrue(c.code in 23..123)
        assertEquals(String::class, makeRandomInstance<String>()::class)
    }

    @Test
    fun `Creates an instance using constructor with primitives and standard types`() {
        val f: F = makeRandomInstance()
        assertEquals(F::class, f::class)

        val g: G = makeRandomInstance()
        assertEquals(G::class, g::class)
        assertTrue("\$G@" in g.toString(), "toString of G should contains \$G@ and it is $g")
    }

    @Test
    }

    @Test
    }


    @Disabled
    @Test
    fun `Creates primitives for Arrays`() {
        assertTrue(makeRandomInstance<Array<Int>>() is Array<Int>)
    }
}
