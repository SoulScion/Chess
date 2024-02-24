package service;

import dataAccess.*;
import model.UserData;
import request_result.RegisterResponse;

public class RegisterService {

    public RegisterResponse register(UserDAO userDAO, UserData data, AuthDAO auth) {
        try {
            userDAO.createUserData(data.username(), data.password(), data.email());
            auth.createAuthData(data.username());
        } catch (DataAccessException error) {
            return new RegisterResponse("Error", "Error");
        }

        return null;

    }


}
