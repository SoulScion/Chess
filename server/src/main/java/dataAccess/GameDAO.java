package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGameData(String username) throws DataAccessException;

    void updateGameData(int givenGameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) throws DataAccessException;

    Collection<GameData> listGameData() throws DataAccessException;

    GameData getGameData(int gameID) throws DataAccessException;

    void deleteAllGameData() throws DataAccessException;

}
