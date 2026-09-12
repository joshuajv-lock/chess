package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;

        int[] row_directions_1 = {2, -2};
        int[] col_directions_1 = {1,-1};

        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (int row_direction: row_directions_1){
            for (int col_direction: col_directions_1){
                int distance = 1;

                //find next available move
                int next_row = position.getRow() + row_direction *distance;
                int next_col = position.getColumn() + col_direction *distance;


                if ( next_row > 8 || next_col > 8 || next_row < 1 || next_col < 1 ){
                    //position is outside the board
                    continue;
                }
                else {
                    // get current chess position
                    ChessPosition nextPosition = new ChessPosition(next_row, next_col);
                    ChessPiece pieceInNextPosition = board.getPiece(nextPosition);
                    if (pieceInNextPosition == null) {
                        /*add to collection of moves*/
                        possibleMoves.add(new ChessMove(position, nextPosition, null));
                    }
                    else if (pieceInNextPosition.getTeamColor() !=null && pieceInNextPosition.getTeamColor() == board.getPiece(position).getTeamColor()) {

                        continue;

                    }
                    else {
                        /*highlight as capture potential*/
                        /*add to collection of moves*/
                        possibleMoves.add(new ChessMove(position,nextPosition,null));
                        continue;
                    }
                }

            }
        }
        int[] row_directions_2 = {1, -1};
        int[] col_directions_2 = {2,-2};

        for (int row_direction: row_directions_2){
            for (int col_direction: col_directions_2){
                int distance = 1;

                //find next available move
                int next_row = position.getRow() + row_direction *distance;
                int next_col = position.getColumn() + col_direction *distance;


                if ( next_row > 8 || next_col > 8 || next_row < 1 || next_col < 1 ){
                    //position is outside the board
                    continue;
                }
                else {
                    // get current chess position
                    ChessPosition nextPosition = new ChessPosition(next_row, next_col);
                    ChessPiece pieceInNextPosition = board.getPiece(nextPosition);
                    if (pieceInNextPosition == null) {
                        /*add to collection of moves*/
                        possibleMoves.add(new ChessMove(position, nextPosition, null));
                    }
                    else if (pieceInNextPosition.getTeamColor() !=null && pieceInNextPosition.getTeamColor() == board.getPiece(position).getTeamColor()) {

                        continue;

                    }
                    else {
                        /*highlight as capture potential*/
                        /*add to collection of moves*/
                        possibleMoves.add(new ChessMove(position,nextPosition,null));
                        continue;
                    }
                }

            }
        }
        return possibleMoves;

    }
}
