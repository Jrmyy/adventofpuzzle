package me.jeremy.aoc.y2021

import me.jeremy.aoc.AOCUtils
import me.jeremy.aoc.Day

class Day16 : Day<List<Int>, Long> {

    data class Packet(
        val version: Long,
        val type: Long,
        val subPackets: MutableList<Packet> = mutableListOf(),
        var value: Long? = null
    ) {
        fun sumVersions(): Long = version + subPackets.sumOf { it.sumVersions() }

        fun getFullValue(): Long = if (value != null) value!! else {
            when (type) {
                0L -> subPackets.sumOf { it.getFullValue() }
                1L -> subPackets.fold(1L) { acc, s -> acc * s.getFullValue() }
                2L -> subPackets.minOf { it.getFullValue() }
                3L -> subPackets.maxOf { it.getFullValue() }
                5L -> {
                    assert(subPackets.size == 2)
                    if (subPackets.first().getFullValue() > subPackets.last().getFullValue()) 1 else 0
                }
                6L -> {
                    assert(subPackets.size == 2)
                    if (subPackets.first().getFullValue() < subPackets.last().getFullValue()) 1 else 0
                }
                7L -> {
                    assert(subPackets.size == 2)
                    if (subPackets.first().getFullValue() == subPackets.last().getFullValue()) 1 else 0
                }
                else -> throw Exception("Wrong type : $type")
            }
        }
    }

    override fun runPartOne(): Long {
        val bits = getInput()
        val (packet, _) = parsePacket(bits)
        return packet.sumVersions()
    }

    override fun runPartTwo(): Long {
        val bits = getInput()
        val (packet, _) = parsePacket(bits)
        return packet.getFullValue()
    }

    override fun getInput(): List<Int> = AOCUtils.getDayInput(2021, 16).first().toList().flatMap {
        Integer.parseInt(it.toString(), 16).toString(2)
            .padStart(4, '0')
            .toList()
            .map { c -> c.toString().toInt() }
    }

    private fun parsePacket(bits: List<Int>, cursor: Int = 0): Pair<Packet, Int> {
        var iCursor = cursor
        val packetVersion = hexToInt(bits.subList(cursor, cursor + 3))
        val packetType = hexToInt(bits.subList(cursor + 3, cursor + 6))
        val packet = Packet(packetVersion, packetType)
        if (packetType == 4L) {
            var currentIdx = iCursor + 6
            var group = bits.subList(currentIdx, currentIdx + 5)
            var shouldStop = false
            val literalValue = mutableListOf<Int>()
            while (!shouldStop) {
                literalValue.addAll(group.subList(1, group.size))
                if (group.first() == 0) {
                    shouldStop = true
                }
                currentIdx += 5
                if (currentIdx <= bits.size - 5) {
                    group = bits.subList(currentIdx, currentIdx + 5)
                } else {
                    break
                }
            }
            packet.value = hexToInt(literalValue)
            return Pair(packet, currentIdx)
        } else {
            when (bits[cursor + 6]) {
                0 -> {
                    val totalLength = hexToInt(bits.subList(cursor + 7, cursor + 22))
                    val oldICursor = cursor + 22
                    iCursor = cursor + 22
                    while (iCursor - oldICursor < totalLength) {
                        val (subPacket, newCursor) = parsePacket(bits, iCursor)
                        iCursor = newCursor
                        packet.subPackets.add(subPacket)
                    }
                    return Pair(packet, iCursor)
                }
                1 -> {
                    val subPacketsCount = hexToInt(bits.subList(cursor + 7, cursor + 18)).toInt()
                    iCursor = cursor + 18
                    repeat(subPacketsCount) {
                        val (subPacket, newCursor) = parsePacket(bits, iCursor)
                        iCursor = newCursor
                        packet.subPackets.add(subPacket)
                    }
                    return Pair(packet, iCursor)
                }
                else -> {
                    throw Exception("Wrong length ID")
                }
            }
        }
    }

    private fun hexToInt(hex: List<Int>): Long = hex.joinToString("").toLong(2)
}

fun main() {
    val day = Day16()
    println(day.runPartOne())
    println(day.runPartTwo())
}
