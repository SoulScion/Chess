package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import errorExceptions.ServerResponseException;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    /**
     * Constructor, accepts an AuthDAO to use to access the authorization database
     * @param authDAO AuthDAO object providing access to the authorization database
     */
    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public boolean authenticate(String authToken) throws ServerResponseException, DataAccessException {
        if (this.authDAO.getAuthData(authToken) == null) {
            throw new ServerResponseException(401, "Error: unauthorized");
        } else {
            return true;
        }
    }

    public AuthData getAuthData(String authToken) throws DataAccessException {
        return authDAO.getAuthData(authToken);
    }

}
