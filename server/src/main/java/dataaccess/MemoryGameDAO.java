package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{

    Map<Integer, GameData> gameStorage  = new HashMap<>();

    @Override
    public void createGame(GameData gameDataObject){
        int gameID = gameDataObject.gameID();
        gameStorage.put(gameID,gameDataObject);
    };

    @Override
    public GameData getGame(int gameID){
        return gameStorage.get(gameID);
    };

    @Override
    public Collection<GameData> listGames(){

        return new ArrayList<GameData>(gameStorage.values());
    };

    @Override
    public GameData updateGame(GameData gameDataObject){
        int givenGameID = gameDataObject.gameID();
        gameStorage.put(givenGameID, gameDataObject);
        return gameDataObject;
    };

    @Override
    public void clear() {
        gameStorage.clear();
    };
}
