package com.github.sylhare.random

import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

val random: Random.Default = Random
internal fun makeStandardInstanceOrNull(clazz: KClass<*>, type: KType): Any? = when(clazz) {
    Any::class -> "Anything"
    Int::class -> random.nextInt()
    Long::class -> random.nextLong()
    Double::class -> random.nextDouble()
    Float::class -> random.nextFloat()
    Char::class -> makeRandomChar()
    String::class -> makeRandomString()
    Boolean::class -> random.nextBoolean()
    List::class, Collection::class -> makeRandomList(type)
    Map::class -> makeRandomMap(type)
    else -> null
}

private fun makeRandomList(type: KType): List<Any?> {
    val numOfElements = random.nextInt(10)
    val elemType = type.arguments[0].type!!
    return (1..numOfElements)
        .map { makeRandomInstance(elemType) }
}

private fun makeRandomMap(type: KType): Map<Any?, Any?> {
    val numOfElements = random.nextInt(10)
    val keyType = type.arguments[0].type!!
    val valType = type.arguments[1].type!!
    val keys = (1..numOfElements)
        .map { makeRandomInstance(keyType) }
    val values = (1..numOfElements)
        .map { makeRandomInstance(valType) }
    return keys.zip(values).toMap()
}

private fun makeRandomChar() = ('A'..'z').random(random)
private fun makeRandomString() = (1..random.nextInt(50))
    .map { makeRandomChar() }
    .joinToString(separator = "")
