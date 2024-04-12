package websocket;

import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotifyMessage;

public interface NotifyHandler {
    void notifyMessage(NotifyMessage message);

    void loadGameMessage(LoadGameMessage message);

    void errorMessage(ErrorMessage message);

}
