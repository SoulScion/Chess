package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import errorExceptions.ServerResponseException;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{
    Session webSession;
    NotifyHandler notifyHandler;

    public WebSocketFacade(String url, NotifyHandler notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notifyHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.webSession = container.connectToServer(this, socketURI);

            this.webSession.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String s) {
                    JsonObject obj = JsonParser.parseString(s).getAsJsonObject();
                    ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.valueOf(obj.get("serverMessageType").getAsString());
                    switch (type) {
                        case LOAD_GAME -> notificationHandler.loadGameMessage(new Gson().fromJson(s, LoadGameMessage.class));
                        case ERROR -> notificationHandler.errorMessage(new Gson().fromJson(s, ErrorMessage.class));
                        case NOTIFICATION -> notificationHandler.notifyMessage(new Gson().fromJson(s, NotifyMessage.class));
                    }
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException error) {
            throw new RuntimeException(error);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor color) throws ServerResponseException {
        try {
            if (color == null) {
                var command = new ObserverJoinCommand(authToken, gameID);
                send(new Gson().toJson(command));
            } else {
                var command = new PlayerJoinCommand(authToken, gameID, color);
                send(new Gson().toJson(command));
            }
        } catch (IOException e) {
            throw new ServerResponseException(500, e.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ServerResponseException {
        try {
            var command = new MakeMoveCommand(authToken, gameID, move);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ServerResponseException(500, e.getMessage());
        }
    }

    public void getGame(String authToken, int gameID) throws ServerResponseException {
        try {
            var command = new GetGameCommand(authToken, gameID);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ServerResponseException(500, e.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws ServerResponseException {
        try {
            var command = new ResignCommand(authToken, gameID);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ServerResponseException(500, e.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) throws ServerResponseException {
        try {
            var command = new LeaveCommand(authToken, gameID);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ServerResponseException(500, e.getMessage());
        }
    }

    public void send(String msg) throws IOException {
        this.webSession.getBasicRemote().sendText(msg);
    }
}

