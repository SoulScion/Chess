package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;

import model.GameData;
import request_result.FailureResponse;
import request_result.ListGamesRequest;
import request_result.ListGamesResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ListGamesService {
    private final GameDAO gameDAO;

    /**
     * Receives a GameDAO object to provide access to the game data
     * @param gameDAO GameDAO object providing access to the game data
     */
    public ListGamesService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public Object listGames(GameDAO gameDAO, String authToken, AuthDAO auth) throws DataAccessException {
        try {
            if (auth.getAuthData(authToken) == null) {
                return new FailureResponse("Error: unauthorized");
            }

            Collection<ListGamesRequest> gameData = new ArrayList<>();
            for (GameData element : gameDAO.listGameData()) {
                gameData.add(new ListGamesRequest(element.gameID(), element.whiteUsername(), element.blackUsername(), element.gameName()));
            }

            return new ListGamesResponse(gameData);
        } catch (DataAccessException error) {
            return new ListGamesResponse(null);
        }

    }
    
    public ConcurrentHashMap<Integer, GameData> getChessGameObjects() throws DataAccessException {
        Collection<GameData> listGames = gameDAO.listGameData();
        ConcurrentHashMap<Integer, GameData> gameData = new ConcurrentHashMap<>();
        for (var game : listGames) {
            gameData.put(game.gameID(), game);
        }

        return gameData;
    }
}
