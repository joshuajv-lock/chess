package service.results;

public record ListGameSingleResult (
        int gameID,
        String whiteUsername,
        String blackUsername,
        String gameName
){
}
