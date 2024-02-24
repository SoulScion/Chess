package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO {

    final private Collection<GameData> allGameData = new ArrayList<>();

    private int gameID = 5;

    GameData createGameData(String gameName) {
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        allGameData.add(newGame);
        return newGame;
    }

    void updateGameData(int givenGameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) {
        for (GameData currentGame : allGameData) {
            if (currentGame.gameID() == (givenGameID)) {
                currentGame = new GameData(givenGameID, whiteUsername, blackUsername, gameName, new ChessGame());
            }
        }

    }

    Collection<GameData> listGameData() {
        return allGameData;
    }

    GameData getGameData(String gameName) {
        for (GameData currentGame : allGameData) {
            if (currentGame.gameName().equals(gameName)) {
                return currentGame;
            }
        }
        return null;
    }

    void deleteGameData(String gameName) {
        for (GameData currentGame : allGameData) {
            if (currentGame.gameName().equals(gameName)) {
                allGameData.remove(currentGame);
            }
        }
    }

    void deleteAllAuthData() {
        for (GameData currentGame : allGameData) {
            allGameData.remove(currentGame);
        }
    }
}
