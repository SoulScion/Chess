package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class WebConnection {
    public String username;
    public Session webSession;

    public WebConnection(String userName, Session session) {
        this.username = userName;
        this.webSession = session;
    }

    public void sessionSend(String message) throws IOException {
        webSession.getRemote().sendString(message);
    }
}
