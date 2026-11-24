package websocket.commands;

//Forfeits the match and ends the game (no more moves can be made)

public class ResignCommand extends UserGameCommand{
    public ResignCommand(String authToken, int gameID) {
        super(CommandType.RESIGN, authToken, gameID);
    }
}
