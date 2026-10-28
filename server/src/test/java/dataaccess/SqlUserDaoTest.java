package dataaccess;


import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;


import static org.junit.jupiter.api.Assertions.*;

public class SqlUserDaoTest{

    private SqlUserDao userDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDao = new SqlUserDao();
        userDao.clear();
    }


    @Test
    public void createUserTable() throws Exception {

        try (var connection = DatabaseManager.getConnection()) {
            var tableData = connection.getMetaData();
            try (var tablesInData = tableData.getTables(
                    null,null,"users",new String[]{"TABLE"})){
                assertTrue(tablesInData.next());
            }
        }
    }
    @Test
    public void createUserTest() throws DataAccessException {
        UserData userData = new UserData("username", "password", "@email");
        userDao.createUser(userData);
        UserData retreivedUser = userDao.getUser("username");
        assertNotNull(retreivedUser);
        assertEquals("username", retreivedUser.username());
        assertTrue(BCrypt.checkpw("password",retreivedUser.password()));
        assertEquals("@email", retreivedUser.email());
    }

    @Test
    public void clearTest() throws DataAccessException {
        UserData userData = new UserData("username", "password", "@email");
        userDao.createUser(userData);
        userDao.clear();
        UserData retreivedUser = userDao.getUser("username");
        assertNull(retreivedUser);
    }

}
