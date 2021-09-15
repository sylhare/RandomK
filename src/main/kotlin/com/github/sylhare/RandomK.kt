package com.github.sylhare

import kotlin.random.Random
import kotlin.reflect.typeOf


@ExperimentalStdlibApi
inline fun <reified T : Any> randomK(
    random: Random = Random,
    config: RandomK.Config = RandomK.Config()
): T {
    val producer = RandomProducer(random, config)
    return producer.make(T::class, typeOf<T>()) as T
}

object RandomK {
    class Config(
        var collectionRange: IntRange = 1..5,
        var stringRange: IntRange = 1..50,
        var any: Any = "Anything"
    )
}
