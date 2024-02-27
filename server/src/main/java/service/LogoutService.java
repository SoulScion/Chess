package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import request_result.*;

import java.util.Objects;

public class LogoutService {

    public Object logout(String authToken, AuthDAO auth) throws DataAccessException {
        try {
            if (auth.getAuthData(authToken) == null) {
                return new FailureResponse("Error: unauthorized");
            }

            auth.deleteAuthData(authToken);
            return new ClearAllResponse(null);
        } catch (DataAccessException error) {
            return new RegisterResponse("Error", "Error");
        }

    }

}
