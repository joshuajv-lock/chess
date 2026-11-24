package websocket.commands;

//Used for a user to make a WebSocket connection as a player or observer.

public class ConnectCommand extends UserGameCommand{

    public ConnectCommand(String authToken, int gameID) {
        super(CommandType.CONNECT, authToken, gameID);
    }

}
