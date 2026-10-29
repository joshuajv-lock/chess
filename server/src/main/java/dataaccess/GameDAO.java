package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(GameData gameDataObject) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    GameData updateGame(GameData gameDataObject) throws DataAccessException;
    void clear() throws DataAccessException;
}
