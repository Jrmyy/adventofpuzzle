package me.jeremy.aoc

interface Day<IptT, ResT> {

    fun runPartOne(): ResT

    fun runPartTwo(): ResT

    fun getInput(): IptT
}
