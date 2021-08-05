import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.IllegalCallableAccessException

class RandomKTest {

    class A
    class B(val a: A)
    class C(val a: A, val b: B)
    data class D(val a: A, val b: B, val c: C)
    class E {
        constructor() {
            throw Error("Do not use this one")
        }

        constructor(a: A)
    }
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
    fun `Throws NoUsableConstructor for private constructor`() {
        assertThrows<NoUsableConstructor> { makeRandomInstance<P>() }
    }

    @Test
    fun `Throws NoUsableConstructor for a sealed class`() {
        assertThrows<NoUsableConstructor> { makeRandomInstance<S>() }
    }
}
