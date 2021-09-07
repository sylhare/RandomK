import kotlin.random.Random
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

class NoUsableConstructor : Error()

fun makeRandomInstance(clazz: KClass<*>): Any {
    val primitive = makePrimitiveOrNull(clazz)
    if(primitive != null) {
        return primitive
    }

    val constructors = clazz.constructors
        .sortedBy { it.parameters.size }

    for (constructor in constructors) {
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

val random = Random
private fun makePrimitiveOrNull(clazz: KClass<*>) = when(clazz) {
    Int::class -> random.nextInt()
    Long::class -> random.nextLong()
    Double::class -> random.nextDouble()
    Float::class -> random.nextFloat()
    Char::class -> makeRandomChar()
    String::class -> makeRandomString()
    Boolean::class -> random.nextBoolean()
    else -> null
}

private fun makeRandomChar() = random.nextInt().toChar()
private fun makeRandomString() = (1..random.nextInt(100))
    .map { makeRandomChar() }
    .joinToString(separator = "") { "$it" }
