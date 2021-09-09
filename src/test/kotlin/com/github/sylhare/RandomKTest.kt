package com.github.sylhare

import com.github.sylhare.MockClasses.A
import com.github.sylhare.MockClasses.B
import com.github.sylhare.MockClasses.C
import com.github.sylhare.MockClasses.D
import com.github.sylhare.MockClasses.E
import com.github.sylhare.MockClasses.F
import com.github.sylhare.MockClasses.G
import com.github.sylhare.MockClasses.GA
import com.github.sylhare.MockClasses.GAA
import com.github.sylhare.MockClasses.GT
import com.github.sylhare.MockClasses.GTA
import com.github.sylhare.MockClasses.L
import com.github.sylhare.MockClasses.M
import com.github.sylhare.MockClasses.P
import com.github.sylhare.MockClasses.S

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalStdlibApi
class RandomKTest {

    @Test
    fun `Creates single instance using an empty constructor JVM only`() {
        val a: A = makeRandomInstanceJVM()
        assertEquals(a::class.java, A::class.java)
        assertTrue("A@" in a.toString(), "toString of A should contains A@ and it is $a")
    }

    @Test
    fun `Creates single instance using an empty constructor`() {
        val a: A = makeRandomInstanceNoArgs()
        assertEquals(a::class.java, A::class.java)
        assertTrue("A@" in a.toString(), "toString of A should contains A@ and it is $a")
    }

    @Test
    fun `Creates a single instance using empty constructor`() {
        val a: A = makeRandomInstance()
        assertEquals(a::class.java, A::class.java)
        assertTrue("A@" in a.toString(), "toString of A should contains A@ and it is $a")
    }


    @Test
    fun `Creates a single instance using constructor with 1 variable`() {
        val b: B = makeRandomInstance()
        assertEquals(b::class.java, B::class.java)
        assertTrue("B@" in b.toString(), "toString of B should contains B@ and it is $b")
    }

    @Test
    fun `Creates a single instance using constructor with 2 variables`() {
        val c: C = makeRandomInstance()
        assertEquals(c::class.java, C::class.java)
        assertTrue("C@" in c.toString(), "toString of C should contains C@ and it is $c")
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
        assertTrue("E@" in e.toString(), "toString of E should contains E@ and it is $e")
    }

    @Test
    fun `With multiple constructors, takes a random one`() {
        val m: M = makeRandomInstance()
        assertEquals(m::class.java, M::class.java)
        assertTrue((1..10).map { makeRandomInstance<M>().number }.toSet().size > 1)
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
        assertTrue("G@" in g.toString(), "toString of G should contains G@ and it is $g")
    }

    @Test
    fun `Creates lists`() {
        val ints: List<Int> = makeRandomInstance()
        assertEquals("class java.util.ArrayList", ints::class.toString())
        assertTrue(ints.toString().startsWith("["))
        assertTrue(ints.toString().endsWith("]"))
    }

    @Test
    fun `Creates maps`() {
        val map = makeRandomInstance<Map<Long, String>>()
        assertEquals(mapOf(1L to "string", 2L to "string")::class, map::class)
        assertNotEquals(mapOf<Long, String>()::class, map::class)
        assertTrue(map.toString().startsWith("{"))
        assertTrue(map.toString().endsWith("}"))

        assertEquals(linkedMapOf<String, B>()::class, makeRandomInstance<LinkedHashMap<String, B>>()::class)
        assertEquals(hashMapOf<A, B>()::class, makeRandomInstance<HashMap<A, B>>()::class)
    }


    @Test
    fun `Creates sets`() {
        val set: Set<F> = setOf(F("3"), F("2"))
        assertEquals(set::class, makeRandomInstance<Set<F>>()::class)
    }

    @Test
    fun `Creates collections`() {
        val g = makeRandomInstance<G>()
        assertEquals(G::class, g::class)
        assertTrue(makeRandomInstance<Collection<A>>() is Collection<A>)
    }

    @Disabled
    @Test
    fun `Creates primitives for Arrays`() {
        assertEquals(intArrayOf(10, 20, 30, 40, 50)::class, makeRandomInstance<IntArray>()::class)
        assertEquals(arrayOf("string")::class, makeRandomInstance<Array<String>>()::class)
        assertEquals(arrayOf(1)::class, makeRandomInstance<Array<Int>>()::class)
    }

    @Test
    fun `Generic classes are supported`() {
        val ga1: GA<Int> = makeRandomInstance()
        assertEquals(ga1.t::class, Int::class)

        val ga2: GA<String> = makeRandomInstance()
        assertEquals(ga2.t::class, String::class)
    }

    @Test
    fun `Generic classes are supported default constructor`() {
        val gt1 = makeRandomInstance<GT<Int>>()
        gt1.t = 1
        assertEquals(1, gt1.t)

        val gt2 = makeRandomInstance<GT<Long>>()
        gt2.t = 1L
        assertEquals(1L, gt2.t)
    }

    @Test
    fun `Generic classes recursive are supported`() {
        val gt1 = makeRandomInstance<GT<Int>>()
        val gtRecursive = makeRandomInstance<GT<GT<Int>>>()
        gtRecursive.t = gt1
        assertEquals(gt1, gtRecursive.t)

        val gaaga: GAA<Long, GA<GT<Int>>> = makeRandomInstance()
        assertEquals(gaaga.t1::class, Long::class)
        assertTrue(gaaga.t2 is GA<GT<Int>>)

        val gggg: GA<GA<GA<Int>>> = makeRandomInstance()
        gggg.t.t.t = 10
        assertEquals(10, gggg.t.t.t)
        gggg.t.t = GA(20)
        assertEquals(20, gggg.t.t.t)
    }

    @Test
    fun `Generic classes with multiple arguments are supported`() {
        val gaa1: GAA<Int, String> = makeRandomInstance()
        assertEquals(gaa1.t1::class, Int::class)
        assertEquals(gaa1.t2::class, String::class)

        val gaa2: GAA<Long, List<Int>> = makeRandomInstance()
        assertEquals(gaa2.t1::class, Long::class)
        assertTrue(gaa2.t2 is List<Int>)

        val gta: GTA<Long, String> = makeRandomInstance()
        assertTrue(gta.t2.length > 1)
    }
}
