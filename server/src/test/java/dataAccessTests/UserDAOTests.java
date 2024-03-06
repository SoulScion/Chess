package dataAccessTests;
import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDAOTests {



    @Test
    public void createUserNeg() throws DataAccessException {

        try {
            UserDAO userSQL = new SQLUserDAO();
            userSQL.createUserData("user", "pass", "email");
            Assertions.assertThrows(DataAccessException.class, () -> userSQL.createUserData("user", "pass", "email"));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void listUsersPos() throws DataAccessException {

        try {
            UserDAO userSQL = new SQLUserDAO();
            userSQL.createUserData("user", "pass", "email");
            userSQL.createUserData("user-1", "pass-1", "email-1");
            userSQL.createUserData("user-2", "pass-2", "email-2");
            userSQL.createUserData("user-3", "pass-3", "email-3");
            Assertions.assertEquals(userSQL.listUserData().size(), 4);

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void listUsersNeg() throws DataAccessException {

        UserDAO userSQL = null;
        try {
            userSQL = new SQLUserDAO();
            userSQL.createUserData("user", "pass", "email");
            userSQL.createUserData("user-1", "pass-1", "email-1");
            userSQL.createUserData("user-2", "pass-2", "email-2");
            userSQL.createUserData("user-3", "pass-3", "email-3");
            userSQL.createUserData(null, null, null);

        } catch (Exception e) {
            Assertions.assertEquals(userSQL.listUserData().size(), 4);
        }
    }

    @Test
    public void getUserPos() throws DataAccessException {

        try {
            UserDAO userSQL = new SQLUserDAO();
            userSQL.createUserData("user", "pass", "email");
            userSQL.createUserData("user-1", "pass-1", "email-1");
            userSQL.createUserData("user-2", "pass-2", "email-2");
            Assertions.assertNotNull(userSQL.getUserData("user"));
            Assertions.assertNotNull(userSQL.getUserData("user-1"));
            Assertions.assertNotNull(userSQL.getUserData("user-2"));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void getUserNeg() throws DataAccessException {

        try {
            UserDAO userSQL = new SQLUserDAO();
            userSQL.createUserData("user", "pass", "email");
            userSQL.createUserData("user-1", "pass-1", "email-1");
            userSQL.createUserData("user-2", "pass-2", "email-2");
            Assertions.assertNull(userSQL.getUserData("username1234"));
            Assertions.assertNull(userSQL.getUserData("user1"));
            Assertions.assertNull(userSQL.getUserData("user-3"));

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }

    @Test
    public void deleteAllUserPos() throws DataAccessException {

        try {
            UserDAO userSQL = new SQLUserDAO();
            userSQL.createUserData("user", "pass", "email");
            userSQL.createUserData("user-1", "pass-1", "email-1");
            userSQL.createUserData("user-2", "pass-2", "email-2");
            userSQL.createUserData("user-3", "pass-3", "email-3");
            userSQL.createUserData("user-4", "pass-4", "email-4");
            userSQL.deleteAllUserData();
            Assertions.assertEquals(userSQL.listUserData().size(), 0);

        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");

        }
    }






}
