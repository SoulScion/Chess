package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import request_result.FailureResponse;
import request_result.ListGamesRequest;
import request_result.ListGamesResponse;
import request_result.RegisterResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ListGamesService {

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
}
