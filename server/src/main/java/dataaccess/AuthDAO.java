package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuth(String authToken);
    void createAuth(AuthData authDataObject);
    void deleteAuth(String authToken);
    void clear();
}
