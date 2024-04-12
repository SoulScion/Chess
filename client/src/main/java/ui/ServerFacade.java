package ui;

import errorExceptions.ServerResponseException;
import model.AuthData;
import model.UserData;
import model.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
// import java.util.Objects;


public class ServerFacade {

    private String authToken;
    private String facadeURL;

    public ServerFacade(String givenURL) {
        facadeURL = givenURL;
    }

    public AuthData register(UserData user) throws ClientAccessException {
        var pathURL = "/user";
        AuthData auth = this.requestOrder("POST", pathURL, user, AuthData.class);
        if (auth != null) {
            authToken = auth.authToken();
        }

        return auth;
    }

    public AuthData login(UserData user) throws ClientAccessException {
        var pathURL = "/session";
        AuthData auth = this.requestOrder("POST", pathURL, user, AuthData.class);
        if (auth != null) {
            authToken = auth.authToken();
        }

        return auth;
    }

    public void logout() throws ClientAccessException {
        var pathURL = "/session";
        this.requestOrder("DELETE", pathURL, null, null);
        authToken = null;
    }

    public GameID createGame(GameName chessGameName) throws ClientAccessException {
        var pathURL = "/game";
        return this.requestOrder("POST", pathURL, chessGameName, GameID.class);
    }

    public void joinGame(JoinRequest joinInfo) throws ClientAccessException {
        var pathURL = "/game";
        this.requestOrder("PUT", pathURL, joinInfo, null);
    }

    public ArrayList<GameDataResponse> listGames() throws ClientAccessException {
        var pathURL = "/objects";
        return this.requestOrder("GET", pathURL, null, ListAllGames.class).games();
    }

    public ConcurrentHashMap<Integer, GameData> getObjectsGame() throws ClientAccessException {
        var pathURL = "/game";
        return this.requestOrder("GET", pathURL, null, GamesObjects.class).gameObjects();
    }

    public void clear() throws ClientAccessException {
        var pathURL = "/db";
        this.requestOrder("DELETE", pathURL,null, null);
    }


    private <T> T requestOrder(String requestType, String pathURL, Object requestData, Class<T> classResponse) throws ClientAccessException {
        try {
            URL newURL = (new URI(facadeURL + pathURL)).toURL();
            HttpURLConnection httpConnection = (HttpURLConnection) newURL.openConnection();
            httpConnection.setRequestMethod(requestType);
            httpConnection.setDoOutput(true);
            if (authToken != null) {
                httpConnection.setRequestProperty("authorization", authToken);
            }

            //if (requestData instanceof AuthData || requestData instanceof GameID || ((Objects.equals(requestType, "GET") || Objects.equals(requestType, "PUT")) && Objects.equals(pathURL, "/game"))) {
            //    httpConnection.setRequestProperty("authorization", authToken);
            //}

            writeRequestBody(requestData, httpConnection);
            httpConnection.connect();
            failureThrow(httpConnection);
            return readRequestBody(httpConnection, classResponse);
        } catch (Exception error){
            throw new ClientAccessException("ERROR: 500");
        }
    }

    private void writeRequestBody(Object requestData, HttpURLConnection httpConnection) throws IOException {
        if (requestData != null) {
            httpConnection.addRequestProperty("Content-Type", "application/json");
            String gsonRequest = new Gson().toJson(requestData);
            try (OutputStream requestBody = httpConnection.getOutputStream()) {
                requestBody.write(gsonRequest.getBytes());
            }
        }
    }

    private static <T> T readRequestBody(HttpURLConnection httpConnection, Class<T> classResponse) throws IOException {
        T newResponse = null;
        if (httpConnection.getContentLength() < 0) {
            try (InputStream requestBody = httpConnection.getInputStream()) {
                InputStreamReader inputReader = new InputStreamReader(requestBody);
                if (classResponse != null) {
                    newResponse = new Gson().fromJson(inputReader, classResponse);
                }
            }
        }
        return newResponse;
    }

    private void failureThrow(HttpURLConnection httpConnection) throws IOException, ClientAccessException {
        var responseCode = httpConnection.getResponseCode();
        if (responseSuccessful(responseCode) != true) {
            throw new ClientAccessException("Error: " + responseCode);
        }

    }

    private boolean responseSuccessful(int responseCode) {
        if (responseCode / 100 == 2) {
            return true;
        } else {
            return false;
        }
    }


















}
