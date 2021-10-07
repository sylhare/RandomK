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
import com.github.sylhare.randomK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExperimentalStdlibApi
@Suppress("USELESS_IS_CHECK")
class RandomKTest {

    @Nested
    inner class ConstructorTests {
        @Test
        fun `Creates a single instance using empty constructor`() {
            val a: A = randomK()
            assertEquals(a::class.java, A::class.java)
        }

        @Test
        fun `Creates a single instance using constructor with 1 variable`() {
            val b: B = randomK()
            assertEquals(b::class.java, B::class.java)
        }

        @Test
        fun `Creates a single instance using constructor with 2 variables`() {
            val c: C = randomK()
            assertEquals(c::class.java, C::class.java)
        }

        @Test
        fun `Creates a single instance using constructor from data class`() {
            val d: D = randomK()
            assertEquals(d::class.java, D::class.java)
        }

        @Test
        fun `Skips constructors that cannot be used`() {
            val e: E = randomK()
            assertEquals(e::class.java, E::class.java)
        }

        @Test
        fun `With multiple constructors, takes a random one`() {
            val m: M = randomK()
            assertEquals(m::class.java, M::class.java)
            assertTrue((1..10).map { randomK<M>().number }.toSet().size > 1)
        }

        @Test
        fun `With lateinit and lazy variables`() {
            val l: L = randomK()
            assertNotEquals(l.b, L("tada").b)
            assertNotEquals(l.b.value, "hello")
            assertEquals(l::class.java, L::class.java)
        }

        @Test
        fun `Creates an instance using constructor with collections, primitives and standard types`() {
            val i: I = randomK()
            assertTrue(i is I)
            assertTrue(i.ints is List<Int>)
            assertTrue(i.ints.firstOrNull() is Int?)

            val j: J = randomK()
            assertTrue(j is J)
            assertTrue(j.map.all { (k, v) -> k is Long && v is String })
        }

        @Test
        fun `Throws NoUsableConstructor for private constructor`() {
            assertThrows<NoUsableConstructor> { randomK<P>() }
        }

        @Test
        fun `Throws NoUsableConstructor for a sealed class`() {
            assertThrows<NoUsableConstructor> { randomK<S>() }
        }
    }

    @Nested
    inner class PrimitiveTypes {
        @Test
        fun `Creates boolean primitives`() {
            assertEquals(Boolean::class, randomK<Boolean>()::class)
        }

        @Test
        fun `Creates number primitives`() {
            assertEquals(Int::class, randomK<Int>()::class)
            assertEquals(Short::class, randomK<Short>()::class)
            assertEquals(Long::class, randomK<Long>()::class)
            assertEquals(Double::class, randomK<Double>()::class)
            assertEquals(Float::class, randomK<Float>()::class)
        }

        @Test
        fun `Creates Byte primitives`() {
            assertEquals(Byte::class, randomK<Byte>()::class)
        }

        @Test
        fun `Creates character primitives`() {
            val c = randomK<Char>()
            assertEquals(Char::class, c::class)
            assertTrue(c.code in 23..123)
            assertEquals(String::class, randomK<String>()::class)
        }

        @Test
        fun `Creates an instance using constructor with primitives and standard types`() {
            val f: F = randomK()
            assertEquals(F::class, f::class)

            val g: G = randomK()
            assertEquals(G::class, g::class)
        }
    }

    @Nested
    inner class CollectionTypes {
        @Test
        fun `Creates lists`() {
            val ints: List<Int> = randomK()
            assertEquals("class java.util.ArrayList", ints::class.toString())
            assertTrue(ints.toString().startsWith("["))
            assertTrue(ints.toString().endsWith("]"))
        }

        @Test
        fun `Creates maps`() {
            val map = randomK<Map<Long, String>>()
            assertTrue(map is Map<Long, String>)
            assertTrue(map.toString().startsWith("{"))
            assertTrue(map.toString().endsWith("}"))

            assertEquals(linkedMapOf<String, B>()::class, randomK<LinkedHashMap<String, B>>()::class)
            assertEquals(hashMapOf<A, B>()::class, randomK<HashMap<A, B>>()::class)
        }

        @Test
        fun `Creates sets`() {
            assertTrue(randomK<Set<F>>() is Set<F>)
            assertEquals(hashSetOf<A>()::class, randomK<HashSet<A>>()::class)
        }

        @Test
        fun `Creates arrayLists`() {
            assertTrue(randomK<ArrayList<F>>() is ArrayList<F>)
        }

        @Test
        fun `Creates collections`() {
            val g = randomK<G>()
            assertEquals(G::class, g::class)
            assertTrue(randomK<Collection<A>>() is Collection<A>)
        }
    }

    @Nested
    inner class Arrays {
        @Test
        fun `Creates primitives for Arrays`() {
            assertEquals(arrayOf(1)::class, randomK<Array<Int>>()::class)
            assertEquals(arrayOf("string")::class, randomK<Array<String>>()::class)
            assertTrue(randomK<Array<Short>>() is Array<Short>)
            assertTrue(randomK<Array<Float>>() is Array<Float>)
            assertTrue(randomK<Array<Long>>() is Array<Long>)
            assertTrue(randomK<Array<Double>>() is Array<Double>)
            assertTrue(randomK<Array<Byte>>() is Array<Byte>)
            assertTrue(randomK<Array<Char>>() is Array<Char>)
            assertTrue(randomK<Array<Boolean>>() is Array<Boolean>)
        }

        @Test
        fun `Creates primitives for more Arrays`() {
            assertEquals(BooleanArray(0)::class, randomK<BooleanArray>()::class)
            assertEquals(CharArray(0)::class, randomK<CharArray>()::class)
            assertEquals(ShortArray(0)::class, randomK<ShortArray>()::class)
            assertEquals(LongArray(0)::class, randomK<LongArray>()::class)
            assertEquals(FloatArray(0)::class, randomK<FloatArray>()::class)
            assertEquals(DoubleArray(0)::class, randomK<DoubleArray>()::class)
            assertEquals(ByteArray::class, randomK<ByteArray>()::class)
            assertEquals(intArrayOf(10, 20, 30, 40, 50)::class, randomK<IntArray>()::class)
        }

        @Test
        fun `Arrays of Any`() {
            assertTrue(randomK<Array<Any>>() is Array<Any>)
        }

        // TODO: cast T to Array<*> does not work
        // TODO: Array<Array<T>> not implemented
        @Test
        fun `Unsupported cast exception`() {
            assertThrows<RandomKNotSupportedType> { randomK<Array<A>>() }
            assertThrows<RandomKNotSupportedType> { randomK<Array<Array<Any>>>() }
        }
    }

    @Nested
    inner class GenericTypes {
        @Test
        fun `Generic classes are supported`() {
            val ga1: GA<Int> = randomK()
            assertEquals(ga1.t::class, Int::class)

            val ga2: GA<String> = randomK()
            assertEquals(ga2.t::class, String::class)
        }

        @Test
        fun `Generic classes are supported default constructor`() {
            val gt1 = randomK<GT<Int>>()
            gt1.t = 1
            assertEquals(1, gt1.t)

            val gt2 = randomK<GT<Long>>()
            gt2.t = 1L
            assertEquals(1L, gt2.t)
        }

        @Test
        fun `Generic classes recursive are supported`() {
            val gt1 = randomK<GT<Int>>()
            val gtRecursive = randomK<GT<GT<Int>>>()
            gtRecursive.t = gt1
            assertEquals(gt1, gtRecursive.t)

            val gaaga: GAA<Long, GA<GT<Int>>> = randomK()
            assertEquals(gaaga.t1::class, Long::class)
            assertTrue(gaaga.t2 is GA<GT<Int>>)

            val gggg: GA<GA<GA<Int>>> = randomK()
            gggg.t.t.t = 10
            assertEquals(10, gggg.t.t.t)
            gggg.t.t = GA(20)
            assertEquals(20, gggg.t.t.t)
        }

        @Test
        fun `Generic classes with multiple arguments are supported`() {
            val gaa1: GAA<Int, String> = randomK()
            assertEquals(gaa1.t1::class, Int::class)
            assertEquals(gaa1.t2::class, String::class)

            val gaa2: GAA<Long, List<Int>> = randomK()
            assertEquals(gaa2.t1::class, Long::class)
            assertTrue(gaa2.t2 is List<Int>)

            val gta: GTA<Long, String> = randomK()
            assertTrue(gta.t2.length > 1)
        }
    }
}
