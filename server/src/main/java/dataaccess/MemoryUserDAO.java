package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    Map<String, UserData> userStorage = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return userStorage.get(username);
    };

    @Override
    public void createUser(UserData userDataObject){
        userStorage.put(userDataObject.username(), userDataObject);
    };

    @Override
    public void clear(){
        userStorage.clear();
    };
}
