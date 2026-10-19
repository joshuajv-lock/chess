package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import java.util.concurrent.atomic.AtomicInteger;


public class GameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private final AtomicInteger nextGameID = new AtomicInteger(1);

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame (CreateGameRequest createGameRequest) throws UnauthorizedException, BadRequestException{
        //authorize them
        if (createGameRequest == null) {
            throw new BadRequestException("you must send a createGameRequest object");
        }
        String authToken = createGameRequest.authToken();
        AuthData possibleAuth = authDAO.getAuth(authToken);
        if (possibleAuth == null) {
            throw new UnauthorizedException("you are not authorized to create a game");
        }
        else if (createGameRequest.gameName() == null) {
            throw new BadRequestException("you didn't give a gameName");
        }
        else {
            //create the game
            //create an ID
            int gameID = nextGameID.getAndIncrement();

            //leave white and black username empty

            //create a new Chessgame
            ChessGame newGame = new ChessGame();

            GameData newGameData = new GameData(gameID,null,null, createGameRequest.gameName(), newGame);
            gameDAO.createGame(newGameData);
            CreateGameResult newCreateGameResult = new CreateGameResult(gameID);
            return newCreateGameResult;
        }

        //create the game
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws UnauthorizedException, BadRequestException {
        if (listGamesRequest == null) {
            throw new BadRequestException("you must send a listGamesRequest object");
        }
        String authToken = listGamesRequest.authToken();
        AuthData possibleAuth = authDAO.getAuth(authToken);
        if (possibleAuth == null) {
            throw new UnauthorizedException("you are not authorized to list the stored games");
        }
        Collection<GameData> myGames = new ArrayList<>();
        //totally not right just a framework

        //list the games
        myGames = gameDAO.listGames();

        Collection<ListGameSingleResult> singleGameResults = new ArrayList<>();

        for (GameData game : myGames) {
            ListGameSingleResult singleGameResult = new ListGameSingleResult(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()
            );
            singleGameResults.add(singleGameResult);
        }
        ListGamesResult newListGamesResult = new ListGamesResult(singleGameResults);
        return newListGamesResult;


    }
}
