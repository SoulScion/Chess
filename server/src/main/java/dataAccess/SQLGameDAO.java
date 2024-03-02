package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import chess.ChessGame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{

    private ArrayList<GameData> allGameData = new ArrayList<>();

    private int gameID = 5;

    public SQLGameDAO() throws DataAccessException{
        configureDatabase();
    }

    public int createGameData(String gameName) throws DataAccessException{
        var statement = "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        var gameInfo = new Gson().toJson(new ChessGame());
        return executeUpdate(statement, gameID, null, null, gameName, gameInfo);
    }

    public void updateGameData(int givenGameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var command = "SELECT gameID, whiteUsername, blackUsername, gameName, gameInfo FROM gameData WHERE gameID=?" + "WHERE gameID=?";
            try (var ps = conn.prepareStatement(command)) {
                ps.setInt(1, gameID);
                ps.setString(1, whiteUsername);
                ps.setString(1, blackUsername);
                ps.setString(1, gameName);
                ps.setString(1, new Gson().toJson(chessGame));
                ps.setInt(1, gameID);
                ps.executeUpdate();

            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");
        }
    }

    public Collection<GameData> listGameData() throws DataAccessException{
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameInfo FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");
        }
        return null;
    }

    public GameData getGameData(int gameID) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameInfo FROM gameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("ERROR: 500");
        }
        return null;
    }

    public void deleteAllGameData() throws DataAccessException{
        var command = "TRUNCATE gameData";
        executeUpdate(command);
    }

    private GameData readGameData(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var gameInfo = rs.getString("gameInfo");
        var chess = new Gson().fromJson(gameInfo, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, chess);
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
            CREATE TABLE IF NOT EXISTS gameData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `gameInfo` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
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
