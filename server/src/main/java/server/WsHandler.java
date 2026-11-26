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
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

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

    private final ConnectionManager connectionManager = new ConnectionManager();


    public void onMessage(WsMessageContext ctx) throws DataAccessException, InvalidMoveException {

        UserGameCommand command = new
                 Gson().fromJson(ctx.message(), UserGameCommand.class);

        if (command.getCommandType() == UserGameCommand.CommandType.CONNECT) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: you are not authorized to execute that action");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }
            String username = authData.username();

            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);
            if (gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: that game does not exist");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            String message;


            if (username.equals(gameData.blackUsername())) {

                message = (username + " connected as BLACK");
            }
            else if (username.equals(gameData.whiteUsername())) {

                message = (username + " connected as WHITE");
            }
            else {
                //observer
                message = (username + " connected as an observer");
            }

            Connection connection = new Connection();

            connection.username=username;
            connection.ctx = ctx;
            connection.gameID = gameID;

            connectionManager.addConnection(connection);

            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
            ctx.send(new Gson().toJson(loadGameMessage));

            NotificationMessage notificationMessage = new NotificationMessage(message);
            String notificationJson = new Gson().toJson(notificationMessage);
            connectionManager.sendWithoutReception(notificationJson,connection);

        }

        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: you are not authorized to execute that action");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);

            if (gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: that game does not exist");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            String username = authData.username();
            String gameName = gameData.gameName();
            ChessGame game = gameData.game();

            MakeMoveCommand makeMoveCommand = new
                    Gson().fromJson(ctx.message(), MakeMoveCommand.class);
            ChessMove move = makeMoveCommand.getMove();

            if (move == null) {
                    ErrorMessage errorMessage = new ErrorMessage("Error: that move does not exist");
                    ctx.send(new Gson().toJson(errorMessage));
                    return;
            }
            if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
                ErrorMessage errorMessage = new ErrorMessage("Error: observers cannot make moves");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            //correct team turn
            ChessGame.TeamColor playerTeamColor = null;
            if (username.equals(gameData.blackUsername())) {
                playerTeamColor = ChessGame.TeamColor.BLACK;
            }
            if (username.equals(gameData.whiteUsername())) {
                playerTeamColor = ChessGame.TeamColor.WHITE;
            }
            if (!game.getTeamTurn().equals(playerTeamColor)){
                ErrorMessage errorMessage = new ErrorMessage("Error: " +
                        "it is not your turn and therefore you cannot make that move");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            Connection connection = connectionManager.getConnection(ctx);

            if (connection == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: this client is not connected to the game");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            if (game.isGameOver()){
                ErrorMessage errorMessage = new ErrorMessage("Error: " +
                        "you cannot make a move, the game is already over");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }
            //update game
            //make move
            try {
                game.makeMove(move);
            } catch (InvalidMoveException e) {
                ErrorMessage errorMessage = new ErrorMessage("Error: " +
                        "the move you tried to make does not seem to be a valid move. It may be out of bounds" +
                        "or unallowed");
                ctx.send(new Gson().toJson(errorMessage));

                return;
            }

            //send message


           String colorInDanger = "";
           String dangerScenario = "";
           if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                colorInDanger = "WHITE";
                dangerScenario = "is in Check";
           }
           if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                colorInDanger = "BLACK";
                dangerScenario = "is in Check";
           }
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                colorInDanger = "BLACK";
                dangerScenario = "is in Checkmate";
                game.setGameOver(true);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                colorInDanger = "WHITE";
                dangerScenario = "is in Checkmate";
                game.setGameOver(true);
            }
            if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                colorInDanger = "BLACK";
                dangerScenario = "is in Stalemate";
                game.setGameOver(true);
            }
            if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                colorInDanger = "WHITE";
                dangerScenario = "is in Stalemate";
                game.setGameOver(true);
            }

            GameData updatedGameWithMove = new GameData(gameID, gameData.whiteUsername(),gameData.blackUsername(),gameName,game);
            gameDao.updateGame(updatedGameWithMove);

            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
            String loadGameString = new Gson().toJson(loadGameMessage);
            connectionManager.broadcast(loadGameString,connection);

            String message = (username + " has executed: "+ move+ " as their move");

            NotificationMessage notificationMessage = new NotificationMessage(message);
            String notificationJson = new Gson().toJson(notificationMessage);
            connectionManager.sendWithoutReception(notificationJson,connection);

            String conditionalMessage;
            if (!colorInDanger.equals("")) {
                conditionalMessage = colorInDanger +" "+ dangerScenario;
                NotificationMessage notificationMessageConditional = new NotificationMessage(conditionalMessage);
                String notificationJsonConditional = new Gson().toJson(notificationMessageConditional);
                connectionManager.broadcast(notificationJsonConditional,connection);
            }

        }

        if (command.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: you are not authorized to resign");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);

            if (gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: that game does not exist");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            String username = authData.username();
            String gameName = gameData.gameName();
            ChessGame game = gameData.game();

            if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
                ErrorMessage errorMessage = new ErrorMessage("Error: observers cannot resign");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            if (game.isGameOver()){
                ErrorMessage errorMessage = new ErrorMessage("Error: " +
                        "you cannot resign the game is already over");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            String opponentUsername = "";
            if (username.equals(gameData.blackUsername())) {
                //end game
                opponentUsername = gameData.whiteUsername();

            }
            if (username.equals(gameData.whiteUsername())) {
                //end game
                opponentUsername = gameData.blackUsername();
            }
            String message = username + " has resigned the game and "+ opponentUsername+ " wins the game";

            Connection connection = connectionManager.getConnection(ctx);

            if (connection == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: this client is not connected to the game");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            game.setGameOver(true);
            GameData updatedGameDataObject = new GameData(gameID,gameData.whiteUsername(),gameData.blackUsername(),gameName,game);
            gameDao.updateGame(updatedGameDataObject);

            NotificationMessage notificationMessage = new NotificationMessage(message);
            String notificationJson = new Gson().toJson(notificationMessage);
            connectionManager.broadcast(notificationJson,connection);

        }

        if (command.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            String authToken = command.getAuthToken();

            var authData = authDAO.getAuth(authToken);
            if (authData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: you are unauthorized to leave");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }
            int gameID = command.getGameID();
            GameData gameData = gameDao.getGame(gameID);

            if (gameData == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: that game does not exist");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            String username = authData.username();
            String gameName = gameData.gameName();
            ChessGame game = gameData.game();

            Connection connection = connectionManager.getConnection(ctx);

            if (connection == null) {
                ErrorMessage errorMessage = new ErrorMessage("Error: this client is not connected to the game");
                ctx.send(new Gson().toJson(errorMessage));
                return;
            }

            connectionManager.removeConnection(connection);

            String message;

            if (username.equals(gameData.blackUsername())) {
                GameData updatedGameData = new GameData(gameID, gameData.whiteUsername(), null, gameName, game);
                gameDao.updateGame(updatedGameData);
                message = username + " has left the game and there is no longer a BLACK player";

            }
            else if (username.equals(gameData.whiteUsername())) {
                GameData updatedGameData = new GameData(gameID, null, gameData.blackUsername(), gameName, game);
                gameDao.updateGame(updatedGameData);
                message = username + " has left the game and there is no longer a WHITE player";
            }
            else {
                //observer
                message = username + " is no longer observing the game";
            }

            NotificationMessage notificationMessage = new NotificationMessage(message);
            String notificationJson = new Gson().toJson(notificationMessage);
            connectionManager.sendWithoutReception(notificationJson,connection);

        }

        System.out.println("WebSocket Message: " + ctx.message());
    }

    public void onClose(WsCloseContext ctx){

        Connection connection = connectionManager.getConnection(ctx);

        if (connection!= null){
            connectionManager.removeConnection(connection);
        }
    }

}
