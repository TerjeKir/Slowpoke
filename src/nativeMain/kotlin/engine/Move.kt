package engine


value class Move(val move: UShort) {

    constructor(from: Square, to: Square, mt: MoveType = NORMAL, promo: PieceType = KNIGHT) :
        this((mt or ((promo - KNIGHT).type shl 12) or (from shl 6) or to).toUShort())

    fun from(): Square = (move.toInt() ushr 6) and 0x3F
    fun to(): Square = move.toInt() and 0x3F
    fun type(): MoveType = move.toInt() and (3 shl 14)
    fun promo(): PieceType = PieceType(KNIGHT.type + ((move.toInt() ushr 12) and 3))

    fun captureSq(): Square = if (type() != ENPAS) to() else to() xor 8

    fun toUCI(): String {
        val promo = if (type() == PROMO) promo().toPieceChar() else ""
        return from().toSquareString() + to().toSquareString() + promo
    }

    companion object {
        fun noMove() = Move(0U)
    }
}
typealias MoveType = Int

const val NORMAL = 0
const val PROMO  = 1 shl 14
const val ENPAS  = 2 shl 14
const val CASTLE = 3 shl 14


fun String.toMove(pos: Position): Move {
    val tokens = this.chunked(2).iterator()

    val from = tokens.next().toSquare()
    val to = tokens.next().toSquare()
    val pt = pos.pieceOn(from).type

    return if (tokens.hasNext())
        Move(from, to, PROMO, tokens.next()[0].toPiece().type)
    else if (pt == KING && from.distanceTo(to) > 1)
        Move(from, to, CASTLE)
    else if (pt == PAWN && from.file() != to.file() && pos.pieceOn(to) == Piece.empty())
        Move(from, to, ENPAS)
    else
        Move(from, to)
}
