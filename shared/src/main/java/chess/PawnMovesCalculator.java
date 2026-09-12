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
        int pieceMoveTracker = 1;
        ChessPiece piece = board.getPiece(position);
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            coef = -1;
        }
        if (pieceMoveTracker > 1) {
            //piece can move two pieces forward

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


        return List.of();
    }
}
