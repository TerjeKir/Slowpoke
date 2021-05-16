@file:Suppress("SpellCheckingInspection")

package engine

import kotlin.math.absoluteValue
import kotlin.math.max


const val STARTFEN: String = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
const val KIWIPETE: String = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"

typealias Color = Int
const val WHITE: Color = 1
const val BLACK: Color = 0

typealias Score = Int
fun Score.mateScore() = when {
        this > 0 ->  ((MATE - this) / 2) + 1
        else     -> -((MATE + this) / 2)
}
fun Score.toUCI(): String = when (this.absoluteValue) {
    in MATE_IN_MAX..MATE -> "mate ${this.mateScore()}"
    else                 -> "cp $this"
}
const val MATE: Score = 30000
const val MATE_IN_MAX: Score = MATE - 999
const val INF: Score = MATE + 1

typealias PieceType = Int
const val PAWN: PieceType = 1
const val KNIGHT: PieceType = 2
const val BISHOP: PieceType = 3
const val ROOK: PieceType = 4
const val QUEEN: PieceType = 5
const val KING: PieceType = 6

typealias Piece = Int
fun makePiece(c: Color, pt: PieceType): Piece = (c shl 3) + pt
fun Piece.color(): Color = this shr 3
fun Piece.type(): PieceType = this and 7
fun Piece.toPieceChar(): Char = ".pnbrqk..PNBRQK"[this]
fun Char.toPiece(): Piece = ".pnbrqk..PNBRQK".indexOf(this)

typealias Direction = Int
const val NORTH = 8
const val EAST  = 1
const val SOUTH = -8
const val WEST  = -1

typealias CastlingRight = Int
const val WHITE_OO: CastlingRight = 1
const val WHITE_OOO: CastlingRight = 2
const val BLACK_OO: CastlingRight = 4
const val BLACK_OOO: CastlingRight = 8
const val NO_CASTLES: CastlingRight = 0
const val ALL_CASTLES: CastlingRight = 15

val CastlePerm = intArrayOf(
    13, 15, 15, 15, 12, 15, 15, 14,
    15, 15, 15, 15, 15, 15, 15, 15,
    15, 15, 15, 15, 15, 15, 15, 15,
    15, 15, 15, 15, 15, 15, 15, 15,
    15, 15, 15, 15, 15, 15, 15, 15,
    15, 15, 15, 15, 15, 15, 15, 15,
    15, 15, 15, 15, 15, 15, 15, 15,
     7, 15, 15, 15,  3, 15, 15, 11,
)

typealias Rank = Int
fun Square.rank() = this shr 3
fun Char.toRank(): Int = code - '1'.code
fun Rank.rankToChar(): Char = (this + '1'.code).toChar()
fun Rank.relative(c: Color): Rank = if (c == WHITE) this else RANK_8 - this
const val RANK_1 = 0
const val RANK_4 = 3
const val RANK_8 = 7

typealias File = Int
fun Square.file() = this and 7
fun Char.toFile(): Int = code - 'a'.code
fun File.fileToChar(): Char = (this + 'a'.code).toChar()
const val FILE_A = 0
const val FILE_H = 7

typealias Square = Int
fun makeSquare(rank: Int, file: Int) = rank * 8 + file
fun Square.isValidSquare(): Boolean = this in A1..H8
fun Square.distanceTo(sq: Square): Int = Distance[this][sq]
fun Square.toSquareString(): String = "${this.file().fileToChar()}${this.rank().rankToChar()}"
fun String.toSquare(): Square = makeSquare(this[1].toRank(), this[0].toFile())
val Distance = Array(64) { sq1 -> Array(64) { sq2 ->
    val vertical = (sq1.file() - sq2.file()).absoluteValue
    val horizontal = (sq1.rank() - sq2.rank()).absoluteValue
    max(vertical, horizontal)
}}
const val A1: Square = 0
const val B1: Square = 1
const val C1: Square = 2
const val D1: Square = 3
const val E1: Square = 4
const val F1: Square = 5
const val G1: Square = 6
const val H1: Square = 7
const val A2: Square = 8
const val B2: Square = 9
const val C2: Square = 10
const val D2: Square = 11
const val E2: Square = 12
const val F2: Square = 13
const val G2: Square = 14
const val H2: Square = 15
const val A3: Square = 16
const val B3: Square = 17
const val C3: Square = 18
const val D3: Square = 19
const val E3: Square = 20
const val F3: Square = 21
const val G3: Square = 22
const val H3: Square = 23
const val A4: Square = 24
const val B4: Square = 25
const val C4: Square = 26
const val D4: Square = 27
const val E4: Square = 28
const val F4: Square = 29
const val G4: Square = 30
const val H4: Square = 31
const val A5: Square = 32
const val B5: Square = 33
const val C5: Square = 34
const val D5: Square = 35
const val E5: Square = 36
const val F5: Square = 37
const val G5: Square = 38
const val H5: Square = 39
const val A6: Square = 40
const val B6: Square = 41
const val C6: Square = 42
const val D6: Square = 43
const val E6: Square = 44
const val F6: Square = 45
const val G6: Square = 46
const val H6: Square = 47
const val A7: Square = 48
const val B7: Square = 49
const val C7: Square = 50
const val D7: Square = 51
const val E7: Square = 52
const val F7: Square = 53
const val G7: Square = 54
const val H7: Square = 55
const val A8: Square = 56
const val B8: Square = 57
const val C8: Square = 58
const val D8: Square = 59
const val E8: Square = 60
const val F8: Square = 61
const val G8: Square = 62
const val H8: Square = 63