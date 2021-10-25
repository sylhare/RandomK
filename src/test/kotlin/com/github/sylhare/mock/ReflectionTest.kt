package com.github.sylhare.mock

import com.github.sylhare.lib.makeRandomInstance
import com.github.sylhare.mock.MockClasses.A
import com.github.sylhare.mock.MockClasses.D
import com.github.sylhare.mock.MockClasses.K
import com.github.sylhare.mock.MockClasses.O
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import kotlin.reflect.jvm.javaType

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
            assertThrows<IllegalArgumentException> { Array<O>::class.createType() }
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
            assertThrows<ClassCastException> { reflectArray<Array<Int>>() }
            assertThrows<ClassCastException> { reflectArray<Array<String>>() }
            assertTrue(reflectArray<Array<Any>>() is Array<*>)
            assertTrue(reflectArray<Array<Any>>() is Array<Any>)
        }

        @Test
        fun testWithArrayReflection() {
            assertTrue(genericReflectionToTypedArray<Array<String>>() is Array<String>)
            assertTrue(genericReflectionToTypedArray<Array<O>>() is Array<O>)
        }

        @Test
        fun testWithOtherArrayReflection() {
            assertTrue(createArrayOfGeneric<String>() is Array<String>)
            assertTrue(instantiateArrayOfGeneric<String>() is Array<String>)
            assertThrows<Error> { instantiateArrayOfGeneric<Array<D>>() }
        }

        @Test
        fun makeYourOwnArray() {
            var array: Array<Int> = Array(3) { i: Int -> i }
            assertThrows<Error> { Array<Int>::class.constructors.first().call(3, { i: Int -> i }) }
            assertEquals(arrayOf(0, 1, 2).contentToString(), array.contentToString())
        }

        private inline fun <reified T> reflectArrayOf(): T? {
            if (T::class.java.isArray) {
                val arrayType: KType = typeOf<T>().arguments[0].type!!
                println(arrayType)
                return arrayOf((arrayType.classifier as KClass<*>).constructors.first().call()) as T
            }
            return null
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

        private inline fun <reified T> genericReflectionToTypedArray(): T? {
            return java.lang.reflect.Array.newInstance(typeOf<T>().arguments[0].type!!.javaType as Class<*>, 10) as T
        }

        @Suppress("UNCHECKED_CAST")
        private inline fun <reified T> createArrayOfGeneric(): Array<T> {
            return java.lang.reflect.Array.newInstance(typeOf<T>().javaType as Class<*>, 10) as Array<T>
        }

        private inline fun <reified T> instantiateArrayOfGeneric(): Array<T> {
            return Array(10) { T::class.constructors.first().call() }
        }
    }
}
