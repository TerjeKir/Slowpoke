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
    var bestMove = Move(A1, A1)

    for (depth in 1..limits.getDepth()) {
        val score = pos.minimax(0, depth)

        if (STOP || limits.timeUp()) break

        bestMove = bestMove_

        printThinking(limits, depth, score, pos.nodeCount, bestMove)

        if (MATE - abs(score) <= 2 * abs(limits.getMate())) break
    }

    while (limits.isInfinite() && !STOP) { /* Wait for stop */ }

    printConclusion(bestMove)
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