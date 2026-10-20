package chess;

import java.util.Collection;

public class MovesCalculatorHelper {

    private ChessBoard board;
    private ChessPosition position;

    public MovesCalculatorHelper (ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public void addMovesInDirection(Collection<ChessMove> possibleMoves, int rowDirection, int colDirection){
        int distance = 1;
        while (true) {

            //find next available move
            int nextRow = position.getRow() + rowDirection * distance;
            int nextCol = position.getColumn() + colDirection * distance;


            if (nextRow > 8 || nextCol > 8 || nextRow < 1 || nextCol < 1) {
                //position is outside the board
                return;
            }

            // get current chess position
            ChessPosition nextPosition = new ChessPosition(nextRow, nextCol);
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

    public void addMovesInDirectionSingleMove(Collection<ChessMove> possibleMoves, int rowDirection, int colDirection) {

        //find next available move
        int nextRow = position.getRow() + rowDirection;
        int nextCol = position.getColumn() + colDirection;


        if (nextRow > 8 || nextCol > 8 || nextRow < 1 || nextCol < 1) {
            //position is outside the board
            return;
        }

        // get current chess position
        ChessPosition nextPosition = new ChessPosition(nextRow, nextCol);
        ChessPiece pieceInNextPosition = board.getPiece(nextPosition);
        if (pieceInNextPosition == null) {
            /*add to collection of moves*/
            possibleMoves.add(new ChessMove(position, nextPosition, null));
            return;
        }
        if (pieceInNextPosition.getTeamColor() != null
                && pieceInNextPosition.getTeamColor() ==
                board.getPiece(position).getTeamColor()) {

            return;

        }

        /*highlight as capture potential*/
        /*add to collection of moves*/
        possibleMoves.add(new ChessMove(position, nextPosition, null));
        return;
    }


}
