package com.github.sylhare

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
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

@ExperimentalStdlibApi
fun makeRandomInstance(type: KType): Any? {
    return makeRandomInstance(type.classifier as KClass<*>, type)
}

@ExperimentalStdlibApi
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
                .map { makeRandomInstanceForParam(it.type, clazz, type) }
                .toTypedArray()

            return constructor.call(*arguments)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    throw NoUsableConstructor(constructors.toString())
}

@ExperimentalStdlibApi
private fun makeRandomInstanceForParam(paramType: KType, clazz: KClass<*>, type: KType): Any? {
    return when (val classifier = paramType.classifier) {
        is KClass<*> -> makeRandomInstance(classifier, paramType)
        is KTypeParameter -> {
            val typeParameterName = classifier.name
            val typeParameterId = clazz.typeParameters.indexOfFirst { it.name == typeParameterName }
            val parameterType = type.arguments[typeParameterId].type ?: typeOf<String>()
            makeRandomInstance(parameterType.classifier as KClass<*>, parameterType)
        }
        else -> throw Error("Type of the classifier $classifier is not supported")
    }
}
