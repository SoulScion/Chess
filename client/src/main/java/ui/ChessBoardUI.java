package ui;
import chess.*;

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

    public static void main(ChessGame chessGame, ChessGame.TeamColor teamColor) {
        var outStream = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var board = chessGame.getBoard();

        outStream.println(ERASE_SCREEN);

        if (teamColor == ChessGame.TeamColor.WHITE) {
            drawHeadersWhite(outStream);
            drawBoardWhite(outStream, board);
            drawHeadersWhite(outStream);
        } else {
            drawHeadersBlack(outStream);
            drawBoardBlack(outStream, board);
            drawHeadersBlack(outStream);
        }

        outStream.print(SET_BG_COLOR_WHITE);

        outStream.print(SET_TEXT_COLOR_WHITE);
    }

    public static void highlight(ChessGame game, ChessGame.TeamColor color, ChessPosition start) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var board = game.getBoard();

        out.println(ERASE_SCREEN);

        if (color == ChessGame.TeamColor.WHITE) {
            drawHeadersWhite(out);
            highlightBoardWhite(out, game, start);
            drawHeadersWhite(out);
        } else {
            drawHeadersBlack(out);
            highlightBoardBlack(out, game, start);
            drawHeadersBlack(out);
        }

        out.print(SET_BG_COLOR_WHITE);

        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void highlightBoardBlack(PrintStream outStream, ChessGame chessGame, ChessPosition startPosition) {
        var validMoves = chessGame.validMoves(startPosition);
        var board = chessGame.getBoard();

        for (int currentRow = 0; currentRow < 8; currentRow++) {
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(currentRow + 1);
            outStream.print(" ");
            for (int col = 0; col < 8; col++) {
                ChessPosition endPosition = new ChessPosition(currentRow + 1, 8 - col);
                ChessPiece chessPiece = board.getPiece(endPosition);
                ChessMove chessMove = new ChessMove(startPosition, endPosition, null);

                printChessBackground(outStream, validMoves, col, currentRow, chessMove);
                if (Objects.equals(startPosition, endPosition)) {
                    outStream.print(SET_BG_COLOR_YELLOW);
                }

                printPiece(outStream, chessPiece);
            }
            outStream.print(SET_TEXT_COLOR_WHITE);
            outStream.print(SET_BG_COLOR_DARK_GREY);
            outStream.print(" ");
            outStream.print(currentRow + 1);
            outStream.print(" ");
            outStream.print(SET_BG_COLOR_WHITE);
            outStream.println();
        }
    }

    private static void printChessBackground(PrintStream outStream, Collection<ChessMove> validMoves, int column, int row, ChessMove chessMove) {
        if ((column + row) % 2 == 0) {
            if (validMoves.contains(chessMove)) {
                outStream.print(SET_BG_COLOR_DARK_GREEN);
            } else {
                outStream.print(SET_BG_COLOR_WHITE);
            }
        } else {
            if (validMoves.contains(chessMove)) {
                outStream.print(SET_BG_COLOR_GREEN);
            } else {
                outStream.print(SET_BG_COLOR_BLACK);
            }
        }
    }

    private static void highlightBoardWhite(PrintStream outStream, ChessGame chessGame, ChessPosition startPosition) {
        var validMoves = chessGame.validMoves(startPosition);
        var board = chessGame.getBoard();

        for (int currentRow = 0; currentRow < 8; currentRow++) {
            outStream.print(SET_BG_COLOR_LIGHT_GREY);
            outStream.print(" ");
            outStream.print(BOARD_SIZE_SQUARES - currentRow);
            outStream.print(" ");
            for (int col = 0; col < 8; col++) {
                ChessPosition newPos = new ChessPosition(8 - currentRow, col + 1);
                ChessPiece chessPiece = board.getPiece(newPos);
                ChessMove chessMove = new ChessMove(startPosition, newPos, null);

                printChessBackground(outStream, validMoves, col, currentRow, chessMove);
                if (Objects.equals(newPos, startPosition)) {
                    outStream.print(SET_BG_COLOR_YELLOW);
                }

                printPiece(outStream, chessPiece);
            }
            outStream.print(SET_TEXT_COLOR_WHITE);
            outStream.print(SET_BG_COLOR_DARK_GREY);
            outStream.print(" ");
            outStream.print(BOARD_SIZE_SQUARES - currentRow);
            outStream.print(" ");
            outStream.println(SET_BG_COLOR_WHITE);
        }

    }

    private static void printPiece(PrintStream outStream, ChessPiece chessPiece) {
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

    private static void drawHeadersBlack(PrintStream outStream) {
        setBlack(outStream);
        outStream.print(SET_BG_COLOR_DARK_GREY);
        outStream.print(SET_TEXT_COLOR_WHITE);

        outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));
        for (int boardColumn = 0; boardColumn < BOARD_SIZE_SQUARES; ++boardColumn) {
            outStream.print(SET_TEXT_COLOR_WHITE);
            printHeader(outStream, LETTER_HEADERS[BOARD_SIZE_SQUARES - boardColumn - 1]);
            printHeader(outStream, "\u2004");

        }
        outStream.print(SET_TEXT_COLOR_WHITE);
        outStream.print(SET_BG_COLOR_DARK_GREY);
        outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));
        outStream.println(SET_BG_COLOR_WHITE);
    }

    private static void drawHeadersWhite(PrintStream outStream) {
        setBlack(outStream);
        outStream.print(SET_BG_COLOR_DARK_GREY);
        outStream.print(SET_TEXT_COLOR_WHITE);

        outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));
        for (int boardCol = 0; boardCol < BOARD_SIZE_SQUARES; ++boardCol) {
            outStream.print(SET_TEXT_COLOR_WHITE);
            printHeader(outStream, LETTER_HEADERS[boardCol]);
            printHeader(outStream, "\u2004");

        }
        outStream.print(SET_TEXT_COLOR_WHITE);
        outStream.print(SET_BG_COLOR_DARK_GREY);
        outStream.print(EMPTY.repeat(LINE_WIDTH_CHARS));
        outStream.println(SET_BG_COLOR_WHITE);

    }

    private static void printHeader(PrintStream outStream, String currentPlayer) {
        outStream.print(SET_BG_COLOR_DARK_GREY);

        outStream.print(currentPlayer);

        setBlack(outStream);
    }

    private static void drawBoardWhite(PrintStream outStream, ChessBoard chessBoard) {
        for (int row = 0; row < 8; row++) {
            outStream.print(SET_BG_COLOR_DARK_GREY);
            outStream.print(" ");
            outStream.print(BOARD_SIZE_SQUARES - row);
            outStream.print(" ");
            for (int col = 0; col < 8; col++) {
                if ((col + row) % 2 == 0) {
                    outStream.print(SET_BG_COLOR_WHITE);
                } else {
                    outStream.print(SET_BG_COLOR_DARK_GREEN);
                }

                ChessPiece piece = chessBoard.getPiece(new ChessPosition(8 - row, col + 1));

                printPiece(outStream, piece);
            }
            outStream.print(SET_TEXT_COLOR_WHITE);
            outStream.print(SET_BG_COLOR_DARK_GREY);
            outStream.print(" ");
            outStream.print(BOARD_SIZE_SQUARES - row);
            outStream.print(" ");
            outStream.println(SET_BG_COLOR_WHITE);
        }
    }

    private static void drawBoardBlack(PrintStream outStream, ChessBoard chessBoard) {
        for (int row = 0; row < 8; row++) {
            outStream.print(SET_BG_COLOR_DARK_GREY);
            outStream.print(" ");
            outStream.print(row + 1);
            outStream.print(" ");
            for (int col = 0; col < 8; col++) {
                if ((col + row) % 2 == 0) {
                    outStream.print(SET_BG_COLOR_WHITE);
                } else {
                    outStream.print(SET_BG_COLOR_DARK_GREEN);
                }

                ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, 8 - col));

                printPiece(outStream, piece);
            }
            outStream.print(SET_TEXT_COLOR_WHITE);
            outStream.print(SET_BG_COLOR_DARK_GREY);
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
