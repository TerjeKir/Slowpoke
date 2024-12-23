package search

import engine.*


typealias SS = Array<Stack>

class Stack {
    val pv = PV()
}

class PV {
    var len = 0
    var moves = ShortArray(100)

    fun clear() { len = 0 }

    fun build(move: Move, pv: PV) {
        moves[0] = move
        append(pv)
    }

    private fun append(other: PV) {
        len = 1 + other.len
        for (i in 0..other.len)
            moves[i+1] = other.moves[i]
    }

    override fun toString(): String {
        val res = StringBuilder()
        for (i in 0 until len)
            res.append(moves[i].toUCI() + " ")
        return res.dropLast(1).toString()
    }
}
