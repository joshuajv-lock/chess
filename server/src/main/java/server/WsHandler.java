package server;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import model.AuthData;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;

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

    public void onMessage(WsMessageContext ctx) throws DataAccessException, InvalidMoveException {

        UserGameCommand command = new
                 Gson().fromJson(ctx.message(), UserGameCommand.class);

        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                //throw DataAccessException;
                return;
            }
            String username = authData.username();

            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);
            if (gameData == null) {
                //throw new DataAccessException("that game doesn't exist", )
            }


            if (username.equals(gameData.blackUsername())) {

                System.out.println(username + "connected as BLACK");
            }
            else if (username.equals(gameData.whiteUsername())) {

                System.out.println(username + " connected as WHITE");
            }
            else {
                //observer
                System.out.println(username + "connected as an observer");
            }

            LoadGameMessage gameMessage = new LoadGameMessage(gameData.game());
            String json = new Gson().toJson(gameMessage);
            ctx.send(json);

        }

        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                //throw DataAccessException;
                return;
            }

            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);
            String username = authData.username();
            String gameName = gameData.gameName();
            ChessGame game = gameData.game();

            MakeMoveCommand makeMoveCommand = new
                    Gson().fromJson(ctx.message(), MakeMoveCommand.class);
            ChessMove move = makeMoveCommand.getMove();


            //update game
            if (gameData.game() == null) {
                //throw an error
            }
            else {

                if (move == null) {
                    //throw error
                }
                else {
                    //make move
                    game.makeMove(move);
                    LoadGameMessage gameMessage = new LoadGameMessage(gameData.game());
                    String json = new Gson().toJson(gameMessage);
                    ctx.send(json);
                }

            }

            //send message
            if (username.equals(gameData.blackUsername())) {
                System.out.println(username + "has executed: "+ move+ "as their move");

            }
            else if (username.equals(gameData.whiteUsername())) {
                System.out.println(username + "has executed: "+ move+ "as their move");
            }
            else {
                System.out.println(username + "has executed: "+ move+ "as their move");;
            }


        }

        if (command.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                //throw DataAccessException;
                return;
            }
            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);
            String username = authData.username();
            String gameName = gameData.gameName();
            ChessGame game = gameData.game();

            if (username.equals(gameData.blackUsername())) {
                //end game
                String opponentUsername = gameData.whiteUsername();
                System.out.println(username + "has resigned the game and "+ opponentUsername+ " wins the game");

            }
            else if (username.equals(gameData.whiteUsername())) {
                //end game
                String opponentUsername = gameData.blackUsername();
                System.out.println(username + "has resigned the game and "+ opponentUsername+ " wins the game");
            }
            else {
                //observer
                System.out.println(username + "is no longer observing the game");
            }

        }

        if (command.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                //throw DataAccessException;
                return;
            }
            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);
            String username = authData.username();
            String gameName = gameData.gameName();
            ChessGame game = gameData.game();

            if (username.equals(gameData.blackUsername())) {
                GameData updatedGameData = new GameData(gameID, gameData.whiteUsername(), null, gameName, game);
                gameDao.updateGame(updatedGameData);
                System.out.println(username + "has left the game and there is no longer a BLACK player");

            }
            else if (username.equals(gameData.whiteUsername())) {
                GameData updatedGameData = new GameData(gameID, null, gameData.blackUsername(), gameName, game);
                gameDao.updateGame(updatedGameData);
                System.out.println(username + "has left the game and there is no longer a WHITE player");
            }
            else {
                //observer
                System.out.println(username + "is no longer observing the game");
            }

        }

        System.out.println("WebSocket Message: " + ctx.message());
    }

    public void onClose(WsCloseContext ctx){
        System.out.println("WebSocket closed");
    }

}
