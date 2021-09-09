package com.github.sylhare


class A
class B(val a: A)
class C(val a: A, val b: B)
data class D(val a: A, val b: B, val c: C)
class E {
    constructor(a: A)
    constructor() {
        throw Error("Do not use this one")
    }
}
class F(private val hello: String)
class G(val f1: F, val f2: F, val c: Char, val str: String, val l: Long, val m: Map<String, C>)

class L {
    val a: N by lazy { HELLO }
    lateinit var b: N
    constructor(str: String) {
        this.b = N(str)
    }
    companion object {
        val HELLO = N("hello")
    }
}
class M {
    private val a: A = A()
    private val b: B = B(A())
    private val c: C = C(a, b)
    var number: Int = 0
    constructor() { number = 1 }
    constructor(a: A) { number = 2 }
    constructor(a: A, b: B) { number = 3 }
    constructor(b: B) { number = 4 }
    constructor(c: C) { number = 5 }
    constructor(d: D) { number = D(a, b, c).hashCode() }
}
data class N (val value: String)
sealed class S
class P {
    private constructor()
}
