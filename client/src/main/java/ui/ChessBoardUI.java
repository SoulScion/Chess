package ui;
import chess.*;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    private static final String[] WHITE_PIECES = {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_PAWN};
    private static final String[] BLACK_PIECES = {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_PAWN};
    private static final String[] LETTER_HEADERS = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
    private static final int BOARD_SIZE_SQUARES = 8;
    private static final int LINE_WIDTH_CHARS = 1;

    public static void main(ChessGame game, ChessGame.TeamColor color) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var board = game.getBoard();

        out.println(ERASE_SCREEN);

        if (color == ChessGame.TeamColor.WHITE) {
            drawHeadersForward(out);

            drawChessBoardForward(out, board);

            drawHeadersForward(out);
        } else {
            drawHeadersBackward(out);

            drawChessBoardBackward(out, board);

            drawHeadersBackward(out);
        }

        out.print(SET_BG_COLOR_WHITE);

        out.print(SET_TEXT_COLOR_WHITE);
    }

    public static void highlight(ChessGame game, ChessGame.TeamColor color, ChessPosition start) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var board = game.getBoard();


        out.println(ERASE_SCREEN);

        if (color == ChessGame.TeamColor.WHITE) {
            drawHeadersForward(out);

            highlightChessBoardForward(out, game, start);

            drawHeadersForward(out);
        } else {
            drawHeadersBackward(out);

            highlightChessBoardBackward(out, game, start);

            drawHeadersBackward(out);
        }

        out.print(SET_BG_COLOR_WHITE);

        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void highlightChessBoardBackward(PrintStream outStream, ChessGame chessGame, ChessPosition startPosition) {
        var validMoves = chessGame.validMoves(startPosition);
        var board = chessGame.getBoard();

        for (int row = 0; row < 8; row++) {
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(row + 1);
            outStream.print(" ");
            for (int col = 0; col < 8; col++) {
                ChessPosition endPosition = new ChessPosition(row + 1, 8 - col);
                ChessPiece chessPiece = board.getPiece(endPosition);
                ChessMove chessMove = new ChessMove(startPosition, endPosition, null);

                printBackground(outStream, validMoves, col, row, chessMove);
                if (Objects.equals(startPosition, endPosition)) {
                    outStream.print(SET_BG_COLOR_YELLOW);
                }

                printNull(outStream, chessPiece);
            }
            outStream.print(SET_TEXT_COLOR_BLACK);
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(row + 1);
            outStream.print(" ");

            outStream.print(SET_BG_COLOR_WHITE);
            outStream.println();
        }
    }

    private static void printBackground(PrintStream outStream, Collection<ChessMove> validMoves, int col, int row, ChessMove chessMove) {
        if ((col + row) % 2 == 0) {
            if (validMoves.contains(chessMove)) {
                outStream.print(SET_BG_COLOR_GREEN);
            } else {
                outStream.print(SET_BG_COLOR_WHITE);
            }
        } else {
            if (validMoves.contains(chessMove)) {
                outStream.print(SET_BG_COLOR_DARK_GREEN);
            } else {
                outStream.print(SET_BG_COLOR_BLACK);
            }
        }
    }

    private static void highlightChessBoardForward(PrintStream outStream, ChessGame chessGame, ChessPosition startPosition) {
        var validMoves = chessGame.validMoves(startPosition);
        var board = chessGame.getBoard();

        for (int row = 0; row < 8; row++) {
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(BOARD_SIZE_SQUARES - row);
            outStream.print(" ");
            for (int col = 0; col < 8; col++) {
                ChessPosition newPos = new ChessPosition(8 - row, col + 1);
                ChessPiece piece = board.getPiece(newPos);
                ChessMove move = new ChessMove(startPosition, newPos, null);

                printBackground(outStream, validMoves, col, row, move);
                if (Objects.equals(newPos, startPosition)) {
                    outStream.print(SET_BG_COLOR_YELLOW);
                }

                printNull(outStream, piece);
            }
            outStream.print(SET_TEXT_COLOR_BLACK);
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(BOARD_SIZE_SQUARES - row);
            outStream.print(" ");

            outStream.println(SET_BG_COLOR_WHITE);
        }

    }

    private static void printNull(PrintStream outStream, ChessPiece chessPiece) {
        if (chessPiece == null) {
            outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));
        } else if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            outStream.print(SET_TEXT_COLOR_BLUE);
            if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
                outStream.print(WHITE_PIECES[0]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                outStream.print(WHITE_PIECES[1]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                outStream.print(WHITE_PIECES[2]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                outStream.print(WHITE_PIECES[3]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.KING) {
                outStream.print(WHITE_PIECES[4]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                outStream.print(WHITE_PIECES[5]);
            }
        } else {
            outStream.print(SET_TEXT_COLOR_RED);
            if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
                outStream.print(BLACK_PIECES[0]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                outStream.print(BLACK_PIECES[1]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                outStream.print(BLACK_PIECES[2]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                outStream.print(BLACK_PIECES[3]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.KING) {
                outStream.print(BLACK_PIECES[4]);
            } else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                outStream.print(BLACK_PIECES[5]);
            }
        }
    }

    private static void drawHeadersBackward(PrintStream outStream) {
        setBlack(outStream);

        outStream.print(SET_BG_COLOR_LIGHT_GREY);

        outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));
        for (int boardCol = 0; boardCol < BOARD_SIZE_SQUARES; ++boardCol) {
            printHeaderText(outStream, LETTER_HEADERS[BOARD_SIZE_SQUARES - boardCol - 1]);
            printHeaderText(outStream, "\u2004");

        }
        outStream.print(SET_BG_COLOR_LIGHT_GREY);
        outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));

        outStream.println(SET_BG_COLOR_WHITE);
    }

    private static void drawHeadersForward(PrintStream outStream) {

        setBlack(outStream);

        outStream.print(SET_BG_COLOR_LIGHT_GREY);

        outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));
        for (int boardCol = 0; boardCol < BOARD_SIZE_SQUARES; ++boardCol) {
            printHeaderText(outStream, LETTER_HEADERS[boardCol]);
            printHeaderText(outStream, "\u2004");

        }
        outStream.print(SET_BG_COLOR_LIGHT_GREY);
        outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));

        outStream.println(SET_BG_COLOR_WHITE);

    }

    private static void printHeaderText(PrintStream outStream, String currentPlayer) {
        outStream.print(SET_BG_COLOR_LIGHT_GREY);

        outStream.print(currentPlayer);

        setBlack(outStream);
    }

    private static void drawChessBoardForward(PrintStream outStream, ChessBoard chessBoard) {
        for (int row = 0; row < 8; row++) {
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(BOARD_SIZE_SQUARES - row);
            outStream.print(" ");
            for (int col = 0; col < 8; col++) {
                if ((col + row) % 2 == 0) {
                    outStream.print(SET_BG_COLOR_WHITE);
                } else {
                    outStream.print(SET_BG_COLOR_BLACK);
                }

                ChessPiece piece = chessBoard.getPiece(new ChessPosition(8 - row, col + 1));

                printNull(outStream, piece);
            }
            outStream.print(SET_TEXT_COLOR_BLACK);
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(BOARD_SIZE_SQUARES - row);
            outStream.print(" ");

            outStream.println(SET_BG_COLOR_WHITE);
        }
    }

    private static void drawChessBoardBackward(PrintStream outStream, ChessBoard chessBoard) {
        for (int row = 0; row < 8; row++) {
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(row + 1);
            outStream.print(" ");
            for (int col = 0; col < 8; col++) {
                if ((col + row) % 2 == 0) {
                    outStream.print(SET_BG_COLOR_WHITE);
                } else {
                    outStream.print(SET_BG_COLOR_BLACK);
                }

                ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, 8 - col));
                printNull(outStream, piece);
            }
            outStream.print(SET_TEXT_COLOR_BLACK);
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(row + 1);
            outStream.print(" ");

            outStream.print(SET_BG_COLOR_WHITE);
            outStream.println();
        }
    }

    private static void setBlack(PrintStream outStream) {
        outStream.print(SET_BG_COLOR_BLACK);
        outStream.print(SET_TEXT_COLOR_BLACK);
    }



}
