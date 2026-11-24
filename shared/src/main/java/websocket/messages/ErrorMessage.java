package websocket.messages;

//This message is sent to a client when it sends an invalid command.
// The message must include the word Error.

import websocket.commands.UserGameCommand;

public class ErrorMessage extends ServerMessage{

    String errorMessage;

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
