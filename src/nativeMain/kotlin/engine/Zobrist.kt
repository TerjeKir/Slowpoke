package engine

import kotlin.math.min
import kotlin.random.Random
import kotlin.random.nextULong

value class Key(val key: ULong) {

    infix fun xor(other: Key) = Key(key.xor(other.key))

    companion object {
        fun zero() = Key(0UL)
    }
}

val rng = Random(0)

val SideKey    = Key(rng.nextULong())
val CastleKeys = Array(16) { Key(rng.nextULong()) }
val PieceKeys  = Array(16) { Array(64) { Key(rng.nextULong()) } }

fun Position.hasRepeated(): Boolean {
    for (i in 4..min(mr50, histPly) step 2)
        if (key == history(-i).key)
            return true
    return false
}
