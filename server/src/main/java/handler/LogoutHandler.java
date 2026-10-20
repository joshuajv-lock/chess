package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;
import service.requests.LogoutRequest;

import java.util.Map;

public class LogoutHandler {
    private final UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public void translate(Context ctx) {
        String authToken = ctx.header("authorization");
        Gson gson = new Gson();


        try{
            LogoutRequest logoutRequest = new LogoutRequest(authToken);

            //from here down is incomplete. need to impilement logout in the service

            userService.logout(logoutRequest);

            ctx.result("{}");
        }

        catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(gson.toJson(Map.of("message", e.getMessage())));
        }
    }
}
