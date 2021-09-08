package com.github.sylhare.random

import kotlin.reflect.KClass

inline fun <reified T> makeRandomInstanceNoArgs(): T {
    return T::class.constructors.first { it.parameters.isEmpty() }.call()
}

inline fun <reified T> makeRandomInstanceJVM(): T {
    return T::class.java.getDeclaredConstructor().newInstance()
}

inline fun <reified T> makeRandomInstance(): T {
    return makeRandomInstance(T::class) as T
}

fun makeRandomInstance(clazz: KClass<*>): Any {
    val primitive = makePrimitiveOrNull(clazz)
    if (primitive != null) {
        return primitive
    }

    val constructors = clazz.constructors
        .sortedBy { it.parameters.size }

    constructors.forEach { constructor ->
        try {
            val arguments = constructor.parameters
                .map { it.type.classifier as KClass<*> }
                .map { makeRandomInstance(it) }
                .toTypedArray()

            return constructor.call(*arguments)
        } catch (e: Throwable) {
            // no-op. We catch any possible error here that might occur during class creation
        }
    }

    throw NoUsableConstructor()
}
