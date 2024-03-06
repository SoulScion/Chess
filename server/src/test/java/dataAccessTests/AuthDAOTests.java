package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {

    @Test
    public void createAuthPos() throws DataAccessException {

        try {
            AuthDAO authSQL = new SQLAuthDAO();
            AuthData userAuth = authSQL.createAuthData("user");
            Assertions.assertNotNull(authSQL.getAuthData(userAuth.authToken()));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void createAuthNeg() throws DataAccessException {

        try {
            AuthDAO authSQL = new SQLAuthDAO();
            AuthData userAuth = authSQL.createAuthData("user");
            Assertions.assertNotNull(authSQL.getAuthData(userAuth.authToken()));
            Assertions.assertThrows(DataAccessException.class, () -> authSQL.createAuthData(null));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void getAuthPos() throws DataAccessException {

        try {
            AuthDAO authSQL = new SQLAuthDAO();
            AuthData authAuth = authSQL.createAuthData("user");
            AuthData auth1Auth = authSQL.createAuthData("user-1");
            AuthData auth2Auth = authSQL.createAuthData("user-2");
            Assertions.assertNotNull(authSQL.getAuthData(authAuth.authToken()));
            Assertions.assertNotNull(authSQL.getAuthData(auth1Auth.authToken()));
            Assertions.assertNotNull(authSQL.getAuthData(auth2Auth.authToken()));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void getAuthNeg() throws DataAccessException {

        try {
            AuthDAO authSQL = new SQLAuthDAO();
            authSQL.createAuthData("user");
            authSQL.createAuthData("user-1");
            authSQL.createAuthData("user-2");
            Assertions.assertNull(authSQL.getAuthData("username1234"));
            Assertions.assertNull(authSQL.getAuthData("user1"));
            Assertions.assertNull(authSQL.getAuthData("user-5"));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void deleteAuthPos() throws DataAccessException {

        try {
            AuthDAO authSQL = new SQLAuthDAO();
            AuthData userAuth = authSQL.createAuthData("user");
            AuthData user1Auth = authSQL.createAuthData("user-1");
            authSQL.deleteAuthData(userAuth.authToken());
            authSQL.deleteAuthData(user1Auth.authToken());
            Assertions.assertNull(authSQL.getAuthData(userAuth.authToken()));
            Assertions.assertNull(authSQL.getAuthData(user1Auth.authToken()));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void deleteAuthNeg() throws DataAccessException {
        AuthDAO authSQL = new SQLAuthDAO();
        Assertions.assertThrows(DataAccessException.class, () -> authSQL.deleteAuthData(null));
    }

    @Test
    public void deleteAllAuthPos() throws DataAccessException {

        try {
            AuthDAO authSQL = new SQLAuthDAO();
            AuthData userAuth = authSQL.createAuthData("user");
            AuthData user1Auth = authSQL.createAuthData("user-1");
            AuthData user2Auth = authSQL.createAuthData("user-2");
            authSQL.deleteAllAuthData();
            Assertions.assertNull(authSQL.getAuthData(userAuth.authToken()));
            Assertions.assertNull(authSQL.getAuthData(user1Auth.authToken()));
            Assertions.assertNull(authSQL.getAuthData(user1Auth.authToken()));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

}
