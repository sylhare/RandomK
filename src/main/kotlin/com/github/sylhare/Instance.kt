package com.github.sylhare

import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

inline fun <reified T> makeRandomInstanceNoArgs(): T {
    return T::class.constructors.first { it.parameters.isEmpty() }.call()
}

inline fun <reified T> makeRandomInstanceJVM(): T {
    return T::class.java.getDeclaredConstructor().newInstance()
}

@ExperimentalStdlibApi
fun makeRandomInstance(type: KType): Any? {
    return RandomBuilder(Random, RandomK.Config()).build(type.classifier as KClass<*>, type)
}

@ExperimentalStdlibApi
inline fun <reified T : Any> makeRandomInstance(): T {
    return RandomBuilder(Random, RandomK.Config()).build(T::class, typeOf<T>()) as T
}

