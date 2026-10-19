package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;
import service.BadRequestException;
import service.AlreadyTakenException;

import java.util.Map;

public class RegisterHandler{

private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public void translate(Context ctx) {
        String jsonText = ctx.body();
        Gson gson = new Gson();


        try{
            RegisterRequest jsonToRequest = gson.fromJson(jsonText, RegisterRequest.class);

            RegisterResult resultFromRequest = userService.register(jsonToRequest);

            String jsonTextResult = gson.toJson(resultFromRequest);
            ctx.result(jsonTextResult);}

            catch (BadRequestException e) {
                ctx.status(400);
                ctx.result(gson.toJson(Map.of("message",e.getMessage())));
            }

            catch (AlreadyTakenException e) {
                ctx.status(403);
                ctx.result(gson.toJson(Map.of("message", e.getMessage())));
            }
        }
    }
