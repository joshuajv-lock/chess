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
        int[] row_directions = {1,-1};
        int[] col_directions = {1,-1};
        for (int row_direction: row_directions){
            for (int col_direction: col_directions){
                boolean expand = true;
                int distance = 1;
                while (expand) {

                    //find next available move
                    int next_row = position.getRow() + row_direction *distance;
                    int next_col = position.getColumn() + col_direction *distance;
                    distance += 1;

                    if ( next_row > 8 || next_col > 8 || next_row < 1 || next_col < 1 ){
                        //position is outside the board
                        expand = false;
                    }
                    else {
                        // get current chess position
                        ChessPosition nextPosition = new ChessPosition(next_row, next_col);
                        ChessPiece pieceInNextPosition = board.getPiece(nextPosition);
                        if (/*square is empty*/) {
                            /*add to collection of moves*/
                            position = nextPosition
                        }
                        else if (/*square holds own color*/) {
                            expand = false;
                        }
                        else /*square holds enemy color*/ {
                            /*highlight as capture potential*/
                            /*add to collection of moves*/
                            position = nextPosition
                        }
                    }
                }

            }
        }
        //return that Collection of ChessMoves
        return List.of();
    }
}
