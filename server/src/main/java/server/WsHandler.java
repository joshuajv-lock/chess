package server;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;

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

    public void onMessage(WsMessageContext ctx) throws DataAccessException {

        UserGameCommand command = new
                 Gson().fromJson(ctx.message(), UserGameCommand.class);

        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                //throw DataAccessException;
                return;
            }
            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);
            String username = authDAO.getAuth(authToken).username();


            if (gameData.blackUsername().equals(username)) {

            }
            else if (gameData.whiteUsername().equals(username)) {

            }
            else {
                //observer
            }

        }

        if (command.equals(UserGameCommand.CommandType.MAKE_MOVE)) {
        }

        if (command.equals(UserGameCommand.CommandType.RESIGN)) {
        }

        if (command.equals(UserGameCommand.CommandType.LEAVE)) {
        }

        System.out.println("WebSocket Message: " + ctx.message());
    }

    public void onClose(WsCloseContext ctx){
        System.out.println("WebSocket closed");
    }

}
