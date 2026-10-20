package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;

import java.util.Map;

public class CreateGameHandler {

    private final GameService gameService;

    public CreateGameHandler (GameService gameService) {
        this.gameService = gameService;
    }

    public void translate(Context ctx) {
        String jsonText = ctx.body();
        String authToken = ctx.header("authorization");
        Gson gson = new Gson();

        CreateGameBody body = gson.fromJson(jsonText, CreateGameBody.class);

        String gameName = body == null ? null : body.gameName();

        try{
            CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);

            CreateGameResult createGameResult = gameService.createGame(createGameRequest);

            String jsonTextResult = gson.toJson(createGameResult);

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
