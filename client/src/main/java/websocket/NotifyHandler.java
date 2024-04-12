package websocket;

import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotifyMessage;

public interface NotifyHandler {
    void notifyMessage(NotifyMessage notifyMessage);

    void loadGameMessage(LoadGameMessage loadMessage);

    void errorMessage(ErrorMessage errorMessage);

}
