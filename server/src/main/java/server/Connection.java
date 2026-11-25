package server;

import io.javalin.websocket.WsContext;

public class Connection {
    String username;
    int gameID;
    WsContext ctx;
}
