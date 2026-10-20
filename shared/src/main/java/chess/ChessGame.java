package chess;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        myBoard.resetBoard();
        turnCounter = 0;
    }

    ChessBoard myBoard = new ChessBoard();

    int turnCounter = 0;

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        //white starts
        // return team color
        // make move counter starts at 0 increases 1 every time
        // if its 0 or even then its whites turn
        if (turnCounter % 2 == 0) {
            return TeamColor.WHITE;
        }
        else {
            return TeamColor.BLACK;
        }
        // if its odd its blacks turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (getTeamTurn() != team) {
            turnCounter ++;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        ChessPiece pieceInPosition = myBoard.getPiece(startPosition);
        if (pieceInPosition == null) {
            return null;
        }
        else {
            Collection<ChessMove> possiblyValidMoves = pieceInPosition.pieceMoves(myBoard,startPosition);
            Iterator<ChessMove> possiblyValidMovesIterator = possiblyValidMoves.iterator();
            while (possiblyValidMovesIterator.hasNext()) {
                ChessMove move = possiblyValidMovesIterator.next();
                TeamColor pieceColor = pieceInPosition.getTeamColor();
                ChessPosition possibleFinalPosition = move.getEndPosition();
                ChessPiece possibleFinalPositionPiece = myBoard.getPiece(possibleFinalPosition);
                //temp board
                ChessBoard tempBoard = myBoard;
                tempBoard.addPiece(startPosition, null);
                tempBoard.addPiece(possibleFinalPosition, pieceInPosition);

                boolean checkChecker = isInCheck(pieceInPosition.getTeamColor());


                if (checkChecker) {
                    possiblyValidMovesIterator.remove();
                }
                //reset the board
                tempBoard.addPiece(startPosition, pieceInPosition);
                tempBoard.addPiece(possibleFinalPosition, possibleFinalPositionPiece);

            }
            return possiblyValidMoves;
        }

        //for (ChessMove move : possiblyValidMoves) {}


        // i need the enemies moves and need to see if moving the king from startPosition to the next position opens my door for check.






        //this would need to access the moves calculators and return the possibleMoves lists but editing out the ones that put the king in check;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //update start and end position for myBoard;
        if (move == null) {
            throw new InvalidMoveException ("that move is null");
        }
        ChessPosition moveStartPosition = move.getStartPosition();
        if (moveStartPosition == null) {
            throw new InvalidMoveException("the startPosition given was null");
        }
        ChessPiece myPiece = myBoard.getPiece(moveStartPosition);
        if (myPiece == null) {
            throw new InvalidMoveException("another one of these garbage errors");
        }
        ChessPosition moveEndPosition = move.getEndPosition();
        if (myPiece.getTeamColor() == null) {
            throw new InvalidMoveException("the team color was null");
        }
        if (myPiece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("wrong team turn");
                }
        Collection<ChessMove> validMovesList = validMoves(moveStartPosition);
        if(validMovesList == null) {
            throw new InvalidMoveException("the validMovesList is null");}
        if (!validMovesList.contains(move)) {
            throw new InvalidMoveException("that move is not found in the validMovesList");
        }
        if (move.getPromotionPiece() == null) {
            myBoard.addPiece(moveEndPosition,myPiece);
        }
        else {
            ChessPiece.PieceType promotionPieceType = move.getPromotionPiece();
            ChessPiece newPromotionPiece = new ChessPiece(getTeamTurn(),promotionPieceType);
            myBoard.addPiece(moveEndPosition, newPromotionPiece);
        }
        myBoard.addPiece(moveStartPosition, null);
        turnCounter ++;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        int[] rows = {1,2,3,4,5,6,7,8};
        int[] cols = {1,2,3,4,5,6,7,8};

        ChessPosition kingPosition = null;

        for (int row : rows) {
            for (int col : cols) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                if (myBoard.getPiece(currentPosition) == null) {
                    continue;
                }
                ChessPiece pieceToCheck = myBoard.getPiece(currentPosition);
                if (pieceToCheck.getPieceType() == ChessPiece.PieceType.KING
                        && pieceToCheck.getTeamColor() == teamColor) {
                    kingPosition = currentPosition;
                }
            }
        }

        //these are enemy pieces
        int[] rowsSub = {1,2,3,4,5,6,7,8};
        int[] colsSub = {1,2,3,4,5,6,7,8};

        for (int rowSub : rowsSub) {
            for (int colSub : colsSub) {
                ChessPosition currentPositionSub = new ChessPosition(rowSub, colSub);
                if (myBoard.getPiece(currentPositionSub) == null) {
                    continue;
                }
                ChessPiece pieceToCheckSub = myBoard.getPiece(currentPositionSub);
                if (pieceToCheckSub.getTeamColor() == teamColor) {
                    continue;
                }
                        //check its possible moves
                    Collection<ChessMove> otherTeamsPieceMoves =
                            pieceToCheckSub.pieceMoves(myBoard,currentPositionSub);
                        for (ChessMove move : otherTeamsPieceMoves) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }



        //i need the state of the king of that color
        //
        //would need to check the kings current position and see if
        //its in any of the possible moves of the opposing teams pieces;
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    private boolean hasValidMove(TeamColor teamColor) {
        int[] rows = {1,2,3,4,5,6,7,8};
        int[] cols = {1,2,3,4,5,6,7,8};
        for (int row : rows) {
            for (int col : cols) {
                ChessPosition currentPosition = new ChessPosition(row, col);

                ChessPiece pieceInCurrentPosition = myBoard.getPiece(currentPosition);

                if (pieceInCurrentPosition == null) {
                    continue;
                }
                if (pieceInCurrentPosition.getTeamColor() != teamColor) {
                    continue;
                }
                Collection<ChessMove> possibleMoves = validMoves(currentPosition);

                if (possibleMoves != null && !possibleMoves.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        //is in check AND the king valid moves option is empty

        //for all pieces on the place team if any of the have a valid move then set boolean to true
        if (!isInCheck(teamColor)) {
            return false;
            //figure out myteams moves


        }
        return !hasValidMove(teamColor);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //isIn check is not true AND the king valid moves is empty
        //if (InCheck(teamColor) is false && validMoves is empty)
        if (isInCheck(teamColor)) {
            return false;
        }
        return !hasValidMove(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }

    @Override
    public String toString() {
        return String.format("%s,%s",myBoard,turnCounter);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turnCounter == chessGame.turnCounter && Objects.equals(myBoard, chessGame.myBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myBoard, turnCounter);
    }
}
