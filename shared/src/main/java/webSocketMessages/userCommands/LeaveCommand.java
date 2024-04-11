package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private final int gameID;

    public LeaveCommand(String authToken, int givenGameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = givenGameID;
    }

    public int getLeaveGameID() {
        return gameID;
    }
}
