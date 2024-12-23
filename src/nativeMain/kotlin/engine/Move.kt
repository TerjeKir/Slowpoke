package engine


typealias Move = Short
typealias MoveType = Int
const val NORMAL = 0
const val PROMO  = 1 shl 14
const val ENPAS  = 2 shl 14
const val CASTLE = 3 shl 14

fun Move(from: Square, to: Square, mt: MoveType = NORMAL, promo: PieceType = KNIGHT): Move =
    (mt or ((promo - KNIGHT) shl 12) or (from shl 6) or to).toShort()

fun Move.from(): Square = (this.toInt() ushr 6) and 0x3F
fun Move.to(): Square = this.toInt() and 0x3F
fun Move.type(): MoveType = this.toInt() and (3 shl 14)
fun Move.promo(): PieceType = KNIGHT + ((this.toInt() ushr 12) and 3)

fun Move.captureSq(): PieceType = if (type() != ENPAS) to() else to() xor 8

fun Move.toUCI(): String {
    val promo = if (type() == PROMO) promo().toPieceChar() else ""
    return from().toSquareString() + to().toSquareString() + promo
}

fun String.toMove(pos: Position): Move {
    val tokens = this.chunked(2).iterator()

    val from = tokens.next().toSquare()
    val to = tokens.next().toSquare()
    val pt = pos.pieceOn(from).type()

    return if (tokens.hasNext())
        Move(from, to, PROMO, tokens.next()[0].toPiece().type())
    else if (pt == KING && from.distanceTo(to) > 1)
        Move(from, to, CASTLE)
    else if (pt == PAWN && from.file() != to.file() && pos.pieceOn(to) == 0)
        Move(from, to, ENPAS)
    else
        Move(from, to)
}
