package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import request_result.CreateGameResponse;
import request_result.FailureResponse;
import request_result.ListGamesRequest;
import request_result.ListGamesResponse;

import java.util.Collection;

public class CreateGameService {

    public Object createGame(GameDAO gameDAO, GameData data, String authToken, AuthDAO auth) throws DataAccessException {
        try {
            if (data.gameName() == null || authToken == null) {
                return new FailureResponse("Error: bad request");
            }

            if (auth.getAuthData(authToken) == null) {
                return new FailureResponse("Error: unauthorized");
            }

            int gameDataID = gameDAO.createGameData(data.gameName()).gameID();

            return new CreateGameResponse(gameDataID);
        } catch (DataAccessException error) {
            return new CreateGameResponse(0);
        }

    }
}
