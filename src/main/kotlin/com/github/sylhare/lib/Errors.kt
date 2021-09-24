package com.github.sylhare

internal class NoUsableConstructor(message: String) : Error(message)

internal  fun <T> tryOf(method: () -> T): T? = try {
    method()
} catch (e: Throwable) {
    println("Can't use randomly selected constructor, $e")
    null
}
