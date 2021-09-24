package com.github.sylhare

import com.github.sylhare.lib.RandomBuilder
import com.github.sylhare.lib.RandomKNotSupportedType
import kotlin.random.Random
import kotlin.reflect.cast
import kotlin.reflect.typeOf


@Throws(RandomKNotSupportedType::class)
@ExperimentalStdlibApi
inline fun <reified T : Any> randomK(
    random: Random = Random,
    config: RandomK.Config = RandomK.Config()
): T {
    val instance = RandomBuilder(random, config).build(T::class, typeOf<T>())
    return try {
        T::class.cast(instance)
    } catch (e: Exception) {
        "Cast from ${instance?.let { it::class }} to ${T::class} is not yet implemented / supported".let {
            println(it)
            throw RandomKNotSupportedType(it)
        }
    }
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
