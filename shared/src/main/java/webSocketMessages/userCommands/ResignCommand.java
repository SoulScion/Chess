package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private final int gameID;

    public ResignCommand(String authToken, int givenGameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = givenGameID;
    }

    public int getResignGameID() {
        return gameID;
    }


}
