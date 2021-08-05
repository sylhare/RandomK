import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.IllegalCallableAccessException

class RandomKTest {

    class A
    class B(val a: A)
    class C(val a: A, val b: B)
    class D {
        constructor() {
            throw Error("Do not use this one")
        }

        constructor(a: A)
    }
    class P {
        private constructor()
    }

    @Test
    fun `Creates single instance using an empty constructor JVM only`() {
        val a: A = makeRandomInstanceJVM()
        assertTrue(a is A)
        assertTrue("A@" in a.toString(), "toString of A should contains \$A@ and it is $a")
    }

    @Test
    fun `Creates single instance using an empty constructor`() {
        val a: A = makeRandomInstanceNoArgs()
        assertTrue(a is A)
        assertTrue("A@" in a.toString(), "toString of A should contains \$A@ and it is $a")
    }

    @Test
    fun `Creates a single instance using empty constructor`() {
        val a: A = makeRandomInstance()
        assertTrue(a is A)
        assertTrue("A@" in a.toString(), "toString of A should contains \$A@ and it is $a")
    }


    @Test
    fun `Creates a single instance using constructor with 1 variable`() {
        val b: B = makeRandomInstance()
        assertTrue(b is B)
        assertTrue("\$B@" in b.toString(), "toString of A should contains \$B@ and it is $b")
    }

    @Test
    fun `Creates a single instance using constructor with 2 variables`() {
        val c: C = makeRandomInstance()
        assertTrue(c is C)
        assertTrue("\$C@" in c.toString(), "toString of A should contains \$C@ and it is $c")
    }

    @Test
    fun `Skips constructors that cannot be used`() {
        val d: D = makeRandomInstance()
        assertTrue(d is D)
        assertTrue("\$D@" in d.toString(), "toString of A should contains \$D@ and it is $d")
    }

    @Test
    fun `Throws error if there is no constructor that could be used`() {
        assertThrows<IllegalCallableAccessException> { makeRandomInstance<P>() }
    }
}
