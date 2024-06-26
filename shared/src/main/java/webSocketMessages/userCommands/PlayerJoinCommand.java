package webSocketMessages.userCommands;

import chess.ChessGame;

public class PlayerJoinCommand extends UserGameCommand {
    private final ChessGame.TeamColor playerColor;
    private final int gameID;

    public PlayerJoinCommand(String authToken, int givenGameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = givenGameID;
        this.playerColor = playerColor;
    }

    public int getPlayerGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

}
