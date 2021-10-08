package com.github.sylhare.mock

import com.github.sylhare.lib.makeRandomInstance
import com.github.sylhare.mock.MockClasses.A
import com.github.sylhare.mock.MockClasses.D
import com.github.sylhare.mock.MockClasses.K
import com.github.sylhare.mock.MockClasses.O
import getKType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
@Suppress("USELESS_IS_CHECK")
class ReflectionTest {

    @Nested
    inner class ReflectionBase {

        @Test
        fun `KClass are Kotlin reflection classes`() {
            val a: KClass<A> = A::class
            assertEquals(a, A::class)
            assertEquals(a, A()::class)
        }

        @Test
        fun `Class are Java reflection classes`() {
            val a: Class<A> = A::class.java
            assertEquals(a, A::class.java)
            assertEquals(a, A()::class.java)
            assertNotEquals(A::class, A::class.java)
        }

        @Test
        fun `KType are kotlin types`() {
            val dType: KType = typeOf<D>()
            assertNotEquals(KType::class, dType::class)
            assertEquals(D::class.qualifiedName, dType.toString())
            println(D::class.qualifiedName)
            assertEquals(D::class.qualifiedName, D::class.createType().toString())
            assertEquals(D::class, typeOf<D>().classifier)
            println(D::class)
        }

        @Test
        fun `getKType test`() {
            assertEquals(typeOf<List<Map<String, Array<Double>>>>(), getKType<List<Map<String, Array<Double>>>>())
            assertEquals(typeOf<Array<Array<String>>>(), getKType<Array<Array<String>>>())
            assertEquals(typeOf<Unit>(), getKType<Unit>())

            assertNotEquals(typeOf<Array<*>>(), getKType<Array<*>>()) // typeOf transform "*" into "Any"
            assertNotEquals(typeOf<List<*>>(), getKType<List<*>>())
        }

        @Test
        fun `Type are java types`() {
            assertNotEquals(typeOf<D>().toString(), typeOf<D>().javaType.toString())
            assertNotEquals("void", typeOf<Unit>().javaType.toString())
            assertNotEquals("D", typeOf<D>().javaType.toString())
        }

    }

    @Nested
    inner class ArrayReflection {
        @Test
        fun `Array are reified`() {
            assertEquals(Array<Int>::class, Array(3) { i -> i * 1 }::class)
            assertNotEquals(Array::class, arrayOf(1, 2, 6)::class)
            assertEquals(Array<Int>::class, arrayOf(1, 2, 6)::class)
            assertEquals(IntArray::class, intArrayOf(1, 2, 6)::class)
            assertEquals(ArrayList::class, arrayListOf(1, 2, 6)::class)
        }


        @Test
        fun `KType and KClass with arrays`() {
            val dType: KType = typeOf<D>()
            assertNotEquals(KType::class, dType::class)
            assertEquals(D::class.qualifiedName, dType.toString())
            println(D::class.qualifiedName)
            assertEquals(D::class.qualifiedName, D::class.createType().toString())
            assertEquals(D::class, typeOf<D>().classifier)
            println(D::class)
        }

        @Test
        fun `Reflect Arrays`() {
            println("${Array<Int>::class.typeParameters[0]::class}")
            println("${typeOf<Array<O>>()}")
            println("${O::class.createType()}")
            org.junit.jupiter.api.assertThrows<IllegalArgumentException> { Array<O>::class.createType() }
            println("${typeOf<Array<Int>>().classifier}")
            println("${typeOf<Array<Int>>().classifier} is IntArray and not Array<kotlin.Int>")
            println("${typeOf<Array<Int>>().arguments[0].type!!.javaType as Class<*>}")
            println("${typeOf<Array<Int>>().arguments[0].type!!} is Kotlin.Int")
        }

        @Test
        fun testWithInstances() {
            var a = makeRandomInstance(typeOf<Array<Char>>())!!
            print("$a  ${a::class}")
            println("${a as CharArray}")
            a = makeRandomInstance(typeOf<CharArray>())!!
            print("$a  ${a::class}")
            println("${a as CharArray}")
        }

        @Test
        fun `Arrays and memberProperties`() {
            var array: Array<*>? = null
            for (memberProperty in K::class.memberProperties) {
                println("memberProperty: $memberProperty")
                val propertyType = memberProperty.returnType.javaType as Class<*>
                println("propertyType: $propertyType")
                if (propertyType.isArray) {
                    println("component: ${propertyType.componentType}")
                    array = java.lang.reflect.Array.newInstance(propertyType.componentType, 10) as Array<*>
                    assertEquals(K("1", arrayOf(1)).a::class, array::class)
                }
            }
            assertNotNull(array)
        }


        @Test
        fun testReflectArray() {
            org.junit.jupiter.api.assertThrows<ClassCastException> { reflectArray<Array<Int>>() }
            org.junit.jupiter.api.assertThrows<ClassCastException> { reflectArray<Array<String>>() }
            assertTrue(reflectArray<Array<Any>>() is Array<*>)
            assertTrue(reflectArray<Array<Any>>() is Array<Any>)
        }

        @Test
        fun testWithArrayReflection() {
            arrayReflectionCast<Array<String>>()
            arrayReflectionCast<Array<O>>()
        }

        private inline fun <reified T> reflectArray(): T {
            if (T::class.java.isArray) {
                val array = arrayOf(
                    when ((typeOf<T>().classifier as KClass<*>)) {
                        Int::class -> 1
                        String::class -> "1" // Compiler does not know if Int or String so impossible cast
                        else -> null
                    }
                )
                println("$array of class ${array::class}")
                return array as T
            } else {
                throw UnsupportedOperationException("${T::class} is not an array")
            }
        }

        private inline fun <reified T> arrayReflectionCast(): T? {
            return java.lang.reflect.Array.newInstance(typeOf<T>().arguments[0].type!!.javaType as Class<*>, 10) as T
        }

    }
}
