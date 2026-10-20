package service;

import chess.ChessGame;
import dataaccess.*;
import handler.JoinGameHandler;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Test
    public void CreateGameTest() throws Exception {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        GameService gameService = new GameService(authDAO,gameDAO);

        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(1,null,null,"g1", chessGame);

        gameDAO.createGame(gameData);

        AuthData authData = new AuthData("example-authToken","josh");
        authDAO.createAuth(authData);

        //create CreateGameRequest request
        CreateGameRequest createGameRequest = new CreateGameRequest("example-authToken", "g1");
        gameService.createGame(createGameRequest);

        assertEquals("josh", authData.username());
        assertEquals("example-authToken",authData.authToken());
        assertEquals("g1", gameDAO.getGame(1).gameName());

        assertThrows(UnauthorizedException.class, () -> gameService.createGame(new CreateGameRequest("fake-authToken", "fake-gamename")));

    }

    @Test
    public void joinGameTest() throws Exception {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        GameService gameService = new GameService(authDAO,gameDAO);

        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(1,null,null,"g1", chessGame);

        gameDAO.createGame(gameData);

        AuthData authData = new AuthData("example-authToken","josh");
        authDAO.createAuth(authData);

        //create JoinGameRequest request
        JoinGameRequest joinGameRequest = new JoinGameRequest("example-authToken","WHITE", 1);
        gameService.joinGame(joinGameRequest);


        assertEquals("josh", gameDAO.getGame(1).whiteUsername());
        assertEquals("g1", gameDAO.getGame(1).gameName());

        assertThrows(UnauthorizedException.class, () -> gameService.createGame(new CreateGameRequest("fake-authToken", "fake-gamename")));

        assertEquals("josh", gameDAO.getGame(1).whiteUsername());

        AuthData authData2 = new AuthData("secondAuthToken", "not-josh");
        authDAO.createAuth(authData2);

        JoinGameRequest notJoshJoinGameRequest = new JoinGameRequest("secondAuthToken","WHITE",1);

        //add the players username to the whiteusername


        assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(notJoshJoinGameRequest));

    }
    @Test
    public void listGamesTest() throws Exception {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        GameService gameService = new GameService(authDAO,gameDAO);

        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(1,"josh","not-josh","g1", chessGame);
        gameDAO.createGame(gameData);

        ChessGame chessGame2 = new ChessGame();
        GameData gameData2 = new GameData(2,"grace","not-grace","g2", chessGame2);
        gameDAO.createGame(gameData2);

        ChessGame chessGame3 = new ChessGame();
        GameData gameData3 = new GameData(3,"luke","not-luke","g3", chessGame3);
        gameDAO.createGame(gameData3);
        //create JoinGameRequest request

        AuthData authData = new AuthData("example-authToken","josh");
        authDAO.createAuth(authData);

        ListGamesRequest listGamesRequest = new ListGamesRequest("example-authToken");
        ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);

        ListGamesRequest listGamesRequestUnauth = new ListGamesRequest("example-authToken-bad");

        assertEquals(3, listGamesResult.games().size());

        assertThrows(UnauthorizedException.class, () -> gameService.listGames(listGamesRequestUnauth));

    }

}
