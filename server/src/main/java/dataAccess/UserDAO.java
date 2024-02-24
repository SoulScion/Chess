package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {
    UserData createUserData(String username, String password, String email) throws DataAccessException;

    Collection<UserData> listUserData() throws DataAccessException;

    UserData getUserData(String username) throws DataAccessException;

    void deleteAllUserData() throws DataAccessException;

}
