package dataAccess;

import model.AuthData;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO {

    private Collection<AuthData> allAuthData = new ArrayList<>();

    public SQLAuthDAO() throws DataAccessException{
        configureDatabase();
    }

    public AuthData createAuthData(String username) throws DataAccessException{

        SecureRandom randomCode = new SecureRandom();
        byte numbers[] = new byte[20];
        randomCode.nextBytes(numbers);
        String authToken = numbers.toString();

        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authToken, username);

        return new AuthData(authToken, username);
    }

    public AuthData getAuthData(String authToken) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM authData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String authFromTable = rs.getString("authToken");
                        String userFromTable = rs.getString("username");

                        return new AuthData(authFromTable, userFromTable);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");
        }
        return null;
    }

    public void deleteAuthData(String authToken) throws DataAccessException{
        if (authToken == null) {
            throw new DataAccessException("ERROR: 500");
        }
        var command = "DELETE FROM authData WHERE authToken=?";
        executeUpdate(command, authToken);
    }

    public void deleteAllAuthData() throws DataAccessException{
        var command = "TRUNCATE authData";
        executeUpdate(command);
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
            CREATE TABLE IF NOT EXISTS authData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };

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
