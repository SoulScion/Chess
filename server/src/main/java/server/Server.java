package server;

import com.google.gson.Gson;
import model.UserData;
import request_result.LogoutRequest;
import spark.*;
import dataAccess.*;
import service.*;
import request_result.LoginRequest;

import java.io.Reader;


public class Server {

    private GameDAO memoryGameDAO = new MemoryGameDAO();

    private UserDAO memoryUserDAO = new MemoryUserDAO();

    private AuthDAO memoryAuthDAO = new MemoryAuthDAO();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.post("/user", this::registerMethod);
        Spark.post("/session", this::loginMethod);
        Spark.post("/session", this::logoutMethod);


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
        LogoutRequest auth = new Gson().fromJson(request.headers(), LogoutRequest.class);
        LogoutService service = new LogoutService();
        var regResponse = service.logout(auth, memoryAuthDAO);
        return new Gson().toJson(regResponse);

    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}



// AuthDAO aDAO = new MemoryAuthDAO
