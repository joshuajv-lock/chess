package websocket.commands;

//Tells the server you are leaving the game so it will stop sending you notifications.
//Root Client sends RESIGN
//
//Server marks the game as over (no more moves can be made).
// Game is updated in the database.
//Server sends a Notification message to all clients in that game informing them that
//the root client resigned. This applies to both players and observers.

public class LeaveCommand extends UserGameCommand{

    public LeaveCommand(String authToken, int gameID) {
        super(CommandType.LEAVE, authToken, gameID);
    }
}
