package com.github.kisaragieffective.ipail.questions

import com.github.kisaragieffective.ipail.*

fun main() {
    val stringDeclation = mapOf(
            "sauce" to Variable("String", String::class.java),
            "search" to Variable.invoke("Word")
    )

    val intDeclartion = mapOf(
            "sauceIndex" to Variable("M", Int::class.java),
            "i" to Variable("i"),
            "j" to Variable("j"),
            "searchIndex" to Variable("N"),
            "count" to Variable("total")
    )
    val i by intDeclartion
    val j by intDeclartion
    val sauceIndex by intDeclartion
    val searchIndex by intDeclartion
    val count by intDeclartion
    val sauce by stringDeclation
    val search by stringDeclation
    listOf(
            Let(count, IntConst(0)),
            Let(i, IntConst(0)),
            Comment("a) right side value"),
            BeforeLoop(
                    LessOrEqual(i, MinusExpr(sauceIndex, searchIndex)),
                    listOf(
                            Let(j, 0),
                            Comment("b) right side value"),
                            BeforeLoop(
                                    Less(j, searchIndex) and Equal(sauce[PlusExpr(i, j)], search[j]),
                                    listOf(
                                            Output(i),
                                            Let(count, count + 1),
                                            Let(i, i + searchIndex)
                                    ),
                                    nest = 1
                            )
                    )
            )
    ).map(Instruction::toString).joinToString("\n").transport()
}