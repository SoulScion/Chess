package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import request_result.RegisterResponse;

public class RegisterService {

    public RegisterResponse register(UserDAO userDAO, UserData data, AuthDAO auth) {
        try {
            userDAO.createUserData(data.username(), data.password(), data.email());
            AuthData authToken = auth.createAuthData(data.username());
            return new RegisterResponse(data.username(), authToken.authToken());
        } catch (DataAccessException error) {
            return new RegisterResponse("Error", "Error");
        }

    }


}
