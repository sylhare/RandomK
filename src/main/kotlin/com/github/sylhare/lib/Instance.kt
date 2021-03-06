package com.github.sylhare.lib

import com.github.sylhare.RandomK
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType

internal inline fun <reified T> makeRandomInstanceNoArgs(): T {
    return T::class.constructors.first { it.parameters.isEmpty() }.call()
}

internal inline fun <reified T> makeRandomInstanceJVM(): T {
    return T::class.java.getDeclaredConstructor().newInstance()
}

internal fun makeRandomInstance(type: KType): Any? {
    return RandomBuilder(Random, RandomK.Config()).build(type.classifier as KClass<*>, type)
}

