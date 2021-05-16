package engine


val BBfileA = Bitboard(0x0101010101010101UL)
val BBfileH = Bitboard(0x8080808080808080UL)
val FileBB = Array(8) { BBfileA shl it }

val BBrank1 = Bitboard(0x00000000000000FFUL)
val BBrank8 = Bitboard(0xFF00000000000000UL)
val RankBB = Array(8) { BBrank1 shl (it * NORTH) }


data class Bitboard(val bb: ULong) : Iterable<Square> {

    constructor() : this(0UL)
    constructor(sq: Square) : this(1UL shl sq)
    constructor(vararg sqs: Square) : this(sqs.fold(0UL) { bb, it -> bb or (1UL shl it) })

    fun isEmpty(): Boolean = bb == 0UL
    fun nonEmpty(): Boolean = !isEmpty()
    fun popcnt(): Int = bb.countOneBits()
    fun lsb(): Square = bb.countTrailingZeroBits()

    fun shift(dir: Direction): Bitboard {
        val res = when (dir and 7) {
            1    -> this and BBfileH.inv()
            7    -> this and BBfileA.inv()
            else -> this
        }
        return if (dir > 0)
            res shl dir
        else
            res shr -dir
    }

    override fun iterator(): Iterator<Square> {
        class BBIterator(var ul: ULong) : Iterator<Square> {
            fun poplsb(): Square {
                val lsb = ul.countTrailingZeroBits()
                ul = ul and (ul - 1UL)
                return lsb
            }

            override fun hasNext(): Boolean = ul != 0UL
            override fun next(): Square = poplsb()
        }
        return BBIterator(this.bb)
    }

    // Bitwise operators
    infix fun shl(other: Int): Bitboard = Bitboard(bb shl other)
    infix fun shr(other: Int): Bitboard = Bitboard(bb shr other)
    infix fun or(other: Bitboard): Bitboard = Bitboard(bb or other.bb)
    infix fun and(other: Bitboard): Bitboard = Bitboard(bb and other.bb)
    infix fun xor(other: Bitboard): Bitboard = Bitboard(bb xor other.bb)
    operator fun minus(other: Bitboard): Bitboard = Bitboard(bb - other.bb)
    infix fun or(sq: Square): Bitboard = Bitboard(bb or (1UL shl sq))
    infix fun and(sq: Square): Bitboard = Bitboard(bb and (1UL shl sq))
    infix fun xor(sq: Square): Bitboard = Bitboard(bb xor (1UL shl sq))
    fun inv(): Bitboard = Bitboard(bb.inv())

    // Print bitboards as an 8x8 grid of 0s and 1s
    override fun toString(): String {

        fun Boolean.toInt(): Int = if (this) 1 else 0

        val res = StringBuilder()
        for (rank in RANK_8 downTo RANK_1) {
            for (file in FILE_A..FILE_H) {
                res.append((this and Bitboard(makeSquare(rank, file))).nonEmpty().toInt())
            }
            res.append("\n")
        }
        return res.toString()
    }
}
