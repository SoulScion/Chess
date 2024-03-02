package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException{
        configureDatabase();
    }

    private Collection<UserData> allUserData = new ArrayList<>();

    public void createUserData(String username, String password, String email) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        // var json = new Gson().toJson(UserData);
        executeUpdate(statement, username, password, email);



        // UserData newUser = new UserData(username, password, email);
        // allUserData.add(newUser);

        // return newUser;

    }

    public Collection<UserData> listUserData() throws DataAccessException{
        var result = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String userFromTable = rs.getString("username");
                        String passwordFromTable = rs.getString("password");
                        String emailFromTable = rs.getString("email");

                        result.add(new UserData(userFromTable, passwordFromTable, emailFromTable));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");
        }
        return result;
    }

    public UserData getUserData(String username) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String userFromTable = rs.getString("username");
                        String passwordFromTable = rs.getString("password");
                        String emailFromTable = rs.getString("email");

                        return new UserData(userFromTable, passwordFromTable, emailFromTable);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");
        }
        return null;


    //    for (UserData currentUserData : allUserData) {
    //        if (currentUserData.username().equals(username)) {
    //            return currentUserData;
    //        }
    //    }
    //    return null;

    }

    public void deleteAllUserData() {
        allUserData.clear();
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("ERROR: 500");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

    // `json` TEXT DEFAULT NULL,


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("ERROR: 500");
        }
    }


}
