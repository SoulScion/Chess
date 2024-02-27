package service;

import dataAccess.*;
import request_result.ClearAllResponse;

public class ClearService {

    public Object clearAll(AuthDAO gameAuthDAO, GameDAO gameGameDAO, UserDAO gameUserDAO) throws DataAccessException {

        gameAuthDAO.deleteAllAuthData();
        gameGameDAO.deleteAllGameData();
        gameUserDAO.deleteAllUserData();
        return new ClearAllResponse(null);

    }

}
