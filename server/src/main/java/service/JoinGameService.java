package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import request_result.ClearAllResponse;
import request_result.CreateGameResponse;
import request_result.FailureResponse;
import request_result.JoinGameRequest;

import java.util.Objects;

public class JoinGameService {
    public Object joinGame(GameDAO gameDAO, JoinGameRequest data, String authToken, AuthDAO auth) throws DataAccessException {
        try {
            if (data.gameID() == 0 || authToken == null) {
                return new FailureResponse("Error: bad request");
            }

            if (auth.getAuthData(authToken) == null) {
                return new FailureResponse("Error: unauthorized");
            }

            if (Objects.equals(data.playerColor(), "WHITE") && gameDAO.getGameData(data.gameID()).whiteUsername() != null) {
                return new FailureResponse("Error: already taken");
            }

            if (Objects.equals(data.playerColor(), "BLACK") && gameDAO.getGameData(data.gameID()).blackUsername() != null) {
                return new FailureResponse("Error: already taken");
            }

            if (data.playerColor() == null) {
                return new ClearAllResponse(null);
            }

            // int gameDataID = gameDAO.createGameData(data.gameName()).gameID();
            GameData updatedGame = gameDAO.getGameData(data.gameID());
            AuthData givenAuthData = auth.getAuthData(authToken);
            if (data.playerColor().equals("WHITE")) {
                gameDAO.updateGameData(updatedGame.gameID(), givenAuthData.username(), updatedGame.blackUsername(), updatedGame.gameName(), updatedGame.game());
            }
            if (data.playerColor().equals("BLACK")) {
                gameDAO.updateGameData(updatedGame.gameID(), updatedGame.whiteUsername(), givenAuthData.username(), updatedGame.gameName(), updatedGame.game());
            }

            return new ClearAllResponse(null);
        } catch (DataAccessException error) {
            return new CreateGameResponse(0);
        }

    }


}
