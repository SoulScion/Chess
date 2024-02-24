package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import request_result.FailureResponse;
import request_result.LoginRequest;
import request_result.RegisterResponse;

import java.util.Objects;

public class LoginService {
    public Object login(UserDAO userDAO, LoginRequest data, AuthDAO auth) throws DataAccessException {
        try {
            if (!Objects.equals(userDAO.getUserData(data.username()).password(), data.password())) {
                return new FailureResponse("Error: unuauthorized");
            }

            AuthData authData = auth.createAuthData(data.username());
            return new RegisterResponse(data.username(), authData.authToken());
        } catch (DataAccessException error) {
            return new RegisterResponse("Error", "Error");
        }

    }
}
