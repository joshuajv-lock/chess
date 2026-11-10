package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class BoardTest {

    public static void main(String[] args) {

        PrintStream out =
                new PrintStream(
                        System.out,
                        true,
                        StandardCharsets.UTF_8
                );

        DrawTheBoard board = new DrawTheBoard();

        board.drawTheBoard(out, ChessGame.TeamColor.BLACK);
    }
}
