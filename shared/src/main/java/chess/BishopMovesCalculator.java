package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class BishopMovesCalculator implements PieceMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;



    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        this.board = board;
        this.position = position;

        MovesCalculatorHelper helper = new MovesCalculatorHelper(board, position);

        int[][] directions = {{1,1},{1,-1},{-1,1},{-1,-1}};

        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (int[] direction : directions) {
            helper.addMovesInDirection(possibleMoves, direction[0],direction[1]);
        }
        //return that Collection of ChessMoves
        return possibleMoves;
    }
}
