package com.github.kisaragieffective.ipail

sealed class Operator<T> : Expr<T>

sealed class BinaryOperator<T> : Operator<T>() {
    operator fun plus(other: Expr<T>): Expr<T> {
        return PlusExpr(this, other)
    }

    operator fun minus(other: Expr<T>): Expr<T> {
        return MinusExpr(this, other)
    }

    open operator fun times(other: Expr<T>): Expr<T> {
        return TimesExpr(this, other)
    }

    open operator fun div(other: Expr<T>): Expr<T> {
        return DivideExpr(this, other)
    }
}

class PlusExpr<T>(val left: Expr<T>, val right: Expr<T>): BinaryOperator<T>() {
    override fun times(other: Expr<T>): Expr<T> {
        return TimesExpr(PreludeExpr(this), other)
    }

    override fun div(other: Expr<T>): Expr<T> {
        return DivideExpr(PreludeExpr(this), other)
    }

    override fun toString(): String {
        return "$left + $right"
    }
}

class TimesExpr<T>(val left: Expr<T>, val right: Expr<T>): BinaryOperator<T>() {
    override fun toString(): String {
        return "$left * $right"
    }
}

class DivideExpr<T>(val left: Expr<T>, val right: Expr<T>): BinaryOperator<T>() {
    override fun toString(): String {
        return "$left / $right"
    }
}

class MinusExpr<T>(val left: Expr<T>, val right: Expr<T>): BinaryOperator<T>() {
    override fun times(other: Expr<T>): Expr<T> {
        return TimesExpr(PreludeExpr(this), other)
    }

    override fun div(other: Expr<T>): Expr<T> {
        return DivideExpr(PreludeExpr(this), other)
    }

    override fun toString(): String {
        return "$left + $right"
    }
}

class PreludeExpr<T>(val wrapper: Expr<T>) : Expr<T> {
    override fun toString(): String {
        return "($wrapper)"
    }
}

class Literal<T>(val from: T): Expr<T> {
    override fun toString(): String {
        return from.toString()
    }
}

class IndexAccess<T>(val ref: Variable<T>, val index: Expr<Int>) : Operator<T>(), LeftSideExpression<T> {
    override fun toString(): String {
        return explain
    }

    operator fun plus(other: Expr<T>) = PlusExpr(this, other)

    operator fun minus(other: Expr<T>) = MinusExpr(this, other)

    operator fun times(other: Expr<T>) = TimesExpr(this, other)

    operator fun div(other: Expr<T>) = DivideExpr(this, other)
    override val explain: String
        get() = "$ref[$index]"
}

class Equal<T>(val left: Expr<T>, val right: Expr<T>) : Expr<T> {
    override fun toString(): String {
        return "$left == $right"
    }
}

class NotEqual<T>(val left: Expr<T>, val right: Expr<T>) : Expr<T> {
    override fun toString(): String {
        return "$left != $right"
    }
}
