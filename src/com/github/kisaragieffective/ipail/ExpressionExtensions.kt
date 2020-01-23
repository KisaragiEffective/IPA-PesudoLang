package com.github.kisaragieffective.ipail

infix fun Expr<Boolean>.and(other: Expr<Boolean>) = And(this, other)

operator fun Expr<String>.get(index: Int) = IndexAccess(Variable<String>("__anon__"), IntConst(index))

operator fun <T> Expr<T>.plus(other: Expr<T>) = PlusExpr(this, other)

operator fun <T : Number> Expr<T>.minus(other: Expr<T>) = MinusExpr(this, other)

operator fun <T : Number> Expr<T>.times(other: Expr<T>) = TimesExpr(this, other)

