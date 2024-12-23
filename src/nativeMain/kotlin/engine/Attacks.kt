package engine

import kotlin.experimental.ExperimentalNativeApi


private val PawnAttacks = Array(2) { color -> Array(64) { sq ->
    pawnBBAttackBB(color, Bitboard(sq))
}}

private val PseudoAttacks = Array(8) { pt -> Array(64) { sq ->

    val kSteps = intArrayOf( -9, -8, -7, -1,  1,  7,  8,  9)
    val nSteps = intArrayOf(-17,-15,-10, -6,  6, 10, 15, 17)

    when (pt) {
        KNIGHT -> nSteps
        KING   -> kSteps
        else   -> IntArray(0)
    }.fold(Bitboard()) { bb, step -> bb or landingSquareBB(sq, step) }
}}


fun landingSquareBB(sq: Square, step: Direction): Bitboard {
    val to = sq + step
    val onTheBoard = to.isValidSquare() && sq.distanceTo(to) <= 2
    return if (onTheBoard) Bitboard(to) else Bitboard()
}

@OptIn(ExperimentalNativeApi::class)
fun attackBB(pt: PieceType, sq: Square, occ: Bitboard = Bitboard()): Bitboard {

    assert(pt in KNIGHT..KING)

    return when (pt) {
        BISHOP -> BMagics[sq].attackBB(occ)
        ROOK   -> RMagics[sq].attackBB(occ)
        QUEEN  -> attackBB(BISHOP, sq, occ) or attackBB(ROOK, sq, occ)
        else   -> PseudoAttacks[pt][sq]
    }
}

fun pawnAttackBB(color: Color, sq: Square): Bitboard {
    return PawnAttacks[color][sq]
}

fun pawnBBAttackBB(color: Color, pawns: Bitboard): Bitboard {
    val up = if (color == WHITE) NORTH else SOUTH
    return pawns.shift(up + WEST) or pawns.shift(up + EAST)
}
