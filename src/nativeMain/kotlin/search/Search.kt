package search

import engine.*
import eval
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import printConclusion
import printThinking
import kotlin.concurrent.Volatile
import kotlin.math.abs
import kotlin.math.max


@Volatile
var STOP = false

private var limits: Limits = Limits()

private const val MAX_PLY = 100


fun CoroutineScope.go(pos: Position, str: String) = launch {

    limits = Limits().apply { parse(str, pos.stm) }
    STOP = false
    pos.nodeCount = 0UL

    search(pos)
}

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

fun Position.alphabeta(ss: SS, alpha: Score, beta: Score, ply: Int, depth: Int): Score {

    val root = ply == 0

    ss[ply].pv.clear()

    if (!root) {
        if (STOP || limits.timeUp() || mr50 >= 100 || hasRepeated()) return 0

        if (ply >= MAX_PLY) return eval()
    }

    if (depth == 0) return quiescence(alpha, beta, ply)

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


fun Position.quiescence(alpha_: Int, beta: Int, ply: Int): Score {

    if (STOP || limits.timeUp()) return 0

    val eval = eval()

    if (eval >= beta || ply >= MAX_PLY)
        return eval

    var alpha = max(alpha_, eval)

    var bestScore = eval

    for (move in genMoves()) {

        if (move.promo() != QUEEN && pieceOn(move.captureSq()) == 0) continue
        if (!makeMove(move)) continue
        val score = -quiescence(-beta, -alpha, ply + 1)
        takeMove()

        if (score > bestScore) {
            bestScore = score

            if (score >= beta) break
            if (score > alpha) alpha = score
        }
    }

    return bestScore
}
