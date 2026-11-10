package client;
import client.requests.*;
import client.results.*;
import exception.ResponseException;
import server.ServerFacade;
import ui.DrawTheBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

    //read in with the scannerloop
    public void run() throws ResponseException {
        
        ServerFacade server = new ServerFacade("http://localhost:8080");

        Scanner scanner = new Scanner(System.in);

        String authToken = null;

        while (true) {
            System.out.print(">>> ");
            String clientInput = scanner.nextLine();

            //register

            // login

            //store authToken

            //creat a game

            //list games

            //join a game

            //help

            //logout

            //draw the board

            if (clientInput.equals("register")) {
                System.out.println("Please provide: username, password, email separated by spaces");
                String registerInput = scanner.nextLine();
                String[] params = registerInput.split(" ");
                RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
                RegisterResult result = server.register(request);
                authToken = result.authToken();
            }

            if (clientInput.equals("quit")) {
                break;
            }
            if (clientInput.equals("login")) {
                System.out.println("Please provide: your username and password separated by spaces. " +
                        "If you don't have an account, you must first register");
                String loginInput = scanner.nextLine();
                String[] params = loginInput.split(" ");
                LoginRequest request = new LoginRequest(params[0], params[1]);
                LoginResult result = server.login(request);
                authToken = result.authToken();
            }
            if (clientInput.equals("create game")) {
                System.out.println("Please Provide the name of the game you would like to create");
                String gameNameLine = scanner.nextLine();
                CreateGameRequest request = new CreateGameRequest(authToken,gameNameLine);
                server.createGame(request);
                System.out.println("Game created successfully.");
            }
            if (clientInput.equals("list games")) {
                System.out.println("We are attempting to get the list of games for you");
                ListGamesRequest request = new ListGamesRequest(authToken);
                ListGamesResult result = server.listGames(request);


                for (ListGameSingleResult game : result.games()) {
                    System.out.println(game);
                }
            }

            if (clientInput.equals("play game")) {
                System.out.println("Please provide the color you would like to be ");
                String joinGameInput = scanner.nextLine();
                String[] params = joinGameInput.split(" ");
                JoinGameRequest request = new JoinGameRequest(authToken,params[0],gameId);
                server.joinGame(request);

                DrawTheBoard board = new DrawTheBoard();

                //draw the board from the correct perspective
            }
            if (clientInput.equals("observe game")) {
                System.out.println("Please provide the gameID of the game you would like to observe");
                String observeGameInput = scanner.nextLine();
                int gameId = Integer.parseInt(observeGameInput);
                //looks like i need to make an observe game request and result?
                JoinGameRequest request = new JoinGameRequest(authToken,params[0],gameId);
                server.joinGame(request);
                //draw the board from the correct perspective
            }

            if (clientInput.equals("help")) {
                if (authToken == null) {
                    System.out.println("Here are the available commands you can type:");
                    System.out.println("register: register yourself");
                    System.out.println("login: login with existing username and password");
                    System.out.println("quit: exits the program");
                    System.out.println("help - provides a list of possible commands");
                }
                else {
                    System.out.println("Here are the available commands you can type:");
                    System.out.println("create game: create a game of chess");
                    System.out.println("list games: list the currently created games");
                    System.out.println("join game: join a game that has been created");
                    System.out.println("observe game: observe a game");
                    System.out.println("logout: logs you out");
                    System.out.println("help - provides a list of possible commands");
                }
            }
            if (clientInput.equals("logout")) {
                LogoutRequest request = new LogoutRequest(authToken);
                server.logout(request);
                authToken = null;
                System.out.println("You have been logged out successfully.");
            }





            System.out.println("message received:"+ clientInput);
        }
    }



}
