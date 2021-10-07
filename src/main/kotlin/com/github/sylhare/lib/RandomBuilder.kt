package com.github.sylhare.lib

import com.github.sylhare.RandomK
import kotlin.random.Random
import kotlin.reflect.*
import kotlin.reflect.full.createType


class RandomBuilder(private val random: Random, private val config: RandomK.Config) {

    /**
     * To build a random instance:
     *  - T?: randomly makes field null
     *  - T is a primitive type: use primitive constructor
     *  - T is not primitive: randomly select a custom constructor
     */
    fun build(clazz: KClass<*>, type: KType): Any? = when {
        type.isMarkedNullable && random.nextBoolean() -> null
        clazz.java.isArray -> buildArray(clazz)
        else -> primitiveOrNull(clazz, type) ?: customOrNull(clazz, type)
    }

    private fun buildArray(clazz: KClass<*>): Any {
        return when (clazz) {
            ByteArray::class -> buildString().toByteArray()
            BooleanArray::class -> BooleanArray(0)
            CharArray::class -> buildString().toCharArray()
            ShortArray::class -> ShortArray(0)
            IntArray::class -> intArrayOf(random.nextInt())
            LongArray::class -> LongArray(0)
            FloatArray::class -> FloatArray(0)
            DoubleArray::class -> DoubleArray(0)
            Array<Byte>::class -> arrayOf(random.nextInt().toByte())
            Array<Int>::class -> arrayOf(random.nextInt())
            Array<Short>::class -> arrayOf(random.nextInt().toShort())
            Array<Long>::class -> arrayOf(random.nextLong())
            Array<Double>::class -> arrayOf(random.nextDouble())
            Array<Float>::class -> arrayOf(random.nextFloat())
            Array<Char>::class -> arrayOf(buildChar())
            Array<String>::class -> arrayOf(buildString())
            Array<Boolean>::class -> arrayOf(random.nextBoolean())
            else -> arrayOf<Any>()
        }
    }

    private fun customOrNull(clazz: KClass<*>, type: KType): Any {
        clazz.constructors.shuffled(random).forEach { constructor ->
            tryOf {
                val arguments = constructor.parameters
                    .map { buildParameter(it.type, clazz, type) }
                    .toTypedArray()

                constructor.call(*arguments)
            }?.let { return it }
        }
        throw NoUsableConstructor("For class: $clazz, with KType: $type")
    }

    private fun primitiveOrNull(clazz: KClass<*>, type: KType): Any? = when (clazz) {
        Any::class -> config.any
        Byte::class -> random.nextInt().toByte()
        Int::class -> random.nextInt()
        Short::class -> random.nextInt().toShort()
        Long::class -> random.nextLong()
        Double::class -> random.nextDouble()
        Float::class -> random.nextFloat()
        Char::class -> buildChar()
        String::class -> buildString()
        Boolean::class -> random.nextBoolean()
        List::class, Collection::class -> buildList(clazz, type)
        Set::class -> buildList(clazz, type).toSet()
        Map::class -> buildMap(clazz, type)
        else -> null
    }

    private fun buildParameter(paramType: KType, clazz: KClass<*>, type: KType): Any? {
        return when (val classifier = paramType.classifier) {
            is KClass<*> -> build(classifier, paramType)
            is KTypeParameter -> {
                val typeParameterName = classifier.name
                val typeParameterId = clazz.typeParameters.indexOfFirst { it.name == typeParameterName }
                val parameterType = type.arguments[typeParameterId].type ?: Any::class.createType()
                build(parameterType.classifier as KClass<*>, parameterType)
            }
            else -> throw Error("Type of the classifier $classifier is not supported")
        }
    }

    private fun buildList(clazz: KClass<*>, type: KType): List<Any?> {
        val elemType = type.arguments[0].type!!
        return (1..config.numberOfElements(random))
            .map { buildParameter(elemType, clazz, type) }
    }

    private fun buildMap(clazz: KClass<*>, type: KType): Map<Any?, Any?> {
        val numOfElements = config.numberOfElements(random)
        val keyType = type.arguments[0].type!!
        val valType = type.arguments[1].type!!
        val keys = (1..numOfElements)
            .map { buildParameter(keyType, clazz, type) }
        val values = (1..numOfElements)
            .map { buildParameter(valType, clazz, type) }
        return keys.zip(values).toMap()
    }

    private fun buildChar() = ('A'..'z').random(random)
    private fun buildString() = (1..config.numberOfChars(random))
        .map { buildChar() }
        .joinToString(separator = "") { "$it" }
}
