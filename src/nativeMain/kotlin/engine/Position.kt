package engine


class Position() {

    class History {
        var key: Key = 0
        var move: Move = 0
        var cr: CastlingRight = 0
        var ep: Square = 0
        var mr50: Int = 0
        var captured: Piece = 0
    }

    var pieceBB: Array<Bitboard> = Array(7) { Bitboard() }
    var colorBB: Array<Bitboard> = Array(2) { Bitboard() }
    var board: Array<Piece> = Array(64) { 0 }

    var key: Key = 0

    var stm: Color = WHITE
    var cr: CastlingRight = 0
    var ep: Square = A1
    var mr50: Int = 0
    var fullmove: Int = 0

    var histPly: Int = 0
    val hist: Array<History> = Array(256) { History() }

    var nodeCount = 0UL

    fun history(offset: Int) = hist[histPly + offset]

    constructor(fen: String) : this() {
        val tokens = fen.split(" ").iterator()

        fun addPiece(piece: Piece, sq: Square) {
            board[sq] = piece
            pieceBB[piece.type()]  = pieceBB[piece.type()]  or sq
            colorBB[piece.color()] = colorBB[piece.color()] or sq
            key = key xor PieceKeys[piece][sq]
        }

        var sq = A8
        for (c in tokens.next())
            when (c) {
                '/'         -> sq -= 16
                in '1'..'8' -> sq += c.digitToInt()
                else        -> addPiece(c.toPiece(), sq++)
            }

        stm = if (tokens.next() == "w") WHITE else BLACK

        for (c in tokens.next())
            when (c) {
                'K' -> cr = cr or WHITE_OO
                'Q' -> cr = cr or WHITE_OOO
                'k' -> cr = cr or BLACK_OO
                'q' -> cr = cr or BLACK_OOO
            }

        tokens.next().let {
            if (it != "-")
                ep = it.toSquare()
        }

        val readOrDefault = { d: Int -> if (tokens.hasNext()) tokens.next().toInt() else d }

        mr50 = readOrDefault(0)
        fullmove = readOrDefault(1)

        if (stm == WHITE) key = key xor SideKey
        if (ep != 0) key = key xor PieceKeys[0][ep]
        key = key xor CastleKeys[cr]
    }

    override fun toString(): String {
        val res = StringBuilder()

        for (rank in RANK_8 downTo RANK_1) {
            var i = 0
            for (file in FILE_A..FILE_H) {
                val piece = pieceOn(makeSquare(rank, file))
                if (piece != 0) {
                    if (i > 0) res.append(i)
                    res.append(piece.toPieceChar())
                    i = 0
                } else
                    i++
            }
            if (i > 0) res.append(i)
            if (rank != RANK_1) res.append('/')
        }

        res.append(if (stm == WHITE) " w " else " b ")

        if (cr == NO_CASTLES)
            res.append('-')
        else {
            if ((cr and WHITE_OO)  != NO_CASTLES) res.append('K')
            if ((cr and WHITE_OOO) != NO_CASTLES) res.append('Q')
            if ((cr and BLACK_OO)  != NO_CASTLES) res.append('k')
            if ((cr and BLACK_OOO) != NO_CASTLES) res.append('q')
        }

        res.append(" ${if (ep == 0) "-" else ep.toSquareString()} $mr50 $fullmove")

        return res.toString()
    }

    fun pieceOn(sq: Square) = board[sq]

    fun pieces(c: Color, pt: PieceType) = colorBB[c] and pieceBB[pt]

    fun inCheck(c: Color) = sqAttacked(pieces(c, KING).lsb(), c xor 1)

    fun sqAttacked(sq: Square, c: Color): Boolean {
        val bishops = colorBB[c] and (pieceBB[QUEEN] or pieceBB[BISHOP])
        val rooks   = colorBB[c] and (pieceBB[QUEEN] or pieceBB[ROOK])
        val occ = colorBB[WHITE] or colorBB[BLACK]

        return (pawnAttackBB(c xor 1, sq) and pieces(c, PAWN)).nonEmpty()
        || (attackBB(KNIGHT, sq) and pieces(c, KNIGHT)).nonEmpty()
        || (attackBB(KING,   sq) and pieces(c, KING)).nonEmpty()
        || (attackBB(BISHOP, sq, occ) and bishops).nonEmpty()
        || (attackBB(ROOK,   sq, occ) and rooks).nonEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Position

        return toString() == other.toString()
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }
}
