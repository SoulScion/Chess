package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import model.GameData;
import model.UserData;
import request_result.ListGamesRequest;
import request_result.LogoutRequest;
import spark.*;
import dataAccess.*;
import service.*;
import request_result.LoginRequest;

import java.io.Reader;
import java.util.Set;


public class Server {

    private GameDAO memoryGameDAO = new MemoryGameDAO();

    private UserDAO memoryUserDAO = new MemoryUserDAO();

    private AuthDAO memoryAuthDAO = new MemoryAuthDAO();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.post("/user", this::registerMethod);
        Spark.post("/session", this::loginMethod);
        Spark.delete("/session", this::logoutMethod);
        Spark.get("/game", this::listGamesMethod);
        Spark.post("/game", this::createGameMethod);


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
        return new Gson().toJson(regResponse);

    }

    public Object loginMethod(Request request, Response response) throws DataAccessException {
        //  get from the body of the request, get the Json, convert that into your own request
        // immediately call service on the next line with the thing you just got.
        // Service should return a result class

        // loginRequest user = new Gson().

        LoginRequest dataUser = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginService service = new LoginService();
        var regResponse = service.login(memoryUserDAO, dataUser, memoryAuthDAO);
        return new Gson().toJson(regResponse);

    }

    // FIXME: FIND A SOLUTION ON HOW TO GRAB THE HEADER FOR LOGOUT.
    public Object logoutMethod(Request request, Response response) throws DataAccessException {

        Set<String> headers = request.headers();
        String header = request.headers("authorization");

        // LogoutRequest auth = new Gson().fromJson(request.headers("authorization"), LogoutRequest.class);
        LogoutService service = new LogoutService();
        var regResponse = service.logout(header, memoryAuthDAO);
        return new Gson().toJson(regResponse);

    }

    public Object listGamesMethod(Request request, Response response) throws DataAccessException {

        String header = request.headers("authorization");

        //GameData dataGame = new Gson().fromJson(request.body(), GameData.class);
        ListGamesService service = new ListGamesService();
        var regResponse = service.listGames(memoryGameDAO, header, memoryAuthDAO);
        return new Gson().toJson(regResponse);

    }

    public Object createGameMethod(Request request, Response response) throws DataAccessException {

        String header = request.headers("authorization");

        GameData dataGame = new Gson().fromJson(request.body(), GameData.class);
        ListGamesService service = new ListGamesService();
        var regResponse = service.listGames(memoryGameDAO, dataGame, header, memoryAuthDAO);
        return new Gson().toJson(regResponse);

    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}



// AuthDAO aDAO = new MemoryAuthDAO
