package ErrorExceptions;

public class ServerResponseException extends Exception {
    final private int statCode;

    public ServerResponseException(int recievedStatCode, String message) {
        super(message);
        this.statCode = recievedStatCode;
    }

    public int getStatCode() {
        return statCode;
    }

    public String getStatMessage() {
        return super.getMessage();
    }

}
