package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class DrawTheBoard {

    //loops through the board:
    public void drawTheBoard(PrintStream out, ChessGame.TeamColor perspective) {

        drawCols(out, true, perspective);

        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int row = 8; row >= 1; row--) {
                drawRow(out,row,perspective);
            }
        }
        if (perspective == ChessGame.TeamColor.BLACK) {
            for (int row = 1; row <= 8; row++) {
                drawRow(out,row,perspective);
            }
        }


        drawCols(out, false, perspective);
    }

    private static void drawCols(PrintStream out, boolean atTop, ChessGame.TeamColor perspective) {
        String[] columns;
        if (perspective == ChessGame.TeamColor.WHITE) {
            columns = new String[]{"  a ", "b ", "c ", "d ", "e ", "f ", "g ", "h "};
        }
        else {
            columns = new String[]{"  h ", "g ", "f ", "e ", "d ", "c ", "b ", "a "};
        }
        for (String col : columns) {
            out.print(" "+ col +" ");
        }
        if (atTop == true) {
            out.println();
        }

    }

    private static void drawRow(PrintStream out, int row, ChessGame.TeamColor perspective) {

        out.print(row + " ");

        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int col = 1; col <= 8; col++) {
                drawSquare(out, row, col);
            }
        }

        if (perspective == ChessGame.TeamColor.BLACK) {
            for (int col = 8; col >= 1; col--) {
                drawSquare(out, row, col);
            }
        }



        out.println(" " + row);
    }

    private static void drawSquare(PrintStream out, int row, int col){

        if ((row + col) % 2 == 0) {
            out.print(SET_BG_COLOR_DARK_GREEN);
        }
        else {
            out.print(SET_BG_COLOR_BLACK);
        }


        out.print("");
        String piece = DrawTheBoard.getPiece(row,col);
        out.print(piece);


        out.print(RESET_BG_COLOR);

    };

    private static String getPiece(int row, int col) {

        String piece = "    ";

        if (row == 7) {
              //it's a pawn
            piece = BLACK_PAWN;
          }

        if (row == 8) {
              //its the other color pawn
            if (col == 1 || col == 8) {
                piece = BLACK_ROOK;
            };
            if (col == 2 || col == 7) {
                piece = BLACK_KNIGHT;
            };
            if (col == 3 || col == 6) {
                piece = BLACK_BISHOP;
            }
            if (col == 4) {
                piece = BLACK_QUEEN;
            }
            if (col == 5) {
                piece = BLACK_KING;
            }
          }

        if (row == 2){
              //one teams regular pieces
            piece = WHITE_PAWN;
          }

        if (row == 1) {
              //other teams regular pieces
            if (col == 1 || col == 8) {
                piece = WHITE_ROOK;
            };
            if (col == 2 || col == 7) {
                piece = WHITE_KNIGHT;
            };
            if (col == 3 || col == 6) {
                piece = WHITE_BISHOP;
            }
            if (col == 4) {
                piece = WHITE_QUEEN;
            }
            if (col == 5) {
                piece = WHITE_KING;
            }
          }

        return piece;
    };


}
