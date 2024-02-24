package dataAccess;

import model.AuthData;

import java.util.Collection;

public interface UserDAO {
    AuthData createUserData(String username, String password, String email) throws DataAccessException;

    Collection<AuthData> listUserData() throws DataAccessException;

    AuthData getUserData(String username) throws DataAccessException;

    void deleteUserData(String username) throws DataAccessException;

    void deleteAllUserData() throws DataAccessException;

}
