package client;

import client.requests.RegisterRequest;
import client.results.RegisterResult;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws ResponseException {
        facade.clear();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    void RegisterToLogout() throws ResponseException {
        //Clear
        assertDoesNotThrow(()-> facade.clear());
        //Register
        RegisterResult registerResult =
                facade.register(new RegisterRequest(
                        "josh", "jpassword", "josh@email.com"
                ));
        assertNotNull(registerResult.authToken());
        //login

        //create game
        //list games
        //join game/play game
        //logout
    }



}
