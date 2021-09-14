package com.github.sylhare

import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.typeOf


@ExperimentalStdlibApi
class RandomProducer(
    private val random: Random,
    private val config: RandomK.Config
) {


    private fun makeRandomInstanceForParam(paramType: KType, clazz: KClass<*>, type: KType): Any? {
        return when (val classifier = paramType.classifier) {
            is KClass<*> -> makeRandomInstance(classifier, paramType)
            is KTypeParameter -> {
                val typeParameterName = classifier.name
                val typeParameterId = clazz.typeParameters.indexOfFirst { it.name == typeParameterName }
                val parameterType = type.arguments[typeParameterId].type ?: typeOf<Any>()
                makeRandomInstance(parameterType.classifier as KClass<*>, parameterType)
            }
            else -> throw Error("Type of the classifier $classifier is not supported")
        }
    }


    fun makeRandomInstance(clazz: KClass<*>, type: KType): Any? {
        if (type.isMarkedNullable && random.nextBoolean()) {
            return null
        }

        val primitive = makeStandardInstanceOrNull(clazz, type)
        if (primitive != null) {
            return primitive
        }

        val constructors = clazz.constructors.shuffled(random)
        constructors.forEach { constructor ->
            try {
                val arguments = constructor.parameters
                    .map { makeRandomInstanceForParam(it.type, clazz, type) }
                    .toTypedArray()

                return constructor.call(*arguments)
            } catch (e: Throwable) {
                println("no-op. We catch any possible error here that might occur during class creation")
                e.printStackTrace()
            }
        }

        throw NoUsableConstructor("For class: $clazz, with KType: $type, and constructor(s): $constructors")
    }

    private fun makeStandardInstanceOrNull(clazz: KClass<*>, type: KType) = when (clazz) {
        Any::class -> config.any
        Int::class -> random.nextInt()
        Long::class -> random.nextLong()
        Double::class -> random.nextDouble()
        Float::class -> random.nextFloat()
        Char::class -> makeRandomChar(random)
        String::class -> makeRandomString(random)
        Boolean::class -> random.nextBoolean()
        List::class, Collection::class -> makeRandomList(clazz, type)
        Set::class -> makeRandomList(clazz, type).toSet()
        Map::class -> makeRandomMap(clazz, type)
        else -> null
    }

    private fun makeRandomList(clazz: KClass<*>, type: KType): List<Any?> {
        val numOfElements = random.nextInt(config.collectionRange.first, config.collectionRange.last + 1)
        val elemType = type.arguments[0].type!!
        return (1..numOfElements)
            .map { makeRandomInstanceForParam(elemType, clazz, type) }
    }

    private fun makeRandomMap(clazz: KClass<*>, type: KType): Map<Any?, Any?> {
        val numOfElements = random.nextInt(config.collectionRange.first, config.collectionRange.last + 1)
        val keyType = type.arguments[0].type!!
        val valType = type.arguments[1].type!!
        val keys = (1..numOfElements)
            .map { makeRandomInstanceForParam(keyType, clazz, type) }
        val values = (1..numOfElements)
            .map { makeRandomInstanceForParam(valType, clazz, type) }
        return keys.zip(values).toMap()
    }

    private fun makeRandomChar(random: Random) = ('A'..'z').random(random)
    private fun makeRandomString(random: Random) = (1..random.nextInt(config.stringRange.first, config.stringRange.last + 1))
        .map { makeRandomChar(random) }
        .joinToString(separator = "") { "$it" }
}
