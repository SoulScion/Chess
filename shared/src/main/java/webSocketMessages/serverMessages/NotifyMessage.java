package webSocketMessages.serverMessages;

public class NotifyMessage extends ServerMessage {
    private final String message;

    public NotifyMessage(String receivedMessage) {
        super(ServerMessageType.NOTIFICATION);
        this.message = receivedMessage;
    }

    public String getNotifyMessage() {
        return message;
    }




}
