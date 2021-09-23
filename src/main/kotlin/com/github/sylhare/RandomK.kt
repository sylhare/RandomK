package com.github.sylhare

import kotlin.random.Random
import kotlin.reflect.typeOf


@ExperimentalStdlibApi
inline fun <reified T : Any> randomK(
    random: Random = Random,
    config: RandomK.Config = RandomK.Config()
): T {
    return RandomBuilder(random, config).build(T::class, typeOf<T>()) as T
}

object RandomK {
    class Config(
        var collectionRange: IntRange = 1..5,
        var stringRange: IntRange = 1..50,
        var any: Any = "Anything"
    ) {
        fun numberOfElements(random: Random) = random.nextInt(this.collectionRange.first, this.collectionRange.last + 1)
        fun numberOfChars(random: Random) = random.nextInt(this.stringRange.first, this.stringRange.last + 1)
    }
}
