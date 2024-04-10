import chess.*;
import ui.ClientRepl;

public class Main {
    public static void main(String[] args) {
        var serverLink = "http://localhost:8080";
        if (args.length == 1) {
            serverLink = args[0];
        }
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        new ClientRepl(serverLink).run();
    }
}