package dataAccess;

import model.AuthData;

import java.util.Collection;

public interface GameDAO {
    AuthData createGameData(String username) throws DataAccessException;

    Collection<AuthData> listGameData() throws DataAccessException;

    AuthData getGameData(int gameID) throws DataAccessException;

    void deleteGameData(int gameID) throws DataAccessException;

    void deleteAllGameData() throws DataAccessException;

}
