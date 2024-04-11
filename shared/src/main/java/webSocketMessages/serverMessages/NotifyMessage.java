package webSocketMessages.serverMessages;

public class NotifyMessage extends ServerMessage {
    private final String notificationMessage;

    public NotifyMessage(String receivedMessage) {
        super(ServerMessageType.NOTIFICATION);
        this.notificationMessage = receivedMessage;
    }

    public String getNotifyMessage() {
        return notificationMessage;
    }




}
