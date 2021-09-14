package com.github.sylhare

import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

val random: Random.Default = Random

@ExperimentalStdlibApi
internal fun makeStandardInstanceOrNull(clazz: KClass<*>, type: KType): Any? = when (clazz) {
    Any::class -> "Anything"
    Int::class -> random.nextInt()
    Long::class -> random.nextLong()
    Double::class -> random.nextDouble()
    Float::class -> random.nextFloat()
    Char::class -> makeRandomChar()
    String::class -> makeRandomString()
    Boolean::class -> random.nextBoolean()
    List::class, Collection::class -> makeRandomList(clazz, type)
    Set::class -> makeRandomList(clazz, type).toSet()
    Map::class -> makeRandomMap(clazz, type)
    else -> null
}

@ExperimentalStdlibApi
private fun makeRandomList(clazz: KClass<*>, type: KType): List<Any?> {
    val numOfElements = random.nextInt(10)
    val elemType = type.arguments[0].type!!
    return (1..numOfElements)
        .map { makeRandomInstanceForParam(elemType, clazz, elemType) }
}

@ExperimentalStdlibApi
private fun makeRandomMap(clazz: KClass<*>, type: KType): Map<Any?, Any?> {
    val numOfElements = random.nextInt(2, 10)
    val keyType = type.arguments[0].type!!
    val valType = type.arguments[1].type!!
    val keys = (1..numOfElements)
        .map { makeRandomInstanceForParam(keyType, clazz, type) }
    val values = (1..numOfElements)
        .map { makeRandomInstanceForParam(valType, clazz, type) }
    return keys.zip(values).toMap()
}

private fun makeRandomChar() = ('A'..'z').random(random)
private fun makeRandomString() = (1..random.nextInt(50))
    .map { makeRandomChar() }
    .joinToString(separator = "")
