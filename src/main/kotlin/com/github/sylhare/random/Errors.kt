package com.github.sylhare.random

class NoUsableConstructor : Error()

fun <T> tryOf(method: () -> T): T? = try {
    method()
} catch (e: Throwable) {
    println("no-op. We catch any possible error here that might occur during class creation, $e")
    e.printStackTrace()
    null
}
