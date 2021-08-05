import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.IllegalCallableAccessException

class RandomKTest {

    class A

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
    fun `Throws error if there is no constructor that could be used`() {
        assertThrows<IllegalCallableAccessException> { makeRandomInstance<P>() }
    }
}
