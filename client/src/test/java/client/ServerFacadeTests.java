package client;

import client.requests.*;
import client.results.*;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        facade = new ServerFacade("http://localhost:"+port);
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

    @Test
    void RegisterToLogout() throws ResponseException {
        //Register
        RegisterResult registerResult =
                facade.register(new RegisterRequest(
                        "josh", "jpassword", "josh@email.com"
                ));
        assertNotNull(registerResult.authToken());
        //login
        LoginResult loginResult =
                facade.login(new LoginRequest("josh","jpassword"));
        assertNotNull(loginResult.authToken());

        //create game
        CreateGameResult createGameResult = facade.createGame(new CreateGameRequest(loginResult.authToken(),"gameName1"));
        assertTrue(createGameResult.gameID() > 0);

        //list games
        ListGamesResult listGamesResult = facade.listGames(new ListGamesRequest(loginResult.authToken()));
        var games = listGamesResult.games();
        ListGameSingleResult myGame = games.iterator().next();
        assertEquals("gameName1",myGame.gameName());
        assertNull(myGame.blackUsername());

        //join game/play game
        facade.joinGame(new JoinGameRequest(loginResult.authToken(), "BLACK", myGame.gameID()));
        ListGamesResult updatedGames = facade.listGames(new ListGamesRequest(loginResult.authToken()));
        ListGameSingleResult joinedGame = updatedGames.games().iterator().next();
        assertEquals("josh",joinedGame.blackUsername());

        //logout
        facade.logout(new LogoutRequest(loginResult.authToken()));
        assertThrows(ResponseException.class , () -> facade.listGames(new ListGamesRequest(loginResult.authToken())));


        //Duplicate Register
        assertThrows(ResponseException.class , () -> facade.register(new RegisterRequest(
                "josh", "jpassword2", "josh2@email.com"
        )));

        //wrong password
        assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("josh", "jpasswordfake")));
    }



}
