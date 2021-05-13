import engine.*


val TypeValues: IntArray = intArrayOf(0, 100, 300, 325, 550, 1000, 0, 0)
val PieceValues: IntArray = IntArray(16) { i -> TypeValues[i % 8] * if (i.color() == BLACK) -1 else 1 }


fun Position.eval(): Int {
    var res = 0
    for (c in BLACK..WHITE)
        for (pt in PAWN..QUEEN)
            res += pieces(c, pt).popcnt() * PieceValues[makePiece(c, pt)]

    return if (stm == WHITE) res else -res
}