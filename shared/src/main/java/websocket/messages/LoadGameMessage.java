package websocket.messages;

//Used by the server to send the current game state to a client.
// When a client receives this message, it will redraw the chess board.

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private ChessGame game;

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
