package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlGameDao implements GameDAO{

    public SqlGameDao() throws DataAccessException {
        DatabaseManager.createDatabase();
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        //create the table if it doesn't exist
        String sqlStatement = """
            CREATE TABLE IF NOT EXISTS games ( 
                gameID INT PRIMARY KEY NOT NULL,
                whiteUsername VARCHAR(256) NOT NULL,
                blackUsername VARCHAR(256) NOT NULL,
                gameName VARCHAR(256) NOT NULL,
                game TEXT NOT NULL
                )
                """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(
                    "The table could not be created",
                    e
            );
        }
    }


    @Override
    public void createGame(GameData gameDataObject) throws DataAccessException{
        String sqlStatement = """
            INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game)
            VALUES (?,?,?,?,?);
                """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.setInt(1, gameDataObject.gameID());
            statement.setString(2,gameDataObject.whiteUsername());
            statement.setString(3, gameDataObject.blackUsername());
            statement.setString(4, gameDataObject.gameName());
            Gson gson = new Gson();
            String gameInJson = gson.toJson(gameDataObject.game());
            statement.setString(5, gameInJson);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(
                    "The info was not inserted into the table",
                    e
            );
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        String sqlStatement = """
            SELECT * FROM games
            WHERE gameID = ?;
            """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.setInt(1, gameID);
            try (var obtainedInfo = statement.executeQuery()) {
                //you are here trying to set what you've obtained to variables
                if (obtainedInfo.next()) {
                    int obtainedGameID = obtainedInfo.getInt("gameID");
                    String obtainedWhiteUsername = obtainedInfo.getString("whiteUsername");
                    String obtainedBlackUsername = obtainedInfo.getString("blackUsername");
                    String obtainedGameName = obtainedInfo.getString("gameName");
                    String obtainedGameString = obtainedInfo.getString("game");

                    Gson gson = new Gson();
                    ChessGame obtainedGame =
                            gson.fromJson(obtainedGameString, ChessGame.class);

                    return new GameData(obtainedGameID, obtainedWhiteUsername, obtainedBlackUsername, obtainedGameName, obtainedGame);
                }
                return null;
            }
        }   catch (SQLException e) {
            throw new DataAccessException(
                    "The info was not obtained from the table",
                    e
            );
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException{
        String sqlStatement = """
            SELECT * FROM games;
            """;

        ArrayList<GameData> gameList = new ArrayList<>();


        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            try (var obtainedInfo = statement.executeQuery()) {
                //you are here trying to set what you've obtained to variables
                Gson gson = new Gson();
                while (obtainedInfo.next()) {
                    int obtainedGameID = obtainedInfo.getInt("gameID");
                    String obtainedWhiteUsername = obtainedInfo.getString("whiteUsername");
                    String obtainedBlackUsername = obtainedInfo.getString("blackUsername");
                    String obtainedGameName = obtainedInfo.getString("gameName");
                    String obtainedGameString = obtainedInfo.getString("game");


                    ChessGame obtainedGame =
                            gson.fromJson(obtainedGameString, ChessGame.class);

                    gameList.add(new GameData(obtainedGameID, obtainedWhiteUsername, obtainedBlackUsername, obtainedGameName, obtainedGame));
                }
                return gameList;
            }
            return null;
        }   catch (SQLException e) {
            throw new DataAccessException(
                    "The info was not obtained from the table",
                    e
            );
        }
    }

    @Override
    public GameData updateGame(GameData gameDataObject) throws DataAccessException{
        return null;
    }

    @Override
    public void clear() throws DataAccessException{

        String sqlStatement = """
            DELETE FROM games;
            """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(
                    "could not clear table",
                    e
            );
        }
    };
}
