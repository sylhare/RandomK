package com.github.sylhare.demo

import org.junit.jupiter.api.*
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
@TestMethodOrder(value = MethodOrderer.MethodName::class)
class ShowcaseTest {

    @BeforeEach
    fun setup() {
        println()
    }

    @Test
    fun `1 String - KType and Type`() {
        val stringType: KType = typeOf<String>()
        Assertions.assertEquals(String::class.qualifiedName, stringType.toString())
        Assertions.assertEquals(String::class.qualifiedName, String::class.createType().toString())
        println("Kotlin Type:\t $stringType")
        println("Java Type:\t\t ${String::class.java.typeName}")
        println("Java Type:\t\t ${"String"::class.java.typeName}")
    }

    @Test
    fun `2 String - KClass, Class and KClassifier`() {
        Assertions.assertEquals(String::class, typeOf<String>().classifier)
        println("Kotlin Class:\t ${String::class}")
        println("KClassifier:\t ${typeOf<String>().classifier}")
        println("Java Class:\t\t ${String::class.java}")
    }

    /**
     * Due to generic type erasure List class has a single implementation for all its generic instantiations.
     * Can't List<String>::class, type is not reified for list:
     *
     *      public interface List<out E> : Collection<E>
     */
    @Test
    fun `3 List of String - KType and Type`() {
        val stringType: KType = typeOf<List<String>>()
        val stringList: List<String> = mutableListOf("A", "B") // Kotlin list uses ArrayList internally
        Assertions.assertNotEquals(List::class.qualifiedName, stringType.toString())
        Assertions.assertNotEquals(
            stringList::class.qualifiedName,
            stringType.toString()
        ) // The qualified name is not a Kotlin class here
        println("Kotlin Type:\t $stringType")
        println("Java Type:\t\t ${stringList::class.java.typeName}") // A generated java class (with the $)
    }

    @Test
    fun `4 List of String - KClass, Class and KClassifier`() {
        val stringList: List<String> = listOf("A", "B")
        Assertions.assertEquals(List::class, typeOf<List<String>>().classifier)
        println("Kotlin Class:\t ${stringList::class}")
        println("KClassifier:\t ${typeOf<List<String>>().classifier}")
        println("Java Class:\t\t ${stringList::class.java}")
    }

    /**
     * EmptyList is an internal object of the Kotlin Collection implementation,
     * EmptyList type does not exist in java.
     *
     *  public inline fun <T> listOf(): List<T> = emptyList()
     *
     * EmptyList in CollectionsKt.class:
     *   internal object EmptyList : List<Nothing>, Serializable, RandomAccess { .. }
     */
    @Test
    fun `5 EmptyList - Type and Class`() {
        val emptyList: List<String> = listOf()
        val emptyListClass: KClass<*> = emptyList::class
        val emptyListType: KType = emptyListClass.createType()
        Assertions.assertNotEquals(emptyList::class.qualifiedName, emptyListType)
        println("Kotlin Class:\t $emptyListClass")
        println("Kotlin Type:\t $emptyListType")
        println("Java Class:\t\t ${emptyListClass::class.java}") // EmptyList class does not exist in java
        println("Java Type:\t\t ${emptyListClass::class.java.typeName}") // EmptyList type does not exist in java, so it's map to a reflect class KClassImpl
        assertThrows<NoSuchMethodException> { emptyListClass::class.java.getDeclaredConstructor().newInstance() }
        assertThrows<IndexOutOfBoundsException> { emptyList[0] }
    }

    /**
     * Array keeps their generic type during instantiation:
     * public inline fun <reified @PureReifiable T> arrayOf(vararg elements: T): Array<T>
     *
     */
    @Test
    fun `6 Array - Type and Class`() {
        val array: Array<String> = arrayOf()
        val emptyArray: Array<String> = emptyArray()
        Assertions.assertEquals(array::class, emptyArray::class) // There a no EmptyArray internal type
        Assertions.assertNotEquals(
            array::class.qualifiedName,
            typeOf<Array<String>>().toString()
        ) // Qualified name gets cut to kotlin.Array
        println("Kotlin Class:\t ${Array<String>::class}") // Type in array is reified
        println("Kotlin Type:\t ${typeOf<Array<String>>()}")
        println("Java Class:\t\t ${Array<String>::class.java}")
        println("Java Type:\t\t ${Array<String>::class.java.typeName}")
    }

    @Test
    fun `7 KClassifier`() {
        val k: KClass<*> = String::class
        val j: Class<*> = String::class.java
        val kt: KType = String::class.createType()
        val jt: Type = String::class.createType().javaType
        val ks: KClassifier? = typeOf<String>().classifier

        println("KClassifier:\t ${typeOf<List<String>>().classifier} for ${listOf("A", "B")::class}")
        println("KClassifier:\t ${typeOf<Array<String>>().classifier} for ${arrayOf("A", "B")::class}")
    }

    private fun <T> nullableType(): List<T?> {
        return listOf()
    }

    @Suppress("UNCHECKED_CAST")
    fun <K : Any, V : Any> exampleType(key: K): V? {
        return "" as V?
    }
}
