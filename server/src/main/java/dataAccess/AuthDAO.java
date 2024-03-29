package dataAccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    AuthData createAuthData(String username) throws DataAccessException;

    AuthData getAuthData(String authToken) throws DataAccessException;

    void deleteAuthData(String authToken) throws DataAccessException;

    void deleteAllAuthData() throws DataAccessException;
}
