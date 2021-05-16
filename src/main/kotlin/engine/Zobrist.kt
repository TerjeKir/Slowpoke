package engine

import kotlin.math.min
import kotlin.random.Random

typealias Key = Long

val rng = Random(0)

val SideKey    = rng.nextLong()
val CastleKeys = Array(16) { rng.nextLong() }
val PieceKeys  = Array(16) { LongArray(64) { rng.nextLong() } }

fun Position.hasRepeated(): Boolean {
    for (i in 4..min(mr50, histPly) step 2)
        if (key == history(-i).key)
            return true
    return false
}
