import engine.*
import kotlin.time.ExperimentalTime
import kotlin.concurrent.thread


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

fun uci() {
    println("""
        id name Slowpoke 0.1
        id author Terje Kirstihagen
        uciok
        """.trimIndent())
}

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
