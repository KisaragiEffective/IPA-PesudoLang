package com.github.kisaragieffective.ipail

sealed class Instruction

sealed class Statement : Instruction()

class Comment(val content: String) : Instruction() {
    override fun toString(): String {
        return "/* $content */"
    }
}

sealed class CallProcedure(val name: String, val value: List<Variable<*>>) : Statement()

sealed class Let<T>(val variable: LeftSideExpression<T>) : Statement()

class LetConst<T>(variable: LeftSideExpression<T>, val value: T) : Let<T>(variable) {
    override fun toString(): String {
        return "・ " + variable.explain + " <- " + value
    }
}

class LetVar<T>(variable: LeftSideExpression<T>, val expression: String) : Let<T>(variable) {
    override fun toString(): String {
        return "$variable <- $expression"
    }
}

class LetExpr<T>(variable: LeftSideExpression<T>, val expression: Expr<T>) : Let<T>(variable) {
    override fun toString(): String {
        return "$variable <- $expression"
    }

    companion object {
        operator fun invoke(variable: LeftSideExpression<String>, constNull: NullConst): LetExpr<String> {
            return LetExpr(variable, StringConst("null"))
        }
    }
}

sealed class Block(val instructions: List<Instruction>) : Instruction()

sealed class IfBlock(val then: List<Instruction>, val els: List<Instruction> = emptyList()) : Instruction() {
    override fun toString(): String {
        if (els.isEmpty()) {
            val thenAsm = then.joinToString("\n") { "| $it" }
            return """
▲
$thenAsm
▼
""".trimIndent()
        } else {
            val thenAsm = then.joinToString("\n") { "| $it" }
            val elseAsm = els.joinToString("\n") { "| $it" }
            return """
▲
$thenAsm
+----------
$elseAsm
▼
"""
        }

    }
}

sealed class Loop(instructions: List<Instruction>) : Block(instructions)

class BeforeLoop(val condition: String, instructions: List<Instruction>, val nest: Int = 0) : Loop(instructions) {
    constructor(condition: Expr<Boolean>, instructions: List<Instruction>, nest: Int = 0)
            : this(condition.toString(), instructions, nest)
    override fun toString(): String {
        return """
■ $condition
${instructions.joinToString("\n") {"| $it"}}
■
        """.trimIndent()
    }
}

class AfterLoop(val condition: String, instructions: List<Instruction>) : Loop(instructions) {
    override fun toString(): String {
        return """
■
${instructions.joinToString("\n") {"| $it"}}
■ $condition
        """.trimIndent()
    }
}

sealed class Member

interface LeftSideExpression<T> {
    val explain: String
}

class Variable<T>(override val explain: String, val type: Class<T>) : Member(), Atom<T>, LeftSideExpression<T> {
    override fun toString(): String {
        return explain
    }

    override fun value(): T {
        return null as T
    }

    companion object {
        inline operator fun <reified T> invoke(name: String): Variable<T> {
            return Variable(name, T::class.java)
        }
    }
}

class Output(val value: Expr<*>) : Instruction()

fun main() {
    val intDeclares = mapOf(
            "i" to Variable("i", Int::class.java),
            "j" to Variable("j", Int::class.java)
    )

    val stringDeclares = mapOf(
            "word" to Variable("Word", String::class.java),
            "add" to Variable("Add", String::class.java)
    )

    val i by intDeclares
    val j by intDeclares
    val word by stringDeclares
    val add by stringDeclares
    listOf(
            LetConst(i, 0),
            BeforeLoop(
                    "Word[i] != null",
                    listOf(
                            LetExpr(i, i + 1)
                    )
            ),
            LetConst(j, 0),
            BeforeLoop(
                    "Add[j] != null",
                    listOf(
                            Comment("Question B"),
                            LetExpr(i, i + 1),
                            LetExpr(j, j + 1)
                    )
            ),
            LetExpr(add[i], NullConst)
    ).map(Instruction::toString).joinToString("\n").transport()
}

operator fun <T> Variable<T>.plus(other: Expr<T>) = PlusExpr(this, other)

operator fun Variable<Int>.plus(other: Int) = PlusExpr(this, IntConst(other))

operator fun Variable<String>.get(index: Int) = IndexAccess(this, IntConst(index))

operator fun Variable<String>.get(index: Expr<Int>) = IndexAccess(this, index)

fun Any.transport() {
    println(this)
}

inline infix fun <T, Q> T.pipe(p: (T) -> Q) : Q {
    return p(this)
}
