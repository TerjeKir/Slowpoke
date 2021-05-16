package engine


typealias ScoredMove = Pair<Move, Int>

class MoveList : Iterator<Move> {
    var size = 0
    private val moves = arrayOfNulls<ScoredMove>(256)

    fun add(move: Move) { moves[size++] = Pair(move, 0) }

    fun addPromos(from: Square, to: Square) {
        for (pt in KNIGHT..QUEEN)
            add(Move(from, to, PROMO, pt))
    }

    override fun next(): Move = moves[--size]!!.first
    override fun hasNext(): Boolean = size > 0
}


fun Position.genMoves(): MoveList {
    val res = MoveList()
    val occ = colorBB[stm] or colorBB[stm xor 1]
    val targets = colorBB[stm].inv()

    for (pt in KNIGHT..KING)
        for (from in pieces(stm, pt))
            for (to in targets and attackBB(pt, from, occ))
                res.add(Move(from, to))

    val (up, left, right) = when (stm) {
        WHITE -> Triple(NORTH, WEST, EAST)
        else  -> Triple(SOUTH, EAST, WEST)
    }

    val promo = BBrank1 or BBrank8
    val normal = promo.inv()

    val genPawnMoves = { moves: Bitboard, step: Direction ->
        (moves and promo).forEach { res.addPromos(it - step, it) }
        (moves and normal).forEach { res.add(Move(it - step, it)) }
    }

    val empty = occ.inv()
    val enemies = colorBB[stm xor 1]
    val pawns = pieces(stm, PAWN)

    val push = empty and pawns.shift(up)
    val double = empty and push.shift(up) and RankBB[RANK_4.relative(stm)]

    genPawnMoves(push, up)
    genPawnMoves(double, up * 2)
    genPawnMoves(enemies and pawns.shift(up + left), up + left)
    genPawnMoves(enemies and pawns.shift(up + right), up + right)

    if (inCheck(stm)) return res

    if (ep != 0)
        for (from in pawns and pawnAttackBB(stm xor 1, ep))
            res.add(Move(from, ep, ENPAS))

    fun noBlocks(vararg sqs: Square): Boolean = (Bitboard(*sqs) and occ).isEmpty()

    when (stm) {
        WHITE -> {
            if ((cr and WHITE_OO) > 0 && noBlocks(F1, G1) && !sqAttacked(F1, stm xor 1))
                res.add(Move(E1, G1, CASTLE))
            if ((cr and WHITE_OOO) > 0 && noBlocks(B1, C1, D1) && !sqAttacked(D1, stm xor 1))
                res.add(Move(E1, C1, CASTLE))
        }
        BLACK -> {
            if ((cr and BLACK_OO) > 0 && noBlocks(F8, G8) && !sqAttacked(F8, stm xor 1))
                res.add(Move(E8, G8, CASTLE))
            if ((cr and BLACK_OOO) > 0 && noBlocks(B8, C8, D8) && !sqAttacked(D8, stm xor 1))
                res.add(Move(E8, C8, CASTLE))
        }
    }

    return res
}
