package misc

import engine.*
import kotlin.time.measureTimedValue


fun perft(input: String, output: Boolean = true): Long {

    fun Position.perft(depth: Int): Long {
        if (depth == 0) return 1L

        var leaves = 0L

        for (move in genMoves()) {
            if (!makeMove(move)) continue
            leaves += perft(depth - 1)
            takeMove()
        }

        return leaves
    }

    val tokens = input.split(" ", limit = 3)
    val depth = tokens.elementAtOrElse(1) { "4" }.toInt()
    val fen   = tokens.elementAtOrElse(2) { KIWIPETE }

    val (leaves, time) = measureTimedValue {
        Position(fen).perft(depth)
    }

    if (output)
        println("""
            $time
            ${leaves * 1000L / time.inWholeMilliseconds.coerceAtLeast(1)}nps
            $leaves
        """.trimIndent())

    return leaves
}
