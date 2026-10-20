package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.RegisterResult;

import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest registerRequestObject)
        throws BadRequestException, AlreadyTakenException {
        //check userdata for user name

        if (registerRequestObject == null || registerRequestObject.username() == null || registerRequestObject.password() == null || registerRequestObject.email() == null) {
            throw new BadRequestException("Error: the given register information is incomplete");
        }


        String givenUsername = registerRequestObject.username();
        UserData possibleUser = userDAO.getUser(givenUsername);

        if (possibleUser != null) {
            throw new AlreadyTakenException("Error: an account with this username already exists");
        }


        //if null create a new one

        UserData newUserData = new UserData(registerRequestObject.username(), registerRequestObject.password(), registerRequestObject.email());
        userDAO.createUser(newUserData);
        //create an authToken
        String authToken = UUID.randomUUID().toString();
        AuthData newAuthData = new AuthData(authToken,givenUsername);
        authDAO.createAuth(newAuthData);

        //turn authdata to registerresult
        RegisterResult myResult = new RegisterResult(givenUsername,authToken);
        return myResult;

        //if not null then return user already created error


        //with userdata create authdata

        //create new authToken

        //use authToken and username to make authdata

        //turn authdata to registerresult
    }

    public LoginResult login(LoginRequest loginRequestObject) throws BadRequestException, UnauthorizedException{
        if (loginRequestObject == null || loginRequestObject.username() == null || loginRequestObject.password() == null) {
            throw new BadRequestException("Error: the given login information is incomplete");
        }

        String givenUsername = loginRequestObject.username();
        UserData possibleUser = userDAO.getUser(givenUsername);
        if (possibleUser == null) {
            throw new UnauthorizedException("Error: we could not find an account with that username. would you like to create an account?");
        }

        //is the password the one that matches the username?

        String givenPassword = loginRequestObject.password();
        if (!givenPassword.equals(possibleUser.password())) {
            throw new UnauthorizedException("Error: the password doesn't match the one we have on file. would you like to recover your password?");
        }
        else {
            String authToken = UUID.randomUUID().toString();
            AuthData newAuth = new AuthData(authToken,givenUsername);
            authDAO.createAuth(newAuth);
            LoginResult myResult = new LoginResult(givenUsername,authToken);
            return myResult;
        }

    }
    public void logout(LogoutRequest logoutRequestObject) throws UnauthorizedException {
        String authToken = logoutRequestObject.authToken();
        AuthData possibleAuth = authDAO.getAuth(authToken);
        if (possibleAuth == null) {
            throw new UnauthorizedException("Error: you are not authorized to logout");
        }
        else {
            authDAO.deleteAuth(authToken);
        }
    }
}
