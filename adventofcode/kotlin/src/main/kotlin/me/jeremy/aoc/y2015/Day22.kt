package me.jeremy.aoc.y2015

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import kotlin.math.max

class Day22 : Day<Pair<Int, Int>, Int> {

    data class State(
        val player: Pair<Int, Int>,
        val boss: Pair<Int, Int>,
        val state: MutableMap<String, Int>,
        val turn: Int
    )

    override fun runPartOne(): Int = minManaToWin(Pair(50, 500), getInput())

    override fun runPartTwo(): Int = minManaToWin(Pair(50, 500), getInput(), mode = "hard")

    override fun getInput(): Pair<Int, Int> = AOCUtils.getDayInput(2015, 22).let { lines ->
        Pair(
            lines.first().split(": ").last().toInt(),
            lines.last().split(": ").last().toInt()
        )
    }

    private fun minManaToWin(
        player: Pair<Int, Int>,
        boss: Pair<Int, Int>,
        mana: Int = 0,
        turn: Int = 1,
        state: MutableMap<String, Int> = mutableMapOf(),
        cache: MutableMap<State, Int> = mutableMapOf(),
        mode: String = "easy"
    ): Int {
        var p1 = player.copy()
        var p2 = boss.copy()
        if (mode == "hard" && turn == 1) {
            p1 = Pair(p1.first - 1, p1.second)
        }
        val s = State(p1.copy(), p2.copy(), state.toMutableMap(), turn)
        if (s in cache) return cache.getValue(s)
        if (state["poison"] != null) {
            p2 = Pair(p2.first - 3, p2.second)
            state["poison"] = state["poison"]!!.minus(1)
            if (state["poison"] == 0) {
                state.remove("poison")
            }
        }
        if (state["mana"] != null) {
            p1 = Pair(p1.first, p1.second + 101)
            state["mana"] = state["mana"]!!.minus(1)
            if (state["mana"] == 0) {
                state.remove("mana")
            }
        }
        if (state["shield"] != null) {
            state["shield"] = state["shield"]!!.minus(1)
            if (state["shield"] == 0) {
                state.remove("shield")
            }
        }
        val cacheValue = if (p1.first <= 0 || p1.second <= 0) {
            Int.MAX_VALUE
        } else if (p2.first <= 0) {
            mana
        } else if (mana >= (cache.values.minOrNull() ?: Int.MAX_VALUE)) {
            Int.MAX_VALUE
        } else {
            if (turn == 1) {
                val throwable = Spell.values().filter { MANA_PER_SPELL[it]!! < p1.second }.toMutableList()
                if (state["shield"] != null) {
                    throwable.remove(Spell.SHIELD)
                }
                if (state["poison"] != null) {
                    throwable.remove(Spell.POISON)
                }
                if (state["mana"] != null) {
                    throwable.remove(Spell.RECHARGE)
                }
                throwable.minOfOrNull {
                    val cost = MANA_PER_SPELL[it]!!
                    val ns = state.toMutableMap()
                    when (it) {
                        Spell.MAGIC_MISSILE -> minManaToWin(
                            Pair(p1.first, p1.second - cost),
                            Pair(p2.first - 4, p2.second),
                            mana + cost,
                            2,
                            ns.toMutableMap(),
                            cache,
                            mode
                        )
                        Spell.DRAIN -> {
                            minManaToWin(
                                Pair(p1.first + 2, p1.second - cost),
                                Pair(p2.first - 2, p2.second),
                                mana + cost,
                                2,
                                ns.toMutableMap(),
                                cache,
                                mode
                            )
                        }
                        Spell.SHIELD -> {
                            ns["shield"] = 6
                            minManaToWin(
                                Pair(p1.first, p1.second - cost),
                                p2.copy(),
                                mana + cost,
                                2,
                                ns.toMutableMap(),
                                cache,
                                mode
                            )
                        }
                        Spell.POISON -> {
                            ns["poison"] = 6
                            minManaToWin(
                                Pair(p1.first, p1.second - cost),
                                p2.copy(),
                                mana + cost,
                                2,
                                ns.toMutableMap(),
                                cache,
                                mode
                            )
                        }
                        Spell.RECHARGE -> {
                            ns["mana"] = 5
                            minManaToWin(
                                Pair(p1.first, p1.second - cost),
                                p2.copy(),
                                mana + cost,
                                2,
                                ns.toMutableMap(),
                                cache,
                                mode
                            )
                        }
                    }
                } ?: Int.MAX_VALUE
            } else {
                val dealt = max(p2.second - (if (state["shield"] != null) 7 else 0), 1)
                minManaToWin(
                    Pair(p1.first - dealt, p1.second),
                    p2.copy(),
                    mana,
                    1,
                    state.toMutableMap(),
                    cache,
                    mode
                )
            }
        }
        cache[State(p1.copy(), p2.copy(), state.toMutableMap(), turn)] = cacheValue
        return cacheValue
    }

    companion object {
        enum class Spell {
            MAGIC_MISSILE,
            DRAIN,
            SHIELD,
            POISON,
            RECHARGE
        }

        val MANA_PER_SPELL = mapOf(
            Spell.MAGIC_MISSILE to 53,
            Spell.DRAIN to 73,
            Spell.SHIELD to 113,
            Spell.POISON to 173,
            Spell.RECHARGE to 229
        )
    }
}

fun main() {
    val day = Day22()
    println(day.runPartOne())
    println(day.runPartTwo())
}
