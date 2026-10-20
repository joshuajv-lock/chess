package handler;


import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;
import service.requests.LoginRequest;
import service.results.LoginResult;

import java.util.Map;

public class LoginHandler {
    private final UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public void translate(Context ctx) {
        String jsonText = ctx.body();
        Gson gson = new Gson();


        try{
            LoginRequest jsonToRequest = gson.fromJson(jsonText, LoginRequest.class);

            LoginResult resultFromRequest = userService.login(jsonToRequest);

            String jsonTextResult = gson.toJson(resultFromRequest);
            ctx.result(jsonTextResult);}

        catch (BadRequestException e) {
            ctx.status(400);
            ctx.result(gson.toJson(Map.of("message",e.getMessage())));
        }

        catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
    }
}
