package search

import engine.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource


class Limits {
    private val start = TimeSource.Monotonic.markNow()
    private var spend = Duration.ZERO
    private var time = 0
    private var inc = 0
    private var moveTime = 0
    private var movesToGo = 30
    private var depth = 0
    private var mate = 0
    private var isInfinite = false
    private var isTimeLimited = false

    fun getDepth(): Int = depth
    fun getMate(): Int = mate
    fun isInfinite(): Boolean = isInfinite
    fun elapsed(): Duration = start.elapsedNow()
    fun timeUp(): Boolean = isTimeLimited && elapsed() >= spend

    fun parse(str: String, stm: Color) {
        val tokens = str.split(" ").iterator()

        val value: () -> Int = { tokens.next().toInt() }

        while (tokens.hasNext())
            when (tokens.next()) {
                "wtime" -> if (stm == WHITE) time = value()
                "winc"  -> if (stm == WHITE) inc  = value()
                "btime" -> if (stm == BLACK) time = value()
                "binc"  -> if (stm == BLACK) inc  = value()
                "movestogo" -> movesToGo = value()
                "movetime"  -> moveTime  = value()
                "depth"     -> depth     = value()
                "mate"      -> mate      = value()
            }

        depth = if (depth != 0) depth else 100
        isInfinite = "infinite" in str
        isTimeLimited = time != 0 || moveTime != 0

        val overhead = 15
        spend = when (moveTime) {
            0    -> (time / movesToGo + inc).coerceAtMost(time - overhead)
            else -> (moveTime - overhead)
        }.milliseconds
    }
}
