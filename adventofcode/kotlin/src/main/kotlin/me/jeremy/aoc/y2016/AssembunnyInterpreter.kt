package me.jeremy.aoc.y2016

class AssembunnyInterpreter {

    companion object {

        private val ONE_ARGUMENTS = listOf("inc", "dec", "tgl")
        private val TWO_ARGUMENTS = listOf("cpy", "jnz")

        fun compute(
            instructions: MutableList<String>,
            registers: MutableMap<String, Int> = mutableMapOf(),
            afterOptsToStop: Int = 10,
        ): Int {
            var i = 0
            var prevOpt: Int? = null
            var optsCount = 0
            while (i < instructions.size) {
                val instruction = instructions[i]
                if (instruction.startsWith("cpy")) {
                    val (from, to) = instruction.replace("cpy ", "").split(" ")
                    if ("[a-z]".toRegex().matches(to)) {
                        if ("-?\\d+".toRegex().matches(from)) {
                            registers[to] = from.toInt()
                        } else {
                            registers[to] = registers[from] ?: 0
                        }
                    }
                    i++
                } else if (instruction.startsWith("inc")) {
                    instruction.replace("inc ", "").let { inc ->
                        if ("[a-z]".toRegex().matches(inc)) {
                            registers[inc] = (registers[inc] ?: 0) + 1
                        }
                    }
                    i++
                } else if (instruction.startsWith("dec")) {
                    instruction.replace("dec ", "").let { dec ->
                        if ("[a-z]".toRegex().matches(dec)) {
                            registers[dec] = (registers[dec] ?: 0) - 1
                        }
                    }
                    i++
                } else if (instruction.startsWith("jnz")) {
                    val (x, y) = instruction.replace("jnz ", "").split(" ")
                    val shouldJump = (if ("-?\\d+".toRegex().matches(x)) x.toInt() else (registers[x] ?: 0)) != 0
                    val plusI = if ("-?\\d+".toRegex().matches(y)) y.toInt() else (registers[y] ?: 0)
                    if (shouldJump) {
                        i += plusI
                    } else {
                        i++
                    }
                } else if (instruction.startsWith("tgl")) {
                    val x = instruction.replace("tgl ", "").let { xi ->
                        if ("-?\\d+".toRegex().matches(xi)) xi.toInt() else (registers[xi] ?: 0)
                    }
                    if (i + x < instructions.size && i + x >= 0) {
                        val toReplace = instructions[i + x]
                        val oaType = ONE_ARGUMENTS.firstOrNull { toReplace.startsWith(it) }
                        val taType = TWO_ARGUMENTS.firstOrNull { toReplace.startsWith(it) }
                        if (oaType == "inc") {
                            instructions[i + x] = toReplace.replace("inc", "dec")
                        } else if (oaType != null) {
                            instructions[i + x] = toReplace.replace(oaType, "inc")
                        } else if (taType == "jnz") {
                            instructions[i + x] = toReplace.replace("jnz", "cpy")
                        } else if (taType != null) {
                            instructions[i + x] = toReplace.replace(taType, "jnz")
                        }
                    }
                    i++
                } else if (instruction.startsWith("out")) {
                    val x = instruction.replace("out ", "").let { xi ->
                        if ("-?\\d+".toRegex().matches(xi)) xi.toInt() else (registers[xi] ?: 0)
                    }
                    if (x in 0..1 && prevOpt != x) {
                        prevOpt = x
                        optsCount++
                        if (optsCount >= afterOptsToStop) {
                            break
                        }
                    } else {
                        error("This opt is not compliant, opt is $x and prev opt was $prevOpt")
                    }
                    i++
                }
            }
            return registers["a"]!!
        }
    }
}
