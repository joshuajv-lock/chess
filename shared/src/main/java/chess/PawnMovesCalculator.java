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

        //piece is white
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            coef = -1;

            //piece promotion probably needs to go here

            if ((position.getRow() == 7 && piece.getTeamColor() == ChessGame.TeamColor.WHITE) || (position.getRow() == 2 && piece.getTeamColor() == ChessGame.TeamColor.BLACK)) {

                //if double move open
                //double move creation
                int moveTwoRows = position.getRow() +2 * coef;
                ChessPosition nextPositionTwoMoves = new ChessPosition(moveTwoRows, position.getColumn());
                ChessPiece pieceInNextPositionTwoMoves = board.getPiece(nextPositionTwoMoves);
                //add the move if its open
                if (pieceInNextPositionTwoMoves == null) {
                    possibleMoves.add(new ChessMove(position, nextPositionTwoMoves, null));
                }
            else if ((position.getRow() == 1 && piece.getTeamColor() == ChessGame.TeamColor.WHITE) || (position.getRow() == 8 && piece.getTeamColor() == ChessGame.TeamColor.BLACK)) {

                //piece promotion
                return null;
            }
            else /*regular decision tree for a pawn*/ {
                //single move creation
                int moveOneRow = position.getRow() + 1 * coef;
                ChessPosition nextPositionOneMove = new ChessPosition(moveOneRow, position.getColumn());
                ChessPiece pieceInNextPositionOneMove = board.getPiece(nextPositionOneMove);
                //add the move if its open
                if (pieceInNextPositionOneMove == null) {
                    possibleMoves.add(new ChessMove(position, nextPositionOneMove, null));
                }

                    // row +1 * coef and look at column + and -1
                    //needs to be within board
                    //can't be same team
                    //so different team and null is alright

                int[] nextColumns = {-1,1};

                for (int nextColumn : nextColumns) {
                    int nextRow = position.getRow() + coef;
                    ChessPosition DiagPos = new ChessPosition(nextRow, nextColumn);
                    ChessPiece pieceInDiagPos = board.getPiece(DiagPos);
                    if (pieceInDiagPos.getTeamColor() != board.getPiece(position).getTeamColor() && pieceInDiagPos.getTeamColor() != null && DiagPos.getRow() < 8 && DiagPos.getColumn() < 8 && DiagPos.getRow() > 0 && DiagPos.getColumn() > 0) {
                        //add the position

                        possibleMoves.add(new ChessMove(position, DiagPos, null));
                    }
                    //position holds is off the board, holds self, or is null
                    else {
                        continue;
                    }
                }
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
