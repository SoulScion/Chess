package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import request_result.CreateGameResponse;
import request_result.FailureResponse;

public class CreateGameService {
    private final GameDAO gameDAO;

    public CreateGameService(GameDAO recievedgameDAO) {
        this.gameDAO = recievedgameDAO;
    }

    public Object createGame(GameDAO gameDAO, GameData data, String authToken, AuthDAO auth) throws DataAccessException {
        try {
            if (data.gameName() == null || authToken == null) {
                return new FailureResponse("Error: bad request");
            }

            if (auth.getAuthData(authToken) == null) {
                return new FailureResponse("Error: unauthorized");
            }

            int gameDataID = gameDAO.createGameData(data.gameName());

            return new CreateGameResponse(gameDataID);
        } catch (DataAccessException error) {
            return new CreateGameResponse(0);
        }

    }

    public GameData getGameInCreateService(Integer gameID) throws DataAccessException {
        return gameDAO.getGameData(gameID);
    }

    public void updateGameInCreateService(GameData gameData) throws DataAccessException {
        gameDAO.updateGameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
    }
}
