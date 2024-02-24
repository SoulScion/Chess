package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import request_result.RegisterResponse;
import request_result.FailureResponse;

public class RegisterService {

    public Object register(UserDAO userDAO, UserData data, AuthDAO auth) throws DataAccessException {
        if (data.email() == null || data.username() == null || data.password() == null) {
            return new FailureResponse("Error: bad request");
        }

        if (userDAO.getUserData(data.username()) != null) {
            return new FailureResponse("Error: already taken");
        }


        try {
            userDAO.createUserData(data.username(), data.password(), data.email());
            AuthData authData = auth.createAuthData(data.username());
            return new RegisterResponse(data.username(), authData.authToken());
        } catch (DataAccessException error) {
            return new RegisterResponse("Error", "Error");
        }

    }




}
