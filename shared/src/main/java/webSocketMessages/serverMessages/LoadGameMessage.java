package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private final GameData game;
    private chess.ChessGame.TeamColor teamColor;

    public LoadGameMessage(GameData receivedGame) {
        super(ServerMessageType.LOAD_GAME);
        this.game = receivedGame;
    }

    public GameData getNewGame() {
        return game;
    }

    public chess.ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    public void setNewColor(chess.ChessGame.TeamColor newColor) {
        this.teamColor = newColor;
    }

}
