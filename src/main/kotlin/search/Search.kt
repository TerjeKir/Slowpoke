package search

import engine.*
import eval
import printConclusion
import printThinking
import kotlin.math.abs
import kotlin.time.ExperimentalTime


@Volatile
var STOP = false

@ExperimentalTime
private var limits: Limits = Limits()


@ExperimentalTime
fun go(pos: Position, str: String) {

    limits = Limits().apply { parse(str, pos.stm) }
    STOP = false
    pos.nodeCount = 0UL

    search(pos)
}

@ExperimentalTime
fun search(pos: Position) {

    val ss = Array(100) { Stack() }
    var bestMove = Move(A1, A1)

    for (depth in 1..limits.getDepth()) {

        val score = pos.alphabeta(ss, -INF, INF, 0, depth)

        if (STOP || limits.timeUp()) break

        bestMove = ss[0].pv.moves[0]

        printThinking(limits, depth, score, pos.nodeCount, ss[0].pv)

        if (MATE - abs(score) <= 2 * abs(limits.getMate())) break
    }

    while (limits.isInfinite() && !STOP) { /* Wait for stop */ }

    printConclusion(bestMove)
}

@ExperimentalTime
fun Position.alphabeta(ss: SS, alpha: Score, beta: Score, ply: Int, depth: Int): Int {

    val root = ply == 0

    ss[ply].pv.clear()

    if (!root) {
        if (STOP || limits.timeUp() || mr50 >= 100 || hasRepeated()) return 0
    }

    if (depth == 0) return eval()

    var bestScore = alpha
    var moveCount = 0

    for (move in genMoves()) {

        if (!makeMove(move)) continue
        val score = -alphabeta(ss, -beta, -bestScore, ply + 1, depth - 1)
        takeMove()

        moveCount++

        if (score > bestScore) {
            bestScore = score

            ss[ply].pv.build(move, ss[ply + 1].pv)

            if (score >= beta) break
        }
    }

    if (moveCount == 0) return when {
        inCheck(stm) -> -(MATE - ply)
        else         -> 0
    }

    return bestScore
}
