package com.github.sylhare

import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
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
        else -> primitiveOrNull(clazz, type) ?: customOrNull(clazz, type)
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
        ByteArray::class -> buildString().toByteArray()
        Int::class -> random.nextInt()
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
