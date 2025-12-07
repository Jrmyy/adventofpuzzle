package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day13 : Day<Day13.TransparentPaper, Int> {

    data class TransparentPaper(
        val grid: MutableList<MutableList<Char>>,
        val foldInstructions: List<Pair<FoldDirection, Int>>
    ) {

        fun print() {
            println(grid.joinToString("\n") { it.joinToString("") })
        }

        fun fold(foldsCount: Int = foldInstructions.size) {
            foldInstructions.subList(0, foldsCount).forEach { fi ->
                val (direction, index) = fi
                val foldedGrid = if (direction == FoldDirection.UP) {
                    val foldedPart = grid.takeLast(grid.size - (index + 1)).reversed()
                    val foldOnPart = grid.take(index)
                    assert(foldOnPart.size == foldedPart.size)
                    foldOnPart.mapIndexed { y, r ->
                        r.mapIndexed { x, c ->
                            if (c == '#') foldOnPart[y][x] else foldedPart[y][x]
                        }
                    }
                } else {
                    grid.map {
                        val foldedPart = it.takeLast(it.size - (index + 1)).reversed()
                        val foldOnPart = it.take(index)
                        assert(foldOnPart.size == foldedPart.size)
                        foldOnPart.mapIndexed { x, c ->
                            if (c == '#') foldOnPart[x] else foldedPart[x]
                        }
                    }
                }
                reshapePaper(foldedGrid)
            }
        }

        private fun reshapePaper(foldedGrid: List<List<Char>>) {
            grid.clear()
            grid.addAll(foldedGrid.map { it.toMutableList() }.toMutableList())
        }

        companion object {

            enum class FoldDirection {
                UP, LEFT
            }

            fun from(dots: List<Pair<Int, Int>>, foldInstructions: List<Pair<String, Int>>): TransparentPaper =
                TransparentPaper(
                    (0..dots.maxOf { it.second }).mapIndexed { y, r ->
                        (0..dots.maxOf { it.first }).mapIndexed { x, i ->
                            if (dots.contains(Pair(x, y))) '#' else '.'
                        }.toMutableList()
                    }.toMutableList(),
                    foldInstructions.map {
                        Pair(if (it.first == "y") FoldDirection.UP else FoldDirection.LEFT, it.second)
                    }
                )
        }
    }

    override fun runPartOne(): Int {
        val paper = getInput()
        paper.fold(1)
        return paper.grid.flatten().count { it == '#' }
    }

    override fun runPartTwo(): Int {
        val paper = getInput()
        paper.fold()
        paper.print()
        return 0
    }

    override fun getInput(): TransparentPaper = AOCUtils.getDayInput(2021, 13).let { lines ->
        val separatorIdx = lines.indexOfFirst { it == "" }
        val dotsLines = lines.subList(0, separatorIdx)
        val foldLines = lines.subList(separatorIdx + 1, lines.size)
        TransparentPaper.from(
            dotsLines.map { l -> l.split(",").let { p -> Pair(p.first().toInt(), p.last().toInt()) } },
            foldLines.map { l ->
                l.removePrefix("fold along ")
                    .split("=")
                    .let { p -> Pair(p.first(), p.last().toInt()) }
            }
        )
    }
}

fun main() {
    val day = Day13()
    println(day.runPartOne())
    day.runPartTwo()
}
