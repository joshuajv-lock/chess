package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;

    private void addMovesInDirection(Collection<ChessMove> possibleMoves, int rowDirection, int colDirection){
        int distance = 1;
        while (true) {

            //find next available move
            int next_row = position.getRow() + rowDirection * distance;
            int next_col = position.getColumn() + colDirection * distance;


            if (next_row > 8 || next_col > 8 || next_row < 1 || next_col < 1) {
                //position is outside the board
                return;
            }

            // get current chess position
            ChessPosition nextPosition = new ChessPosition(next_row, next_col);
            ChessPiece pieceInNextPosition = board.getPiece(nextPosition);
            if (pieceInNextPosition == null) {
                /*add to collection of moves*/
                possibleMoves.add(new ChessMove(position, nextPosition, null));
                distance += 1;
                continue;
            }
            if (pieceInNextPosition.getTeamColor() !=null
                    && pieceInNextPosition.getTeamColor() ==
                    board.getPiece(position).getTeamColor()) {

                return;

            }

            /*highlight as capture potential*/
            /*add to collection of moves*/
            possibleMoves.add(new ChessMove(position,nextPosition,null));
            return;
        }

    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        this.board = board;
        this.position = position;

        int[][] directions = {{0,1},{0,-1},{1,0},{-1,0},
                {1,1},{1,-1},{-1,1},{-1,-1}};

        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (int[] direction : directions) {
            addMovesInDirection(possibleMoves, direction[0],direction[1]);
        }
        //return that Collection of ChessMoves
        return possibleMoves;
    }
}
