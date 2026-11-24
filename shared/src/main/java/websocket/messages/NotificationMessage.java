package websocket.messages;

//This is a message meant to inform a player when another player made an action.

//Root Client sends CONNECT
//
//Server sends a LOAD_GAME message back to the root client.
//Server sends a Notification message to all other clients in that
// game informing them the root client connected to the game, either as a player
// (in which case their color must be specified) or as an observer.

public class NotificationMessage extends ServerMessage{

    private String message;

    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
