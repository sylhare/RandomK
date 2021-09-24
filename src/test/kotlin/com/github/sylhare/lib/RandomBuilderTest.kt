package com.github.sylhare.lib

import com.github.sylhare.mock.MockClasses.A
import com.github.sylhare.mock.MockClasses.B
import com.github.sylhare.mock.MockClasses.C
import com.github.sylhare.mock.MockClasses.D
import com.github.sylhare.mock.MockClasses.E
import com.github.sylhare.mock.MockClasses.F
import com.github.sylhare.mock.MockClasses.G
import com.github.sylhare.mock.MockClasses.GA
import com.github.sylhare.mock.MockClasses.GAA
import com.github.sylhare.mock.MockClasses.GT
import com.github.sylhare.mock.MockClasses.GTA
import com.github.sylhare.mock.MockClasses.I
import com.github.sylhare.mock.MockClasses.J
import com.github.sylhare.mock.MockClasses.L
import com.github.sylhare.mock.MockClasses.M
import com.github.sylhare.mock.MockClasses.P
import com.github.sylhare.mock.MockClasses.S
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalStdlibApi
@Suppress("USELESS_IS_CHECK")
class RandomBuilderTest {

    @Test
    fun `Creates a single instance using empty constructor`() {
        val a: A = makeRandomInstance()
        assertEquals(a::class.java, A::class.java)
    }

    @Test
    fun `Creates a single instance using constructor with 1 variable`() {
        val b: B = makeRandomInstance()
        assertEquals(b::class.java, B::class.java)
    }

    @Test
    fun `Creates a single instance using constructor with 2 variables`() {
        val c: C = makeRandomInstance()
        assertEquals(c::class.java, C::class.java)
    }

    @Test
    fun `Creates a single instance using constructor from data class`() {
        val d: D = makeRandomInstance()
        assertEquals(d::class.java, D::class.java)
    }

    @Test
    fun `Skips constructors that cannot be used`() {
        val e: E = makeRandomInstance()
        assertEquals(e::class.java, E::class.java)
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
    fun `Creates boolean primitives`() {
        assertEquals(Boolean::class, makeRandomInstance<Boolean>()::class)
    }

    @Test
    fun `Creates number primitives`() {
        assertEquals(Int::class, makeRandomInstance<Int>()::class)
        assertEquals(Long::class, makeRandomInstance<Long>()::class)
        assertEquals(Double::class, makeRandomInstance<Double>()::class)
        assertEquals(Float::class, makeRandomInstance<Float>()::class)
    }

    @Test
    fun `Creates Byte primitives`() {
        assertEquals(Byte::class, makeRandomInstance<Byte>()::class)
    }

    @Test
    fun `Creates ByteArray and IntArray primitives`() {
        assertEquals(ByteArray::class, makeRandomInstance<ByteArray>()::class)
        assertEquals(intArrayOf(10, 20, 30, 40, 50)::class, makeRandomInstance<IntArray>()::class)
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
        assertTrue(map is Map<Long, String>)
        assertTrue(map.toString().startsWith("{"))
        assertTrue(map.toString().endsWith("}"))

        assertEquals(linkedMapOf<String, B>()::class, makeRandomInstance<LinkedHashMap<String, B>>()::class)
        assertEquals(hashMapOf<A, B>()::class, makeRandomInstance<HashMap<A, B>>()::class)
    }

    @Test
    fun `Creates an instance using constructor with collections, primitives and standard types`() {
        val i: I = makeRandomInstance()
        assertTrue(i is I)
        assertTrue(i.ints is List<Int>)
        assertTrue(i.ints.firstOrNull() is Int?)

        val j: J = makeRandomInstance()
        assertTrue(j is J)
        assertTrue(j.map.all { (k, v) -> k is Long && v is String })
    }


    @Test
    fun `Creates sets`() {
        assertTrue(makeRandomInstance<Set<F>>() is Set<F>)
    }

    @Test
    fun `Creates collections`() {
        val g = makeRandomInstance<G>()
        assertEquals(G::class, g::class)
        assertTrue(makeRandomInstance<Collection<A>>() is Collection<A>)
    }

    @Test
    fun `Creates primitives for Arrays`() {
        assertEquals(arrayOf("this", "is", "a", "string", "array")::class, Array<String>::class)
        assertEquals(arrayOf(1)::class, makeRandomInstance<Array<Int>>()::class)
        assertEquals(arrayOf("string")::class, makeRandomInstance<Array<String>>()::class)
        assertEquals(arrayOf(arrayOf(1L))::class, Array<Array<Long>>::class)
    }

    // TODO cast Array<Any> / Object[] to Array<T>
    @Test
    fun `Creates primitives for other Arrays`() {
        assertThrows<ClassCastException> { makeRandomInstance<Array<A>>() }
        //assertEquals(arrayOf(A())::class, makeRandomInstance<Array<A>>()::class)
        //assertEquals(arrayOf(arrayOf(A()))::class, Array<Array<A>>::class)
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
