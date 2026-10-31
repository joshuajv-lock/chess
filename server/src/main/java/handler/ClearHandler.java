package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;
import service.GameService;

import com.google.gson.Gson;
import io.javalin.http.Context;

import java.util.Map;

public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler (ClearService clearService) {
        this.clearService = clearService;
    }
    public void clear(Context ctx) throws DataAccessException {
        Gson gson = new Gson();
        try {String jsonText = ctx.body();

            clearService.clear();

            ctx.result("{}");}
        catch (DataAccessException e) {
            ctx.status(500);
            ctx.result(gson.toJson(Map.of("message","Error" + e.getMessage())));
        }

    }
}
