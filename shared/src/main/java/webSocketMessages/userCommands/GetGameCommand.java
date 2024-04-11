package webSocketMessages.userCommands;

public class GetGameCommand extends UserGameCommand {
    private final int gameID;

    public GetGameCommand(String authToken, int givenGameID) {
        super(authToken);
        this.commandType = UserGameCommand.CommandType.GET_GAME;
        this.gameID = givenGameID;
    }

    public int getRetrieveGameID() {
        return gameID;
    }


}
