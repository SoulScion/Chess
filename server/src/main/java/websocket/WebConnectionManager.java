package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.LoadGameMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class WebConnectionManager {
    public final ConcurrentHashMap<String, WebConnection> webConnections = new ConcurrentHashMap<>();

    public void addConnection(String username, Session session) {
        var connection = new WebConnection(username, session);
        webConnections.put(username, connection);
    }

    public void removeConnection(String userName) {
        webConnections.remove(userName);
    }

    public void sendMessageConnection(String connectionPlayer, String connectionMessage) throws IOException {
        var conn = webConnections.get(connectionPlayer);
        if (conn.webSession.isOpen()) {
            conn.sessionSend(connectionMessage);
        }
    }

    public void broadcastChessGame(LoadGameMessage loadMessage) throws IOException {
        var removeList = new ArrayList<WebConnection>();

        for (var v : webConnections.values()) {
            if (v.webSession.isOpen()) {
                if (Objects.equals(v.username, loadMessage.getNewGame().blackUsername())) {
                    loadMessage.setNewColor(ChessGame.TeamColor.BLACK);
                } else {
                    loadMessage.setNewColor(ChessGame.TeamColor.WHITE);
                }
                v.sessionSend(new Gson().toJson(loadMessage));
            } else {
                removeList.add(v);
            }
        }

        for (var r : removeList) {
            webConnections.remove(r.username);
        }
    }

    public void generalBroadcast(String connectionPlayer, String connectionMessage) throws IOException {
        var removeList = new ArrayList<WebConnection>();
        for (var  v: webConnections.values()) {
            if (v.webSession.isOpen()) {
                if (!v.username.equals(connectionPlayer)) {
                    v.sessionSend(connectionMessage);
                }
            } else {
                removeList.add(v);
            }
        }

        for (var r : removeList) {
            webConnections.remove(r.username);
        }
    }


}
