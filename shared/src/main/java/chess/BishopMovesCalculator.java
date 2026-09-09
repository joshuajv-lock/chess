package chess;

import java.util.Collection;

import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        //now we figure out the valid bishop destinations
        //get bishop starting position
        this.board = board;
        this.position = position;
        //travel out one square at a time
        //you need to define these four directions so a four loop can move through them
        //row +1, col +1
        //row +1, col -1
        //row -1, col +1
        //row -1, col -1

        //for (each direction, check if its valid, move onto the next direction,)
            //boolean expand = true;

        //A bishop cannot:
            //leave the 8x8 grid
            //land on its own color piece
            //pass through another piece in its path

        //a bishop must
            //move diagonally

        //if a bishop encounters another piece it must:
            //stop and capture the piece then replace that piece

        //build ChessMoves objects

        //return that Collection of ChessMoves
        return List.of();

    }
