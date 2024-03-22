package ui;

import chess.ChessGame;
import chess.ChessPiece;

public class ClientMain {

    public static void main(String[] args) {
        var clientURL = "http://localhost:8080";
        if (args.length == 1) {
            clientURL = args[0];
        }
        var chessPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.print("♛ 240 Chess Client " + chessPiece);

        new ClientRepl(clientURL).run();

    }


}
