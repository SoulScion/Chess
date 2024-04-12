package model;

import chess.ChessGame;

public record GameDataResponse(int gameID, String whiteUsername, String blackUsername, String gameName) {

    public boolean spaceOccupied(ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE && whiteUsername() != null) ||
                (color == ChessGame.TeamColor.BLACK && blackUsername() != null);
    }
}
