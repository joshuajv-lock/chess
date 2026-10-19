package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;

import java.util.Map;

public class ListGamesHandler {

    private final GameService gameService;
    public ListGamesHandler (GameService gameService) {
        this.gameService = gameService;
    }
    public void translate(Context ctx) {
        String authToken = ctx.header("authorization");
        Gson gson = new Gson();

        try{
            ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);

            String jsonTextResult = gson.toJson(listGamesResult);

            ctx.result(jsonTextResult);
        }

        catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
        catch (BadRequestException e) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
    }
}
