package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import errorExceptions.ServerResponseException;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        return authDAO.getAuthData(authToken);
    }

}
