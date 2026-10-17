package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(GameData gameDataObject);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    GameData updateGame(GameData gameDataObject);
    void clear();
}
