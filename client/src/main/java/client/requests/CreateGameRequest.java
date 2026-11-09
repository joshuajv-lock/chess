package client.requests;

public record CreateGameRequest (
        String authToken,
        String gameName
){
}
