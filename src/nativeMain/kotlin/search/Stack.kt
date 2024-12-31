package search

import engine.*


typealias SS = Array<Stack>

class Stack {
    val pv = PV()
}

value class PV(private val pv: MutableList<Move>) {

    constructor() : this(mutableListOf())

    fun clear() = pv.clear()

    fun build(move: Move, continuation: PV) {
        pv.clear()
        pv.add(move)
        pv.addAll(continuation.pv)
    }

    operator fun get(i: Int) = pv[i]

    override fun toString() = pv.joinToString(" ") { it.toUCI() }
}
