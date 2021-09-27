package com.github.sylhare.mock

import com.github.sylhare.mock.MockClasses.A
import com.github.sylhare.mock.MockClasses.D
import com.github.sylhare.mock.MockClasses.K
import getKType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
class ReflectionTest {

    @Test
    fun `Array are reified`() {
        assertEquals(Array<Int>::class, Array(3) { i -> i * 1 }::class)
        assertNotEquals(Array::class, arrayOf(1, 2, 6)::class)
        assertEquals(Array<Int>::class, arrayOf(1, 2, 6)::class)
        assertEquals(IntArray::class, intArrayOf(1, 2, 6)::class)
        assertEquals(ArrayList::class, arrayListOf(1, 2, 6)::class)
    }

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
        assertEquals(D::class.qualifiedName, D::class.createType().toString())
        assertEquals(D::class, typeOf<D>().classifier)
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

    @Test
    fun `Arrays and memberProperties`() {
        for (memberProperty in K::class.memberProperties) {
            val propertyType = memberProperty.returnType.javaType as Class<*>
            if (propertyType.isArray) {
                val array = java.lang.reflect.Array.newInstance(propertyType.componentType, 10) as Array<*>
                assertEquals(K(arrayOf(1)).a::class, array::class)
            }
        }
    }
}
