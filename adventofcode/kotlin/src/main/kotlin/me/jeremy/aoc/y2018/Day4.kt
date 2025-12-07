package me.jeremy.aoc.y2018

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Day4 : Day<List<Day4.GuardEvent>, Int> {

    enum class GuardEventType {
        SHIFT_BEGIN,
        FALL_ASLEEP,
        WAKE_UP,
    }

    data class GuardEvent(
        val timestamp: LocalDateTime,
        val id: Int,
        val eventType: GuardEventType
    )

    override fun runPartOne(): Int {
        val minutesAsleepPerGuard = computeMinutesAsleepPerGuard()
        return (
            minutesAsleepPerGuard.maxByOrNull {
                it.value.size
            } ?: error("No minute asleep for any guard")
            )
            .toPair()
            .let {
                Pair(
                    it.first,
                    (
                        it.second
                            .groupingBy { m -> m }
                            .eachCount()
                            .maxByOrNull { m -> m.value } ?: error("No max minute")
                        )
                        .key
                )
            }
            .toList()
            .reduce { acc, i -> acc * i }
    }

    override fun runPartTwo(): Int {
        val minutesAsleepPerGuard = computeMinutesAsleepPerGuard()
        return (
            (0 until 60).map { minute ->
                Pair(
                    minute,
                    minutesAsleepPerGuard.map {
                        Pair(it.key, it.value.count { asleepMinute -> asleepMinute == minute })
                    }.maxByOrNull { it.second } ?: error("No guard asleep for this minute")
                )
            }.maxByOrNull {
                it.second.second
            } ?: error("No max minute asleep for any guard")
            )
            .let {
                Pair(it.first, it.second.first)
            }
            .toList()
            .reduce { acc, i -> acc * i }
    }

    override fun getInput(): List<GuardEvent> {
        var currentGuardIdx = -1
        return AOCUtils.getDayInput(2018, 4)
            .map {
                Pair(
                    LocalDateTime.parse(
                        Regex("\\[(.+)]").find(it)!!.groupValues[1],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    ),
                    it.split("] ")[1]
                )
            }
            .sortedBy { it.first }
            .map {
                val (timestamp, rawEvent) = it
                val eventType = when {
                    rawEvent.startsWith("Guard #") -> {
                        currentGuardIdx = Regex("Guard #(\\d+)").find(rawEvent)!!.groupValues[1].toInt()
                        GuardEventType.SHIFT_BEGIN
                    }
                    rawEvent == "wakes up" -> GuardEventType.WAKE_UP
                    else -> GuardEventType.FALL_ASLEEP
                }
                GuardEvent(timestamp, currentGuardIdx, eventType)
            }
    }

    private fun computeMinutesAsleepPerGuard(): MutableMap<Int, MutableList<Int>> {
        val minutesAsleepPerGuard = mutableMapOf<Int, MutableList<Int>>()
        var asleepTimestamp: LocalDateTime? = null
        getInput().forEach {
            if (it.eventType == GuardEventType.FALL_ASLEEP) {
                asleepTimestamp = it.timestamp
            } else if (it.eventType == GuardEventType.WAKE_UP) {
                minutesAsleepPerGuard[it.id] = (
                    minutesAsleepPerGuard.getOrDefault(it.id, mutableListOf()) + (
                        asleepTimestamp!!.minute until it.timestamp.minute
                        )
                    ).toMutableList()
            }
        }
        return minutesAsleepPerGuard
    }
}

fun main() {
    val day = Day4()
    println(day.runPartOne())
    println(day.runPartTwo())
}
