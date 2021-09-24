package com.github.sylhare.lib

internal class NoUsableConstructor(message: String) : Error(message)

 class RandomKNotSupportedType(message: String): RuntimeException(message)

internal  fun <T> tryOf(method: () -> T): T? = try {
    method()
} catch (e: Throwable) {
    println("Can't use randomly selected constructor, $e")
    null
}
