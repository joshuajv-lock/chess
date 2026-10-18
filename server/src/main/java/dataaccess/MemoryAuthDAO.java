package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{

    Map<String, AuthData> authStorage = new HashMap<>();

    @Override
    public void createAuth(AuthData authDataObject){
        String key = authDataObject.authToken();
        authStorage.put(key,authDataObject);
    };
    @Override
    public AuthData getAuth(String authToken) {
        AuthData authObjectToReturn = authStorage.get(authToken);
        return authObjectToReturn;
    };
    @Override
    public void deleteAuth(String authToken) {
        authStorage.remove(authToken);
    };
    @Override
    public void clear() {
        authStorage.clear();
    };
}
