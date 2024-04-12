package server;

import com.google.gson.Gson;
import errorExceptions.ServerResponseException;
import model.GameData;
import model.UserData;
import request_result.*;
import spark.*;
import dataAccess.*;
import service.*;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class Server {


    private GameDAO memoryGameDAO;

    private UserDAO memoryUserDAO;

    private AuthDAO memoryAuthDAO;

    private void makeDAOs() {
        try {

            memoryGameDAO = new SQLGameDAO();

            memoryUserDAO = new SQLUserDAO();

            memoryAuthDAO = new SQLAuthDAO();

        } catch (DataAccessException error) {
            throw new RuntimeException(error);
        }
    }


    public int run(int desiredPort) {
        makeDAOs();
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.post("/user", this::registerMethod);
        Spark.post("/session", this::loginMethod);
        Spark.delete("/session", this::logoutMethod);
        Spark.get("/game", this::listGamesMethod);
        Spark.get("/objects", this::gameObjects);
        Spark.post("/game", this::createGameMethod);
        Spark.put("/game", this::joinGameMethod);
        Spark.delete("/db", this::clearAllMethod);


        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public Object registerMethod(Request request, Response response) throws DataAccessException {
        //  get from the body of the request, get the Json, convert that into your own request
        // immediately call service on the next line with the thing you just got.
        // Service should return a result class

        // loginRequest user = new Gson().

        UserData dataUser = new Gson().fromJson(request.body(), UserData.class);
        RegisterService service = new RegisterService();

        var regResponse = service.register(memoryUserDAO, dataUser, memoryAuthDAO);
        // register.instanceof()

        if (regResponse instanceof FailureResponse) {
            if (Objects.equals(((FailureResponse) regResponse).message(), "Error: bad request")) {
                response.status(400);
            } else {
                response.status(403);
            }
        } else {
            response.status(200);
        }


        return new Gson().toJson(regResponse);

    }

    public Object loginMethod(Request request, Response response) throws DataAccessException {
        //  get from the body of the request, get the Json, convert that into your own request
        // immediately call service on the next line with the thing you just got.
        // Service should return a result class

        // loginRequest user = new Gson().

        LoginRequest dataUser = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginService service = new LoginService(memoryAuthDAO);
        var regResponse = service.login(memoryUserDAO, dataUser, memoryAuthDAO);

        if (regResponse instanceof FailureResponse) {
            if (Objects.equals(((FailureResponse) regResponse).message(), "Error: unauthorized")) {
                response.status(401);
            }
        } else {
            response.status(200);
        }


        return new Gson().toJson(regResponse);

    }

    public Object logoutMethod(Request request, Response response) throws DataAccessException {

        Set<String> headers = request.headers();
        String header = request.headers("authorization");

        // LogoutRequest auth = new Gson().fromJson(request.headers("authorization"), LogoutRequest.class);
        LogoutService service = new LogoutService();
        var regResponse = service.logout(header, memoryAuthDAO);

        if (regResponse instanceof FailureResponse) {
            if (Objects.equals(((FailureResponse) regResponse).message(), "Error: unauthorized")) {
                response.status(401);
            }
        } else {
            response.status(200);
        }


        return new Gson().toJson(regResponse);

    }

    public Object listGamesMethod(Request request, Response response) throws DataAccessException {

        String header = request.headers("authorization");

        //GameData dataGame = new Gson().fromJson(request.body(), GameData.class);
        ListGamesService service = new ListGamesService(memoryGameDAO);
        var regResponse = service.listGames(memoryGameDAO, header, memoryAuthDAO);

        if (regResponse instanceof FailureResponse) {
            if (Objects.equals(((FailureResponse) regResponse).message(), "Error: unauthorized")) {
                response.status(401);
            }
        } else {
            response.status(200);
        }



        return new Gson().toJson(regResponse);

    }

    private Object gameObjects(Request request, Response response) throws ServerResponseException, DataAccessException {
        var authToken = request.headers("authorization");
        // AuthService.authenticate(authToken);

        ConcurrentHashMap<Integer, GameData> games;
        ListGamesService service = new ListGamesService(memoryGameDAO);
        games = ListGamesService.getChessGameObjects();

        response.status(200);
        response.body(new Gson().toJson(new GameObjects(games)));
        return new Gson().toJson(new GameObjects(games));
    }

    public Object createGameMethod(Request request, Response response) throws DataAccessException {

        String header = request.headers("authorization");

        GameData dataGame = new Gson().fromJson(request.body(), GameData.class);
        CreateGameService service = new CreateGameService(memoryGameDAO); //Changed this for phase 6
        var regResponse = service.createGame(memoryGameDAO, dataGame, header, memoryAuthDAO);

        if (regResponse instanceof FailureResponse) {
            if (Objects.equals(((FailureResponse) regResponse).message(), "Error: bad request")) {
                response.status(400);
            } else {
                response.status(401);
            }
        } else {
            response.status(200);
        }


        return new Gson().toJson(regResponse);

    }

    public Object joinGameMethod(Request request, Response response) throws DataAccessException {

        String header = request.headers("authorization");

        JoinGameRequest joinData = new Gson().fromJson(request.body(), JoinGameRequest.class);
        JoinGameService service = new JoinGameService();
        var regResponse = service.joinGame(memoryGameDAO, joinData, header, memoryAuthDAO);

        if (regResponse instanceof FailureResponse) {
            if (Objects.equals(((FailureResponse) regResponse).message(), "Error: bad request")) {
                response.status(400);
            } else if (Objects.equals(((FailureResponse) regResponse).message(), "Error: unauthorized")){
                response.status(401);
            } else {
                response.status(403);
            }
        } else {
            response.status(200);
        }


        return new Gson().toJson(regResponse);

    }

    public Object clearAllMethod(Request request, Response response) throws DataAccessException {

        // String header = request.headers("authorization");

        // GameData dataGame = new Gson().fromJson(request.body(), GameData.class);
        ClearService service = new ClearService();
        var regResponse = service.clearAll(memoryAuthDAO, memoryGameDAO, memoryUserDAO);
        response.status(200);
        return new Gson().toJson(regResponse);

    }





    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}



// AuthDAO aDAO = new MemoryAuthDAO
