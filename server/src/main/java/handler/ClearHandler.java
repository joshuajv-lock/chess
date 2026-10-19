package handler;

import com.google.gson.Gson;
import service.ClearService;
import service.GameService;

import com.google.gson.Gson;
import io.javalin.http.Context;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler (ClearService clearService) {
        this.clearService = clearService;
    }
    public void clear(Context ctx) {
        String jsonText = ctx.body();
        Gson gson = new Gson();

        clearService.clear();

        ctx.result("{}");
    }
}
