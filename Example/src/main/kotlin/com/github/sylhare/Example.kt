package com.github.sylhare

data class Example(val i: Int, val s: String, val m: Map<Long, Char>)

data class OtherExample(val e: Example, val note: String)

class Presenter() {

    var other: OtherExample? = null
    fun showcase(): OtherExample? = other
}
