package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request_result.FailureResponse;
import request_result.LoginRequest;
import request_result.RegisterResponse;

import java.util.Objects;

public class LoginService {
    private final AuthDAO authDAO;

    public LoginService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public Object login(UserDAO userDAO, LoginRequest data, AuthDAO auth) throws DataAccessException {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (userDAO.listUserData().isEmpty() || userDAO.getUserData(data.username()) == null || !encoder.matches(data.password(), userDAO.getUserData(data.username()).password())) {
                return new FailureResponse("Error: unauthorized");
            }

            AuthData authData = auth.createAuthData(data.username());
            return new RegisterResponse(data.username(), authData.authToken());
        } catch (DataAccessException error) {
            return new RegisterResponse("Error", "Error");
        }

    }

    public String getUser(String authToken) throws DataAccessException {
        return authDAO.getAuthData(authToken).username();
    }

}
