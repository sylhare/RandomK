package com.github.sylhare

class NoUsableConstructor(message: String) : Error(message)

fun <T> tryOf(method: () -> T): T? = try {
    method()
} catch (e: Throwable) {
    println("Can't use randomly selected constructor, $e")
    null
}
