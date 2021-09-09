package com.github.sylhare

class NoUsableConstructor(message: String) : Error(message)

fun <T> tryOf(method: () -> T): T? = try {
    method()
} catch (e: Throwable) {
    println("no-op. We catch any possible error here that might occur during class creation, ${e.message}")
    null
}
