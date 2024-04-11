package webSocketMessages.userCommands;

public class ObserverJoinCommand extends UserGameCommand {
    private final int gameID;

    public ObserverJoinCommand(String authToken, int givenGameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = givenGameID;
    }

    public int getObserverGameID() {
        return gameID;
    }
}
