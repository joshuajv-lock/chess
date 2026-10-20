package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    public void registerTest() throws Exception {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO,authDAO);

        RegisterRequest request = new RegisterRequest(
                "josh",
                "pass123",
                "josh@gmail.com"
        );

        RegisterResult result = userService.register(request);

        assertEquals("josh", result.username());
        assertNotNull(result.authToken());

        assertThrows(
                BadRequestException.class,
                () -> userService.register(new RegisterRequest(
                        null,
                        "pass123",
                        "josh@gmail.com")));
        assertThrows(
                AlreadyTakenException.class,
                () -> userService.register(new RegisterRequest(
                        "josh",
                        "password",
                        "email"))
);

    }

    @Test
    public void loginTest() throws Exception {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO,authDAO);

        UserData userData = new UserData("josh","pass123", "josh@gmail.com");
        userDAO.createUser(userData);

        LoginRequest request = new LoginRequest("josh", "pass123");

        LoginResult result = userService.login(request);

        assertEquals("josh", result.username());
        assertNotNull(result.authToken());

        assertThrows(
                UnauthorizedException.class,
                () -> userService.login(new LoginRequest("not-josh","password")));

        assertThrows(
                UnauthorizedException.class,
                () -> userService.login(new LoginRequest("josh", "not-password")));


    }

    @Test
    public void logoutTest() throws Exception {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO,authDAO);
        UserData userData = new UserData("josh","pass123", "josh@gmail.com");
        userDAO.createUser(userData);

        AuthData authData = new AuthData("example-authToken","josh");
        authDAO.createAuth(authData);

        //create logout request
        LogoutRequest logoutRequest = new LogoutRequest("example-authToken");
        userService.logout(logoutRequest);

        assertNull(authDAO.getAuth("example-authToken"));

        assertThrows(UnauthorizedException.class, () -> userService.logout(new LogoutRequest("fake-authToken")));


    }
}
