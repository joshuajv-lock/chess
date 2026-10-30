package dataaccess;

import chess.ChessGame;

import model.GameData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlGameDaoTest {

    private SqlGameDao gameDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDao = new SqlGameDao();
        gameDao.clear();
    }

    @Test
    public void createGameTable() throws Exception {

        try (var connection = DatabaseManager.getConnection()) {
            var tableData = connection.getMetaData();
            try (var tablesInData = tableData.getTables(
                    null,null,"games",new String[]{"TABLE"})){
                assertTrue(tablesInData.next());
            }
        }
    }

    //create and get game
    @Test
    public void createGameTest() throws DataAccessException {

        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(1,"whiteusername","blackusername","gameName", chessGame);
        gameDao.createGame(gameData);
        GameData retreivedGame = gameDao.getGame(1);
        assertNotNull(retreivedGame);
        assertEquals(1, retreivedGame.gameID());
        assertEquals("whiteusername", retreivedGame.whiteUsername());
        assertEquals("blackusername", retreivedGame.blackUsername());
        assertEquals("gameName", retreivedGame.gameName());
        assertNotNull(retreivedGame.game());
    }

    //ListGameTest
    @Test
    public void listGamesTest() throws DataAccessException {

        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(1,"whiteusername","blackusername","gameName", chessGame);
        gameDao.createGame(gameData);
        GameData retreivedGame = gameDao.getGame(1);
        assertNotNull(retreivedGame);

        ChessGame chessGame2 = new ChessGame();
        GameData gameData2 = new GameData(2,"whiteusername2","blackusername2","gameName2", chessGame2);
        gameDao.createGame(gameData2);
        GameData retreivedGame2 = gameDao.getGame(2);
        assertNotNull(retreivedGame2);

        ArrayList<GameData> listOfGames = new ArrayList<>(gameDao.listGames());
        assertNotNull(listOfGames);
        assertEquals(2,listOfGames.size());
        assertEquals("whiteusername2", listOfGames.get(1).whiteUsername());
    }

    //updateGameTest
    @Test
    public void updateGameTest() throws DataAccessException {

        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(1,"whiteusername",null,"gameName", chessGame);
        gameDao.createGame(gameData);
        GameData retreivedGame = gameDao.getGame(1);
        assertNotNull(retreivedGame);

        assertNull(retreivedGame.blackUsername());

        GameData updatedGameData = new GameData(1,"whiteusername","blackusername2","gameName", chessGame);
        gameDao.updateGame(updatedGameData);
        GameData retreivedUpdatedGame = gameDao.getGame(1);
        assertNotNull(retreivedUpdatedGame);

        assertEquals("blackusername2", retreivedUpdatedGame.blackUsername());
    }

    @Test
    public void clearTest() throws DataAccessException {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1,"wusername", "busername", "gameName", game);
        gameDao.createGame(gameData);
        gameDao.clear();
        GameData retreivedGame = gameDao.getGame(gameData.gameID());
        assertNull(retreivedGame);
    }

}
