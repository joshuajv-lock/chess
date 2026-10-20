package handler;
import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;
import service.requests.JoinGameRequest;

import java.util.Map;
public class JoinGameHandler {
    private final GameService gameService;

    public JoinGameHandler (GameService gameService) {
        this.gameService = gameService;
    }
    public void translate(Context ctx) {
        String jsonText = ctx.body();
        String authToken = ctx.header("authorization");
        Gson gson = new Gson();

        JoinGameBody body = gson.fromJson(jsonText, JoinGameBody.class);

        String playerColor = body == null ? null : body.playerColor();
        int gameID = body == null ? 0 : body.gameID();

        try{
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor,gameID);

            gameService.joinGame(joinGameRequest);

            ctx.result("{}");
        }

        catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
        catch (BadRequestException e) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
        catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
    }
}
