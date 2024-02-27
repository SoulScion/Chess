package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import request_result.FailureResponse;
import request_result.RegisterResponse;
import request_result.ClearAllResponse;

public class ClearService {

    public Object clearAll(AuthDAO gameAuthDAO, GameDAO gameGameDAO, UserDAO gameUserDAO) throws DataAccessException {

        gameAuthDAO.deleteAllAuthData();
        gameGameDAO.deleteAllGameData();
        gameUserDAO.deleteAllUserData();
        return new ClearAllResponse(null);

    }

}
