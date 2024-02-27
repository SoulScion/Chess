package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {

    private ArrayList<GameData> allGameData = new ArrayList<>();

    private int gameID = 5;

    public GameData createGameData(String gameName) {
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        allGameData.add(newGame);
        gameID = gameID + 2;
        return newGame;
    }

    public void updateGameData(int givenGameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) {
        for (int i = 0; i < allGameData.size(); i++) {
            if (allGameData.get(i).gameID() == givenGameID) {
                allGameData.set(i, new GameData(givenGameID, whiteUsername, blackUsername, gameName, new ChessGame()));
            }
        }

    }

    public Collection<GameData> listGameData() {
        return allGameData;
    }

    public GameData getGameData(int gameID) {
        for (GameData currentGame : allGameData) {
            if (currentGame.gameID() == (gameID)) {
                return currentGame;
            }
        }
        return null;
    }

    public void deleteAllGameData() {
        allGameData.clear();
    }
}
