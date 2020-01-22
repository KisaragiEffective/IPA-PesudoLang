package com.github.kisaragieffective.ipail

interface Atom<T> : Expr<T> {
    fun value(): T
}

class IntConst(val value: Int) : Atom<Int> {
    override fun value(): Int {
        return value
    }

    override fun toString(): String {
        return value.toString()
    }
}

class FloatingConst(val value: Double) : Atom<Double> {
    override fun value(): Double {
        return value
    }
}

class StringConst(val value: String) : Atom<String> {
    override fun value(): String {
        return value
    }

    override fun toString(): String {
        return value()
    }
}

object NullConst : Atom<Nothing?> {
    override fun value(): Nothing? {
        return null
    }
}