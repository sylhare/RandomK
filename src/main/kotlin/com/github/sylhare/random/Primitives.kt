package com.github.sylhare.random

import kotlin.random.Random
import kotlin.reflect.KClass

private val random: Random.Default = Random
internal fun makePrimitiveOrNull(clazz: KClass<*>) = when(clazz) {
    Int::class -> random.nextInt()
    Long::class -> random.nextLong()
    Double::class -> random.nextDouble()
    Float::class -> random.nextFloat()
    Char::class -> makeRandomChar()
    String::class -> makeRandomString()
    Boolean::class -> random.nextBoolean()
    else -> null
}

private fun makeRandomChar() = random.nextInt(23, 123).toChar()
private fun makeRandomString() = (1..random.nextInt(50))
    .map { makeRandomChar() }
    .joinToString(separator = "")
