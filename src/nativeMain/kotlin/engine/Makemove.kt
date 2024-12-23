package engine

import kotlin.experimental.ExperimentalNativeApi


private fun Position.togglePiece(piece: Piece, sq: Square) {
    board[sq] = board[sq] xor piece
    pieceBB[piece.type()] = pieceBB[piece.type()] xor sq
    colorBB[piece.color()] = colorBB[piece.color()] xor sq
    key = key xor PieceKeys[piece][sq]
}

private fun Position.movePiece(piece: Piece, from: Square, to: Square) {
    togglePiece(piece, from)
    togglePiece(piece, to)
}

fun Position.makeMove(move: Move): Boolean {

    val from = move.from()
    val to = move.to()
    val mover = pieceOn(from)
    val captSq = move.captureSq()
    val captured = pieceOn(captSq)

    hist[histPly].key = key
    hist[histPly].move = move
    hist[histPly].cr = cr
    hist[histPly].ep = ep
    hist[histPly].mr50 = mr50
    hist[histPly].captured = captured

    key = key xor CastleKeys[cr]
    cr = cr and CastlePerm[from] and CastlePerm[to]
    key = key xor CastleKeys[cr]

    if (ep != 0) key = key xor PieceKeys[0][ep]
    ep = 0

    mr50++
    histPly++
    nodeCount++

    if (captured != 0) {
        mr50 = 0
        togglePiece(captured, captSq)
    }

    movePiece(mover, from, to)

    if (mover.type() == PAWN) {
        mr50 = 0

        if (from xor to == 16) {
            ep = to xor 8
            key = key xor PieceKeys[0][ep]
        } else if (move.type() == PROMO) {
            togglePiece(pieceOn(to), to)
            togglePiece(makePiece(stm, move.promo()), to)
        }
    } else if (move.type() == CASTLE)
        when (to) {
            G1 -> movePiece(makePiece(WHITE, ROOK), H1, F1)
            C1 -> movePiece(makePiece(WHITE, ROOK), A1, D1)
            G8 -> movePiece(makePiece(BLACK, ROOK), H8, F8)
            C8 -> movePiece(makePiece(BLACK, ROOK), A8, D8)
        }

    key = key xor SideKey
    stm = stm xor 1
    fullmove += stm

    if (inCheck(stm xor 1)) {
        takeMove()
        return false
    }

    return true
}

@OptIn(ExperimentalNativeApi::class)
fun Position.takeMove() {

    histPly--
    fullmove -= stm
    stm = stm xor 1

    val move = history(0).move
    val from = move.from()
    val to = move.to()
    val mover = pieceOn(to)
    val captured = history(0).captured

    if (move.type() == CASTLE) {
        assert(mover.type() == KING)
        when (to) {
            G1 -> movePiece(makePiece(WHITE, ROOK), F1, H1)
            C1 -> movePiece(makePiece(WHITE, ROOK), D1, A1)
            G8 -> movePiece(makePiece(BLACK, ROOK), F8, H8)
            C8 -> movePiece(makePiece(BLACK, ROOK), D8, A8)
        }
    }

    assert(mover.color() in BLACK..WHITE)
    assert(mover.type() in PAWN..KING)
    movePiece(mover, to, from)

    if (captured != 0) {
        assert(captured.color() in BLACK..WHITE)
        assert(captured.type() in PAWN..QUEEN)
        togglePiece(captured, move.captureSq())
    }

    if (move.type() == PROMO) {
        assert(mover != PAWN && mover != KING)
        togglePiece(mover, from)
        togglePiece(makePiece(stm, PAWN), from)
    }

    key  = history(0).key
    cr   = history(0).cr
    ep   = history(0).ep
    mr50 = history(0).mr50
}
