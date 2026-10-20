package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTest {

    @Test
    public void clearTest() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();

        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(1,"josh","not-josh","g1", chessGame);
        gameDAO.createGame(gameData);

        ChessGame chessGame2 = new ChessGame();
        GameData gameData2 = new GameData(2,"grace","not-grace","g2", chessGame2);
        gameDAO.createGame(gameData2);

        ChessGame chessGame3 = new ChessGame();
        GameData gameData3 = new GameData(3,"luke","not-luke","g3", chessGame3);
        gameDAO.createGame(gameData3);

        AuthData authData = new AuthData("example-authToken","josh");
        authDAO.createAuth(authData);

        UserData userData = new UserData("josh", "pass123", "josh@gmail.com");
        userDAO.createUser(userData);

        ClearService clearService = new ClearService(authDAO,gameDAO,userDAO);
        clearService.clear();

        assertNull(userDAO.getUser("josh"));
        assertTrue(gameDAO.listGames().isEmpty());
        assertNull(authDAO.getAuth("example-authToken"));
    }
}
