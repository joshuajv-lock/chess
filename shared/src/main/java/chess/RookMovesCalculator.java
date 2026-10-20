package chess;


import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class RookMovesCalculator implements PieceMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        this.board = board;
        this.position = position;
        
        int[][] directions = {{0,1},{0,-1},{1,0},{-1,0}};

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        MovesCalculatorHelper helper = new MovesCalculatorHelper(board, position);

        for (int[] direction : directions) {
            helper.addMovesInDirection(possibleMoves, direction[0],direction[1]);
        }
        //return that Collection of ChessMoves
        return possibleMoves;
    }
}
