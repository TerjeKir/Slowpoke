import engine.*
import misc.perft
import search.Limits
import search.PV
import search.STOP
import search.go
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


fun main() {
    val searchScope = CoroutineScope(Dispatchers.Default)

    var pos = Position(STARTFEN)

    while (true) {
        val input = readlnOrNull() ?: break
        when (input.substringBefore(' ')) {
            "go"         -> searchScope.go(pos, input)
            "uci"        -> uci()
            "isready"    -> println("readyok")
            "position"   -> pos = position(input)
            "setoption"  -> setOption(input)
            "ucinewgame" -> {}
            "stop"       -> STOP = true
            "quit"       -> break

            "eval"  -> println(pos.eval())
            "print" -> println(pos)
            "perft" -> perft(input)
        }
    }
}

fun uci() {
    println("""
        id name Slowpoke 0.1
        id author Terje Kirstihagen
        uciok
    """.trimIndent())
}

fun position(str: String): Position {
    val fen = when {
        "fen" in str -> str.split(" ", limit = 3)[2]
        else -> STARTFEN
    }

    return Position(fen).apply {
        if (str.contains("moves "))
            str.split("moves ")[1].split(" ").forEach {
                makeMove(it.toMove(this))
                if (mr50 == 0) histPly = 0
            }
    }
}

// TODO(Actually set options)
fun setOption(str: String) {
    val (name, value) = str.split("name ")[1].split(" value ")
    println("$name: $value")

}

fun printThinking(limits: Limits, depth: Int, score: Score, nodes: ULong, pv: PV) {
    val elapsed = limits.elapsed().inWholeMilliseconds.coerceAtLeast(1)
    val nps = nodes * 1000U / elapsed.toULong()
    println(
        "info " +
        "depth $depth " +
        "score ${score.toUCI()} " +
        "time $elapsed " +
        "nodes $nodes " +
        "nps $nps " +
        "pv $pv"
    )
}

fun printConclusion(bm: Move) {
    println("bestmove ${bm.toUCI()}")
}
