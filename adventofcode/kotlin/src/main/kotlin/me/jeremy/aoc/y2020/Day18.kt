package me.jeremy.aoc.y2020

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day18 : Day<List<Day18.Operation>, Long> {

    data class OperationPart(
        val value: Pair<Long?, Operation?>,
        val leftOperator: String? = null
    ) {
        fun childCalculation(): Long =
            if (value.first != null) {
                value.first!!
            } else {
                value.second!!.childCalculation()
            }

        fun adultCalculation(): Long =
            if (value.first != null) {
                value.first!!
            } else {
                value.second!!.adultCalculation()
            }
    }

    data class Operation(
        val parts: MutableList<OperationPart> = mutableListOf()
    ) {
        fun childCalculation(): Long {
            var value = parts[0].childCalculation()
            parts.drop(1).forEach {
                when (it.leftOperator) {
                    "+" -> value += it.childCalculation()
                    "*" -> value *= it.childCalculation()
                }
            }
            return value
        }

        fun adultCalculation(): Long {
            val additions = parts
                .mapIndexed { index, operationPart -> Pair(index, operationPart) }
                .filter { it.second.leftOperator == "+" }
            val additionResults = mutableListOf<Pair<Int, Long>>()
            additions.forEach {
                val prev = if ((it.first - 1) in additionResults.map { that -> that.first }) {
                    additionResults.first { that -> that.first == it.first - 1 }.second
                } else {
                    parts[it.first - 1].adultCalculation()
                }
                additionResults.add(Pair(it.first, prev + it.second.adultCalculation()))
            }
            additionResults.forEachIndexed { index, it ->
                parts.removeAt(it.first - index)
                val prev = parts.removeAt(it.first - index - 1)
                parts.add(it.first - index - 1, OperationPart(Pair(it.second, null), prev.leftOperator))
            }
            var value = parts[0].adultCalculation()
            parts.drop(1).forEach {
                value *= it.adultCalculation()
            }
            return value
        }
    }

    override fun runPartOne(): Long {
        val operations = getInput()
        return operations.sumOf { it.childCalculation() }
    }

    override fun runPartTwo(): Long {
        val operations = getInput()
        return operations.sumOf { it.adultCalculation() }
    }

    override fun getInput(): List<Operation> =
        AOCUtils.getDayInput(2020, 18).map {
            val parts = it.split(" ")
            val allParenthesis = mutableListOf<Pair<String?, Operation>>()
            var currentOperator: String? = null
            var currentValue: Int? = null
            val operations = mutableListOf<OperationPart>()
            parts.forEach { s ->
                when {
                    s.startsWith("(") -> {
                        val parenthesisNumber = s.count { that -> that == '(' }
                        currentValue = s[parenthesisNumber].toString().toInt()
                        allParenthesis.add(Pair(currentOperator, Operation()))
                        (1 until parenthesisNumber).forEach { _ ->
                            allParenthesis.add(Pair(null, Operation()))
                        }
                        currentOperator = null
                    }
                    s.endsWith(")") -> {
                        val parenthesisNumber = s.count { that -> that == ')' }
                        allParenthesis.last().second.parts.add(
                            OperationPart(
                                Pair(s[0].toString().toLong(), null),
                                currentOperator
                            )
                        )
                        (0 until parenthesisNumber).forEach { _ ->
                            val last = allParenthesis.removeLast()
                            val part = OperationPart(
                                Pair(null, last.second),
                                last.first
                            )
                            if (allParenthesis.isNotEmpty()) {
                                allParenthesis.last().second.parts.add(part)
                            } else {
                                operations.add(part)
                            }
                        }
                    }
                    else -> {
                        try {
                            currentValue = s.toInt()
                        } catch (_: NumberFormatException) {
                            currentOperator = s
                        }
                    }
                }
                if (currentValue != null) {
                    val op = OperationPart(
                        Pair(currentValue!!.toLong(), null),
                        currentOperator
                    )
                    if (allParenthesis.isNotEmpty()) {
                        allParenthesis.last().second.parts.add(op)
                    } else {
                        operations.add(op)
                    }
                    currentOperator = null
                    currentValue = null
                }
            }
            Operation(operations)
        }
}

fun main() {
    val day = Day18()
    println(day.runPartOne())
    println(day.runPartTwo())
}
