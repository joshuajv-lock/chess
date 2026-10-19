package server;

import dataaccess.*;
import handler.CreateGameHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
import handler.RegisterHandler;
import io.javalin.*;
import service.GameService;
import service.LogoutRequest;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        UserService userService = new UserService(userDAO,authDAO);
        GameService gameService = new GameService(authDAO,gameDAO);

        RegisterHandler registerHandler = new RegisterHandler(userService);

        javalin.post("/user", ctx -> registerHandler.translate(ctx));

        LoginHandler loginHandler = new LoginHandler(userService);

        javalin.post("/session", ctx -> loginHandler.translate(ctx));

        LogoutHandler logoutHandler = new LogoutHandler(userService);

        javalin.delete("/session", ctx -> logoutHandler.translate(ctx));

        CreateGameHandler createGameHandler = new CreateGameHandler(gameService);

        javalin.post("/game", ctx -> createGameHandler.translate(ctx));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
