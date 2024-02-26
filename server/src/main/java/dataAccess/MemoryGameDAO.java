package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {

    private Collection<GameData> allGameData = new ArrayList<>();

    private int gameID = 5;

    public GameData createGameData(String gameName) {
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        allGameData.add(newGame);
        gameID = gameID + 2;
        return newGame;
    }

    public void updateGameData(int givenGameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) {
        for (GameData currentGame : allGameData) {
            if (currentGame.gameID() == (givenGameID)) {
                currentGame = new GameData(givenGameID, whiteUsername, blackUsername, gameName, new ChessGame());
            }
        }

    }

    public Collection<GameData> listGameData() {
        return allGameData;
    }

    public GameData getGameData(String gameName) {
        for (GameData currentGame : allGameData) {
            if (currentGame.gameName().equals(gameName)) {
                return currentGame;
            }
        }
        return null;
    }

    public void deleteAllGameData() {
        for (GameData currentGame : allGameData) {
            allGameData.remove(currentGame);
        }
    }
}
