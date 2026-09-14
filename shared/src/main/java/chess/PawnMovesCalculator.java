package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        //check pawn color
        int coef = 1;
        ChessPiece piece = board.getPiece(position);

        //piece is BLACK
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            coef = -1;
        }
            //piece promotion probably needs to go here
            // white on black end row and black on white end row

        if ((position.getRow() == 7 && piece.getTeamColor() == ChessGame.TeamColor.BLACK) || (position.getRow() == 2 && piece.getTeamColor() == ChessGame.TeamColor.WHITE)) {

            //if space in front is open
            int moveOneRow = position.getRow() + 1 * coef;
            ChessPosition nextPositionOneMove = new ChessPosition(moveOneRow, position.getColumn());
            ChessPiece pieceInNextPositionOneMove = board.getPiece(nextPositionOneMove);
            //if double move open
            //double move creation
            int moveTwoRows = position.getRow() + 2 * coef;
            ChessPosition nextPositionTwoMoves = new ChessPosition(moveTwoRows, position.getColumn());
            ChessPiece pieceInNextPositionTwoMoves = board.getPiece(nextPositionTwoMoves);
            //add the move if its open
            if (pieceInNextPositionTwoMoves == null &&  pieceInNextPositionOneMove == null) {
                possibleMoves.add(new ChessMove(position, nextPositionTwoMoves, null));
            }
        }

        /*regular decision tree for a pawn*/
        //single move creation
        int moveOneRow = position.getRow() + 1 * coef;
        ChessPosition nextPositionOneMove = new ChessPosition(moveOneRow, position.getColumn());
        ChessPiece pieceInNextPositionOneMove = board.getPiece(nextPositionOneMove);
        //add the move if its open
        if (pieceInNextPositionOneMove == null) {
            if ((position.getRow() == 2 && piece.getTeamColor() == ChessGame.TeamColor.BLACK) || (position.getRow() == 7 && piece.getTeamColor() == ChessGame.TeamColor.WHITE)) {

                //piece promotion
                possibleMoves.add(new ChessMove(position, nextPositionOneMove, ChessPiece.PieceType.QUEEN));
                possibleMoves.add(new ChessMove(position, nextPositionOneMove, ChessPiece.PieceType.KNIGHT));
                possibleMoves.add(new ChessMove(position, nextPositionOneMove, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(position, nextPositionOneMove, ChessPiece.PieceType.BISHOP));
                //possibleMoves.add(new ChessMove(position, DiagPos, ChessPiece.PieceType.PAWN));
                //possibleMoves.add(new ChessMove(position, DiagPos, ChessPiece.PieceType.KING));

            }
            else {
                possibleMoves.add(new ChessMove(position, nextPositionOneMove, null));
            }
        }


                // row +1 * coef and look at column + and -1
                //needs to be within board
                //can't be same team
                //so different team and null is alright

        int[] nextColumns = {-1,1};

        for (int nextColumn : nextColumns) {
            int nextRow = position.getRow() + coef;
            int nextCol = position.getColumn() + nextColumn;

            if (nextRow <= 8 && nextCol <= 8 && nextRow >= 1 && nextCol >= 1) {
                ChessPosition DiagPos = new ChessPosition(nextRow, nextCol);
                if (board.getPiece(DiagPos) != null) {
                    ChessPiece pieceInDiagPos = board.getPiece(DiagPos);

                    if (pieceInDiagPos.getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //add the position
                        if ((position.getRow() == 2 && piece.getTeamColor() == ChessGame.TeamColor.BLACK) || (position.getRow() == 7 && piece.getTeamColor() == ChessGame.TeamColor.WHITE)) {

                            //piece promotion
                            possibleMoves.add(new ChessMove(position, DiagPos, ChessPiece.PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(position, DiagPos, ChessPiece.PieceType.KNIGHT));
                            possibleMoves.add(new ChessMove(position, DiagPos, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, DiagPos, ChessPiece.PieceType.BISHOP));
                            //possibleMoves.add(new ChessMove(position, DiagPos, ChessPiece.PieceType.PAWN));
                            //possibleMoves.add(new ChessMove(position, DiagPos, ChessPiece.PieceType.KING));

                        }
                        else {possibleMoves.add(new ChessMove(position, DiagPos, null));}
                    }
                    //for piece promotion

                    //position holds is off the board, holds self, or is null
                    else {
                        continue;
                    }
                } else {
                    continue;
                }

            }
        }
        return  possibleMoves;
        }
        //color A increases coef = - otherwise keep it positive
            /*if its the pawns first move*/
                //it can move twice
                //it can move once:
                //it can't move into its own piece
                //check diagonal for enemies
            //*if its the pawns second + move*/
            //it can move once:
            //it can't move into its own piece
            //check diagonal for enemies



}
