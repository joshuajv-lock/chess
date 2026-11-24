package websocket.commands;

//Used to request to make a move in a game.
//Root Client sends MAKE_MOVE
//
//Server verifies the validity of the move.
//Game is updated to represent the move. Game is updated in the database.
//Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
//Server sends a Notification message to all other clients in that game informing them what move was made.
//If the move results in check, checkmate or stalemate the server sends a Notification message to all clients.

public class MakeMoveCommand {
}
