import engine.*
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource.Monotonic.markNow


@Volatile
var STOP = false

@ExperimentalTime
var limits: Limits = Limits()


@ExperimentalTime
class Limits {
    private val start = markNow()
    private var spend = Duration.ZERO
    private var time = 0
    private var inc = 0
    private var movetime = 0
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
                "movetime"  -> movetime  = value()
                "depth"     -> depth     = value()
                "mate"      -> mate      = value()
            }

        depth = if (depth != 0) depth else 100
        isInfinite = "infinite" in str
        isTimeLimited = time != 0 || movetime != 0

        val overhead = 15
        spend = Duration.milliseconds(
            when (movetime) {
                0    -> (time / movesToGo + inc).coerceAtMost(time - overhead)
                else -> movetime - overhead
            }
        )
    }
}

@ExperimentalTime
fun go(pos: Position, str: String) {

    limits = Limits().apply { parse(str.substringAfter(' ', ""), pos.stm) }
    STOP = false
    var bestMove = Move(A1, A1)

    for (depth in 1..limits.getDepth()) {
        val score = pos.minimax(0, depth)

        if (STOP || limits.timeUp()) break

        bestMove = bestMove_

        println("info depth $depth score ${score.toUCI()} time ${limits.elapsed().inWholeMilliseconds} pv ${bestMove.toUCI()}")

        if (MATE - abs(score) <= 2 * abs(limits.getMate())) break
    }

    while (limits.isInfinite() && !STOP) { /* Wait for stop */ }

    println("bestmove ${bestMove.toUCI()}")
}

var bestMove_: Move = Move(A1, A1)

@ExperimentalTime
fun Position.minimax(ply: Int, depth: Int): Int {

    val root = ply == 0

    if (!root && (STOP || limits.timeUp() || mr50 >= 100)) return 0

    if (depth == 0) return eval()

    var bestScore = -INF

    for (move in genMoves()) {

        if (!makeMove(move)) continue
        val score = -minimax(ply + 1, depth - 1)
        takeMove()

        if (score > bestScore) {
            bestScore = score
            if (root)
                bestMove_ = move
        }
    }

    if (bestScore == -INF) return when {
        inCheck(stm) -> -(MATE - ply)
        else         -> 0
    }

    return bestScore
}