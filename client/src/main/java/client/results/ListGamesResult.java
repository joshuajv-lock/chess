package client.results;

import java.util.Collection;

public record ListGamesResult (
        Collection<ListGameSingleResult> games
){
}
