import engine.*
import search.*
import kotlin.time.ExperimentalTime
import kotlin.concurrent.thread


@ExperimentalTime
fun main() {
    var pos = Position(STARTFEN)

    while (true) {
        val input = readLine() ?: break
        when (input.substringBefore(' ')) {
            "go"         -> thread { go(pos, input) }
            "uci"        -> uci()
            "isready"    -> println("readyok")
            "position"   -> pos = position(input)
            "setoption"  -> setOption(input)
            "ucinewgame" -> {} // TODO("Reset TT")
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
                copyMake(it.toMove(this))
            }
    }
}

// TODO(Actually set options)
fun setOption(str: String) {
    val (name, value) = str.split("name ")[1].split(" value ")
    println("$name: $value")

}

@ExperimentalTime
fun printThinking(limits: Limits, depth: Int, score: Score, nodes: ULong, bm: Move) {
    val elapsed = limits.elapsed().inWholeMilliseconds.coerceAtLeast(1)
    val nps = nodes * 1000U / elapsed.toULong()
    println("info depth $depth score ${score.toUCI()} time $elapsed nodes $nodes nps $nps pv ${bm.toUCI()}")
}

fun printConclusion(bm: Move) {
    println("bestmove ${bm.toUCI()}")
}
