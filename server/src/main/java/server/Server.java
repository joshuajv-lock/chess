package server;

import dataaccess.*;
import dataaccess.sql.SqlAuthDao;
import dataaccess.sql.SqlGameDao;
import dataaccess.sql.SqlUserDao;
import handler.*;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        try {
            UserDAO userDAO = new SqlUserDao();
            AuthDAO authDAO = new SqlAuthDao();
            GameDAO gameDAO = new SqlGameDao();

            UserService userService = new UserService(userDAO,authDAO);
            GameService gameService = new GameService(authDAO,gameDAO);
            ClearService clearService = new ClearService(authDAO,gameDAO,userDAO);

            RegisterHandler registerHandler = new RegisterHandler(userService);

            javalin.post("/user", ctx -> registerHandler.translate(ctx));

            LoginHandler loginHandler = new LoginHandler(userService);

            javalin.post("/session", ctx -> loginHandler.translate(ctx));

            LogoutHandler logoutHandler = new LogoutHandler(userService);

            javalin.delete("/session", ctx -> logoutHandler.translate(ctx));

            CreateGameHandler createGameHandler = new CreateGameHandler(gameService);

            javalin.post("/game", ctx -> createGameHandler.translate(ctx));

            ListGamesHandler listGamesHandler = new ListGamesHandler(gameService);

            javalin.get("/game", ctx -> listGamesHandler.translate(ctx));

            JoinGameHandler joinGameHandler = new JoinGameHandler(gameService);

            javalin.put("/game", ctx -> joinGameHandler.translate(ctx));

            ClearHandler clearHandler = new ClearHandler(clearService);
            javalin.delete("/db", ctx -> clearHandler.clear(ctx));
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Failed to start database", e);
        }


    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
