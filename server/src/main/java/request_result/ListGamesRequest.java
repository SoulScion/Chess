package request_result;

import chess.ChessGame;

public record ListGamesRequest(int gameID, String whiteUsername, String blackUsername, String gameName) {
}
