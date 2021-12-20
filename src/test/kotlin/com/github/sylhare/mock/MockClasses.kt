package com.github.sylhare.mock


@Suppress("UNUSED_PARAMETER")
object MockClasses {
    class A {
        override fun toString(): String = "A"
    }

    class B(val a: A) {
        override fun toString() = "B(a=$a)"
    }

    class C(val a: A, val b: B) {
        override fun toString() = "C(a=$a, b=$b)"
    }

    data class D(val a: A, val b: B?, val c: C) {
        override fun toString() = "D(a=$a, b=$b, c=$c)"
    }

    class E {
        constructor(a: A)
        constructor() {
            throw Error("Do not use this one")
        }

        override fun toString() = "E"
    }

    class F(private val hello: String) {
        override fun toString() = "F(hello=$hello)"
    }

    class G(val f1: F, val f2: F, val c: Char, val str: String, val l: Long, val m: Map<String, C>) {
        override fun toString() = "G(f1=$f1, f2=$f2, c=$c, str=$str, l=$l, m=$m)"
    }

    class GT<T> {
        var t: T? = null
    }

    class GA<T>(var t: T)
    class GAA<T1, T2>(val t1: T1, val t2: T2)
    class GTA<T1, T2>(val t2: T2)
    class H(val i: Int, val s: String, val l: List<A>)
    class I(val ints: List<Int>)
    class J(val map: Map<Long, String>)
    class K(val aa: String, val a: Array<Int>)
    class L {
        val a: N by lazy { HELLO }
        var b: N = N("default")

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

        constructor() {
            number = 1
        }

        constructor(a: A) {
            number = 2
        }

        constructor(a: A, b: B) {
            number = 3
        }

        constructor(b: B) {
            number = 4
        }

        constructor(c: C) {
            number = 5
        }

        constructor(d: D) {
            number = D(a, b, c).hashCode()
        }
    }

    data class N(val value: String)

    class O {
        init {
            print("Hello")
        }
    }

    class P {
        private constructor()
    }

    class Q (var alreadyNull: String? = null, var notYetNull: String?)
    sealed class S

    data class SimpleClassWithEnum(val status: Status)

    data class SimpleClassWithEnumList(val statuses: List<Status>)

    data class NestedClassWithEnum(val simpleClassWithEnum: SimpleClassWithEnum)

    enum class Status {
        ENABLED, DISABLED
    }
}
