package server;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;

public class WsHandler {

    private AuthDAO authDAO;
    private GameDAO gameDao;

    public WsHandler(AuthDAO authDAO, GameDAO gameDao) {
        this.authDAO = authDAO;
        this.gameDao = gameDao;
    }

    public void onConnect(WsConnectContext ctx){
        System.out.println("WebSocket connected");
    }

    public void onMessage(WsMessageContext ctx){
        System.out.println("WebSocket Message: " + ctx.message());
    }

    public void onClose(WsCloseContext ctx){
        System.out.println("WebSocket closed");
    }

}
