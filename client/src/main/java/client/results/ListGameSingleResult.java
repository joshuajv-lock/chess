package client.results;

public record ListGameSingleResult (
        int gameID,
        String whiteUsername,
        String blackUsername,
        String gameName
){
    @Override
    public String toString() {
        return  ", gameName='" + gameName + '\'' +
                "whiteUsername='" + whiteUsername + '\'' +
                ", blackUsername='" + blackUsername + '\'' +

                '}';
    }
}
