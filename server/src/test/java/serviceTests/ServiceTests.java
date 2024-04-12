package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.RegisterService;
import service.*;
import request_result.*;

public class ServiceTests {

    @Test
    public void registerPositive() {
        RegisterService registeringService = new RegisterService();
        AuthDAO auth = new MemoryAuthDAO();
        UserDAO user = new MemoryUserDAO();
        UserData dataUser = new UserData("user", "password", "help@gmail.com");
        try {
            var regResponse = registeringService.register(user, dataUser, auth);
            if (regResponse instanceof RegisterResponse) {
                Assertions.assertNotNull(regResponse);
            }
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void registerNegative() {
        RegisterService registeringService = new RegisterService();
        AuthDAO auth = new MemoryAuthDAO();
        UserDAO user = new MemoryUserDAO();
        UserData dataUser = new UserData("user", "passwor", "help@gmail.com");
        UserData dataUser2 = new UserData("user", "passwor", "help@gmail.com");
        try {
            var regResponse = registeringService.register(user, dataUser, auth);
            var regResponse2 = registeringService.register(user, dataUser, auth);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(FailureResponse.class, regResponse2);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void loginPositive() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        LoginService loginService = new LoginService(authDAO);
        try {

            userDAO.createUserData("username", "password", "email-1");





            var regResponse = loginService.login(userDAO, new LoginRequest("username", "password"), authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(RegisterResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void loginNegative() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        LoginService loginService = new LoginService(authDAO);
        try {

            userDAO.createUserData("username", "password", "email-1");





            var regResponse = loginService.login(userDAO, new LoginRequest("username", "pass"), authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(FailureResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void logoutPositive() {
        LogoutService logoutService = new LogoutService();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        try {

            userDAO.createUserData("username", "password", "email-1");
            AuthData authToken = authDAO.createAuthData("username");





            var regResponse = logoutService.logout(authToken.authToken(),authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(ClearAllResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void logoutNegative() {
        LogoutService logoutService = new LogoutService();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        try {

            userDAO.createUserData("username", "password", "email-1");
            AuthData authToken = authDAO.createAuthData("username");





            var regResponse = logoutService.logout("wrongToken",authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(FailureResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void listGamesPositive() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        ListGamesService listGamesService = new ListGamesService(gameDAO);
        try {

            gameDAO.createGameData("user");
            gameDAO.createGameData("user-1");
            gameDAO.createGameData("user-2");
            gameDAO.createGameData("user-3");
            gameDAO.createGameData("user-4");
            AuthData authToken = authDAO.createAuthData("user-1");


            var regResponse = listGamesService.listGames(gameDAO, authToken.authToken(), authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(ListGamesResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void listGamesNegative() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        ListGamesService listGamesService = new ListGamesService(gameDAO);
        try {

            gameDAO.createGameData("user");
            gameDAO.createGameData("user-1");
            gameDAO.createGameData("user-2");
            gameDAO.createGameData("user-3");
            gameDAO.createGameData("user-4");
            AuthData authToken = authDAO.createAuthData("user-1");


            var regResponse = listGamesService.listGames(gameDAO, "wrongToken", authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(FailureResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void CreateGamePositive() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        CreateGameService createGameService = new CreateGameService(gameDAO);
        try {

            AuthData authToken = authDAO.createAuthData("username");
            int game = gameDAO.createGameData("username");
            GameData gameData = gameDAO.getGameData(5);





            var regResponse = createGameService.createGame(gameDAO, gameData, authToken.authToken(), authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(CreateGameResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void CreateGameNegative() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        CreateGameService createGameService = new CreateGameService(gameDAO);
        try {

            AuthData authToken = authDAO.createAuthData("username");
            int game = gameDAO.createGameData("username");
            GameData gameData = gameDAO.getGameData(5);





            var regResponse = createGameService.createGame(gameDAO, gameData, "wrongToken", authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(FailureResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void JoinGamePositive() {
        JoinGameService joinGameService = new JoinGameService();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        try {

            AuthData authToken = authDAO.createAuthData("username");
            int game = gameDAO.createGameData("username");






            var regResponse = joinGameService.joinGame(gameDAO, new JoinGameRequest("WHITE", 5), authToken.authToken(), authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(ClearAllResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }

    @Test
    public void JoinGameNegative() {
        JoinGameService joinGameService = new JoinGameService();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        try {

            AuthData authToken = authDAO.createAuthData("username");
            int game = gameDAO.createGameData("username");






            var regResponse = joinGameService.joinGame(gameDAO, new JoinGameRequest("WHITE", 5), "wrongToken", authDAO);
            //if (regResponse2 instanceof FailureResponse) {
            //    Assertions.assertNotNull(regResponse);
            //}
            Assertions.assertInstanceOf(FailureResponse.class, regResponse);
        } catch (DataAccessException Error) {
            return;
        }

    }





    @Test
    public void clearPositive() {
        ClearService clearingService = new ClearService();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        GameDAO emptyGameDAO = new MemoryGameDAO();
        UserDAO emptyUserDAO = new MemoryUserDAO();
        AuthDAO emptyAuthDAO = new MemoryAuthDAO();

        try {
            userDAO.createUserData("user", "pass", "email");
            userDAO.createUserData("user-2", "pass-2", "email-2");
            authDAO.createAuthData("authUser");
            authDAO.createAuthData("authUser-2");
            gameDAO.createGameData("gameUser");
            gameDAO.createGameData("gameUser-2");

            var regResponse = clearingService.clearAll(authDAO, gameDAO, userDAO);

            Assertions.assertInstanceOf(ClearAllResponse.class, regResponse);

        } catch (DataAccessException Error) {
            return;
        }

    }
















}
