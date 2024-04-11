package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove currentMove;
    private final int gameID;

    public MakeMoveCommand(String authToken, int givenGameID, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = givenGameID;
        this.currentMove = move;
    }

    public int getMoveGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return currentMove;
    }


}
