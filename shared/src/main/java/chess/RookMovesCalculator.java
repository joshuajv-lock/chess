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

        int[] row_directions = {0};
        int[] col_directions = {1,-1};

        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (int row_direction: row_directions){
            for (int col_direction: col_directions){
                boolean expand = true;
                int distance = 1;
                while (expand) {

                    //find next available move
                    int next_row = position.getRow() + row_direction *distance;
                    int next_col = position.getColumn() + col_direction *distance;


                    if ( next_row > 8 || next_col > 8 || next_row < 1 || next_col < 1 ){
                        //position is outside the board
                        expand = false;
                    }
                    else {
                        // get current chess position
                        ChessPosition nextPosition = new ChessPosition(next_row, next_col);
                        ChessPiece pieceInNextPosition = board.getPiece(nextPosition);
                        if (pieceInNextPosition == null) {
                            /*add to collection of moves*/
                            possibleMoves.add(new ChessMove(position, nextPosition, null));
                            distance += 1;
                        }
                        else if (pieceInNextPosition.getTeamColor() !=null && pieceInNextPosition.getTeamColor() == board.getPiece(position).getTeamColor()) {

                            expand = false;

                        }
                        else {
                            /*highlight as capture potential*/
                            /*add to collection of moves*/
                            possibleMoves.add(new ChessMove(position,nextPosition,null));
                            expand = false;
                        }
                    }
                }

            }
        }
        int[] row_directions_2 = {1,-1};
        int[] col_directions_2 = {0};


        for (int row_direction: row_directions_2){
            for (int col_direction: col_directions_2){
                boolean expand = true;
                int distance = 1;
                while (expand) {

                    //find next available move
                    int next_row = position.getRow() + row_direction *distance;
                    int next_col = position.getColumn() + col_direction *distance;


                    if ( next_row > 8 || next_col > 8 || next_row < 1 || next_col < 1 ){
                        //position is outside the board
                        expand = false;
                    }
                    else {
                        // get current chess position
                        ChessPosition nextPosition = new ChessPosition(next_row, next_col);
                        ChessPiece pieceInNextPosition = board.getPiece(nextPosition);
                        if (pieceInNextPosition == null) {
                            /*add to collection of moves*/
                            possibleMoves.add(new ChessMove(position, nextPosition, null));
                            distance += 1;
                        }
                        else if (pieceInNextPosition.getTeamColor() !=null && pieceInNextPosition.getTeamColor() == board.getPiece(position).getTeamColor()) {

                            expand = false;

                        }
                        else {
                            /*highlight as capture potential*/
                            /*add to collection of moves*/
                            possibleMoves.add(new ChessMove(position,nextPosition,null));
                            expand = false;
                        }
                    }
                }

            }
        }
        //return that Collection of ChessMoves
        return possibleMoves;
    }
}
