package service;

import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
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
            throw new BadRequestException("the given register information is incomplete");
        }



        String givenUsername = registerRequestObject.username();
        UserData possibleUser = userDAO.getUser(givenUsername);

        if (possibleUser != null) {
            throw new AlreadyTakenException("an account with this username already exists");
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
}
