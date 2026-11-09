package server;

import java.net.http.HttpClient;

import client.requests.*;
import client.results.CreateGameResult;
import client.results.ListGamesResult;
import client.results.LoginResult;
import client.results.RegisterResult;
import com.google.gson.Gson;
import exception.ResponseException;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    //add or register
    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException{
        var request = buildRequest("POST", "/user", registerRequest, null);
        var result = sendRequest(request);
        return  handleResponse(result,RegisterResult.class);
    }
        //request
        //response
        //handler

    //login
        //request
        //response
        //handler
    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var request = buildRequest("POST", "/session", loginRequest, null);
        var result = sendRequest(request);
        return handleResponse(result,LoginResult.class);
    }

    //delete or logout
        //request
        //send request
        //handler
    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        var request = buildRequest("DELETE", "/session", null, logoutRequest.authToken());
        var result = sendRequest(request);
        handleResponse(result,null);
    }

    //createGame
        //request
        //response
        //handler
    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException {
        var request = buildRequest("POST", "/game", createGameRequest, createGameRequest.authToken());
        var result = sendRequest(request);
        return handleResponse(result, CreateGameResult.class);
    }

    //listGames
        //request
        //response
        //handler
    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ResponseException {
        var request = buildRequest("GET", "/game", null, listGamesRequest.authToken());
        var result = sendRequest(request);
        return handleResponse(result, ListGamesResult.class);
    }

    //joinGame
        //request
        //handler
    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var request = buildRequest("PUT", "/game", joinGameRequest, joinGameRequest.authToken());
        var result = sendRequest(request);
        handleResponse(result,null);
    }
    //clear
    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null, null);
        var result = sendRequest(request);
        handleResponse(result,null);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(500, "Error Unable to send request");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        int status = response.statusCode();
        if (!isSuccessful(status)) {

                throw new ResponseException(status, response.body());
            }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }



}
