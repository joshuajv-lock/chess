package dataaccess;

import dataaccess.sql.SqlAuthDao;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SqlAuthDaoTest {

    private SqlAuthDao authDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDao = new SqlAuthDao();
        authDao.clear();
    }

    @Test
    public void createAuthTable() throws Exception {

        try (var connection = DatabaseManager.getConnection()) {
            var tableData = connection.getMetaData();
            try (var tablesInData = tableData.getTables(
                    null,null,"auths",new String[]{"TABLE"})){
                assertTrue(tablesInData.next());
            }
        }
    }

    @Test
    public void createAuthTest() throws DataAccessException {
        AuthData authData = new AuthData("authToken","username");
        authDao.createAuth(authData);
        AuthData retreivedAuth = authDao.getAuth("authToken");
        assertNotNull(retreivedAuth);
        assertEquals("username", retreivedAuth.username());
        assertEquals("authToken", retreivedAuth.authToken());
    }

    //delete auth test
    @Test
    public void deleteAuthTest() throws DataAccessException {
        AuthData authData = new AuthData("authToken","username");
        authDao.createAuth(authData);
        AuthData retreivedAuth = authDao.getAuth("authToken");

        AuthData authData2 = new AuthData("authToken2","username2");
        authDao.createAuth(authData2);

        authDao.deleteAuth("authToken2");

        AuthData notDeletedAuth = authDao.getAuth("authToken");
        AuthData removedAuth2 = authDao.getAuth("authToken2");

        assertNotNull(retreivedAuth);
        assertEquals("username", notDeletedAuth.username());
        assertEquals("authToken", notDeletedAuth.authToken());

        assertNull(removedAuth2);


    }



    @Test
    public void clearTest() throws DataAccessException {
        AuthData authData = new AuthData("authToken","username");
        authDao.createAuth(authData);
        authDao.clear();
        AuthData retreivedAuth = authDao.getAuth("authToken");
        assertNull(retreivedAuth);
    }


}
