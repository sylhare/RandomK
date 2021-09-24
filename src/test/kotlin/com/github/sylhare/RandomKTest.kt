package com.github.sylhare

import com.github.sylhare.lib.RandomKNotSupportedType
import com.github.sylhare.lib.makeRandomInstance
import com.github.sylhare.mock.MockClasses
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

@ExperimentalStdlibApi
internal class RandomKTest {
    @Test
    fun `Creates mock with randomK`() {
        val h: MockClasses.H = randomK()
        assertEquals(h::class.java, MockClasses.H::class.java)
        assertTrue(h.s.isNotEmpty())
        assertTrue(h.l.isNotEmpty())
    }

    @Test
    fun `Creates mock with config random seed in randomK`() {
        val h: MockClasses.H = randomK(Random(123))
        assertEquals(h::class.java, MockClasses.H::class.java)
        assertEquals(h.i, randomK<MockClasses.H>(Random(123)).i)
        assertEquals(h.s, randomK<MockClasses.H>(Random(123)).s)
        assertEquals(h.l.size, randomK<MockClasses.H>(Random(123)).l.size)
    }

    @Test
    fun `Creates mock with config in randomK`() {
        val any: Any = randomK(Random(123), RandomK.Config(1..10, 1..20, MockClasses.B(MockClasses.A())))
        assertEquals(any::class.java, MockClasses.B::class.java)
    }

    @Test
    fun `When user expects empty collections, both Map and List are empty`() {
        val config = RandomK.Config(collectionRange = 0..0)
        repeat(10) {
            assertEquals(emptyList<Int>(), randomK<List<Int>>(config = config))
            assertEquals(emptyList<Int>(), randomK<List<List<Int>>>(config = config))
            assertEquals(emptyMap<Int, String>(), randomK<Map<Int, String>>(config = config))
        }
    }

    @Test
    fun `When user expects concrete collection size, both Map and List are of this size`() {
        val config = RandomK.Config(collectionRange = 5..5)
        repeat(10) {
            assertEquals(5, randomK<List<Int>>(config = config).size)
            assertEquals(5, randomK<List<List<Int>>>(config = config).size)
            assertEquals(5, randomK<Map<Int, String>>(config = config).size)
        }
    }

    @Test
    fun `When user expects concrete String length, all Strings have this length`() {
        val config = RandomK.Config(stringRange = 5..5, collectionRange = 2..2)
        repeat(10) {
            assertEquals(5, randomK<String>(config = config).length)
            assertEquals(5, randomK<List<String>>(config = config)[0].length)
            assertEquals(5, randomK<List<List<String>>>(config = config)[1][1].length)
        }
    }

    @Test
    fun `Object set in config as Any, is always returned when we expect Any`() {
        val any = object {}
        val config = RandomK.Config(any = any)
        repeat(10) {
            assertEquals(any, randomK(config = config))
            assertEquals(any, randomK<MockClasses.GA<Any>>(config = config).t)
            assertEquals(any, randomK<MockClasses.GA<MockClasses.GA<Any>>>(config = config).t.t)
        }
    }

    @Test
    fun `Check expected random values`() {
        val random = Random(12345)
        assertEquals("A", randomK<MockClasses.A>(random)::class.java.simpleName.toString())
        assertEquals("B(a=A)", randomK<MockClasses.B>(random).toString())
        assertEquals("C(a=A, b=B(a=A))", randomK<MockClasses.C>(random).toString())
        assertEquals("D(a=A, b=null, c=C(a=A, b=B(a=A)))", randomK<MockClasses.D>(random).toString())
        assertEquals("E", randomK<MockClasses.E>(random).toString())
        assertEquals("F(hello=SkcjX`jn[vxAkBvoWGUkC\\\\kp]U`N)", randomK<MockClasses.F>(random).toString())
        assertEquals(
            "G(f1=F(hello=`tCacMffjYGtd), f2=F(hello=nA[trp_JLM]iD^yBz`AGmwIMmdtGFCTcKrSYMlUYH), c=b, str=cUJxl^\\dBAlOpa^clPXl]oozUVBMFC], l=7449062515800925810, m={X[Em=C(a=A, b=B(a=A))})",
            randomK<MockClasses.G>(random).toString()
        )
    }

    @Test
    fun `Not implemented test`() {
        assertThrows<RandomKNotSupportedType> { randomK<Array<MockClasses.A>>() }
    }
}
