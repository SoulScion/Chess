package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    private final String errorMessage;

    public ErrorMessage(String receivedMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = receivedMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}
