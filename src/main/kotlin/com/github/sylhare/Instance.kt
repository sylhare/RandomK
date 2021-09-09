package com.github.sylhare

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
inline fun <reified T: Any> makeRandomInstance(): T {
    return makeRandomInstance(T::class, typeOf<T>()) as T
}

fun makeRandomInstance(type: KType): Any? {
    return makeRandomInstance(type.classifier as KClass<*>, type)
}

fun makeRandomInstance(clazz: KClass<*>, type: KType): Any? {
    if(type.isMarkedNullable && random.nextBoolean()) {
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
                .map { makeRandomInstance(it.type) }
                .toTypedArray()

            return constructor.call(*arguments)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    throw NoUsableConstructor(constructors.toString())
}
