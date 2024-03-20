package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_CHARS = 10;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBlackBoard(out);

        out.print("\n");
        out.print(SET_BG_COLOR_BLACK);
        out.print("\n");

        drawWhiteBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawBlackBoard(PrintStream out) {

        String[] boardBackColors = {EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY};


        String preSpace = "\u2002\u2006 \u2009\u2002";
        String postSpace = "\u2002 \u200A\u2002";
        String[] letters = {"\u2002\u2005a\u200A\u2005\u2002", "\u2002\u2005b\u2005\u2002", "\u2002\u2005c\u2005\u2002", "\u2002\u2005\u200Ad\u2005\u2002", "\u2002\u200A\u200Ae\u2005\u2002", "\u2002\u2005f\u2005\u2002", "\u2002\u2005\u200Ag\u200A\u2005\u2002", "\u2002\u2005h\u2005\u2002"};
        String[] lettersReverse = {"\u2002\u2005h\u200A\u2005\u2002", "\u2002\u2005g\u2005\u2002", "\u2002\u2005f\u2005\u2002", "\u2002\u2005\u200Ae\u2005\u2002", "\u2002\u200A\u200Ad\u2005\u2002", "\u2002\u2005c\u2005\u2002", "\u2002\u2005\u200Ab\u200A\u2005\u2002", "\u2002\u2005a\u2005\u2002"};
        String[] numbers = {"\u20028\u2004\u2002", "\u20027\u2004\u2002", "\u20026\u2004\u2002", "\u20025\u2004\u2002", "\u20024\u2004\u2002", "\u20023\u2004\u2002", "\u20022\u2004\u2002", "\u20021\u2004\u2002"};

        String[] whitePieces = {EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK};
        String[] whitePawns = {EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN};

        String[] blackPieces = {EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN, EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK};
        String[] blackPawns = {EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN};
        String[] rowHeaders = {"", "8", "7", "6", "5", "4", "3", "2", "1", " "};

        out.print(SET_BG_COLOR_DARK_GREY);

        for (int boardRow = 0; boardRow < SQUARE_SIZE_IN_CHARS; ++boardRow) {
            for (int boardCol = 0; boardCol < SQUARE_SIZE_IN_CHARS; ++boardCol) {
                if (boardRow == 0 && boardCol == 0) {
                    out.print(preSpace);
                    for (String letter : letters) {
                        out.print(letter);
                    }
                    out.print(postSpace);
                } else if (boardRow == 1 && boardCol == 1) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[0]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + whitePieces[i]);
                        } else {
                            if (i % 2 == 1) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + whitePieces[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + whitePieces[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[7]);

                } else if (boardRow == 2 && boardCol == 2) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[1]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + whitePawns[i]);
                        } else {
                            if (i % 2 == 0) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + whitePawns[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + whitePawns[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[6]);

                } else if (boardRow == 3 && boardCol == 3) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[2]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                        } else {
                            if (i % 2 == 1) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + boardBackColors[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[5]);

                } else if (boardRow == 4 && boardCol == 4) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[3]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                        } else {
                            if (i % 2 == 0) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + boardBackColors[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[4]);

                } else if (boardRow == 5 && boardCol == 5) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[4]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                        } else {
                            if (i % 2 == 1) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + boardBackColors[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[3]);

                } else if (boardRow == 6 && boardCol == 6) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[5]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                        } else {
                            if (i % 2 == 0) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + boardBackColors[i]);
                            }
                        }
                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[2]);

                } else if (boardRow == 7 && boardCol == 7) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[6]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + blackPawns[i]);
                        } else {
                            if (i % 2 == 1) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + blackPawns[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + blackPawns[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[1]);

                } else if (boardRow == 8 && boardCol == 8) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[7]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + blackPieces[i]);
                        } else {
                            if (i % 2 == 0) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + blackPieces[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + blackPieces[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[0]);
                } else if (boardRow == 9 && boardCol == 9) {
                    out.print("\n");
                    out.print(SET_BG_COLOR_DARK_GREY + preSpace);
                    for (String letter : lettersReverse) {
                        out.print(SET_BG_COLOR_DARK_GREY + letter);
                    }
                    out.print(postSpace);
                }

                if (boardCol != 0) {
                    out.print(RESET_BG_COLOR);
                }
            }
        }
    }


    private static void drawWhiteBoard(PrintStream out) {

        String[] boardBackColors = {EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY};


        String preSpace = "\u2002\u2006 \u2009\u2002";
        String postSpace = "\u2002 \u200A\u2002";
        String[] letters = {"\u2002\u2005a\u200A\u2005\u2002", "\u2002\u2005b\u2005\u2002", "\u2002\u2005c\u2005\u2002", "\u2002\u2005\u200Ad\u2005\u2002", "\u2002\u200A\u200Ae\u2005\u2002", "\u2002\u2005f\u2005\u2002", "\u2002\u2005\u200Ag\u200A\u2005\u2002", "\u2002\u2005h\u2005\u2002"};
        String[] lettersReverse = {"\u2002\u2005h\u200A\u2005\u2002", "\u2002\u2005g\u2005\u2002", "\u2002\u2005f\u2005\u2002", "\u2002\u2005\u200Ae\u2005\u2002", "\u2002\u200A\u200Ad\u2005\u2002", "\u2002\u2005c\u2005\u2002", "\u2002\u2005\u200Ab\u200A\u2005\u2002", "\u2002\u2005a\u2005\u2002"};
        String[] numbers = {"\u20028\u2004\u2002", "\u20027\u2004\u2002", "\u20026\u2004\u2002", "\u20025\u2004\u2002", "\u20024\u2004\u2002", "\u20023\u2004\u2002", "\u20022\u2004\u2002", "\u20021\u2004\u2002"};

        String[] whitePieces = {EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK};
        String[] whitePawns = {EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN};

        String[] blackPieces = {EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN, EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK};
        String[] blackPawns = {EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN};
        String[] rowHeaders = {"", "8", "7", "6", "5", "4", "3", "2", "1", " "};

        out.print(SET_BG_COLOR_DARK_GREY);

        for (int boardRow = 0; boardRow < SQUARE_SIZE_IN_CHARS; ++boardRow) {
            for (int boardCol = 0; boardCol < SQUARE_SIZE_IN_CHARS; ++boardCol) {
                if (boardRow == 0 && boardCol == 0) {
                    out.print(preSpace);
                    for (String letter : letters) {
                        out.print(letter);
                    }
                    out.print(postSpace);
                } else if (boardRow == 1 && boardCol == 1) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[0]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + blackPieces[i]);
                        } else {
                            if (i % 2 == 1) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + blackPieces[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + blackPieces[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[7]);

                } else if (boardRow == 2 && boardCol == 2) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[1]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + blackPawns[i]);
                        } else {
                            if (i % 2 == 0) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + blackPawns[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + blackPawns[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[6]);

                } else if (boardRow == 3 && boardCol == 3) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[2]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                        } else {
                            if (i % 2 == 1) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + boardBackColors[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[5]);

                } else if (boardRow == 4 && boardCol == 4) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[3]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                        } else {
                            if (i % 2 == 0) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + boardBackColors[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[4]);

                } else if (boardRow == 5 && boardCol == 5) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[4]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                        } else {
                            if (i % 2 == 1) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + boardBackColors[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[3]);

                } else if (boardRow == 6 && boardCol == 6) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[5]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                        } else {
                            if (i % 2 == 0) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + boardBackColors[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + boardBackColors[i]);
                            }
                        }
                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[2]);

                } else if (boardRow == 7 && boardCol == 7) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[6]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + whitePawns[i]);
                        } else {
                            if (i % 2 == 1) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + whitePawns[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + whitePawns[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[1]);

                } else if (boardRow == 8 && boardCol == 8) {
                    out.print("\n");
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[7]);
                    for (int i = 0; i < 8; i++) {
                        if (i == 0) {
                            out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + whitePieces[i]);
                        } else {
                            if (i % 2 == 0) {// odd
                                out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLACK + whitePieces[i]);
                            } else {
                                out.print(SET_BG_COLOR_WHITE + whitePieces[i]);
                            }
                        }

                    }
                    out.print(SET_TEXT_COLOR_WHITE + SET_BG_COLOR_DARK_GREY + numbers[0]);
                } else if (boardRow == 9 && boardCol == 9) {
                    out.print("\n");
                    out.print(SET_BG_COLOR_DARK_GREY + preSpace);
                    for (String letter : lettersReverse) {
                        out.print(SET_BG_COLOR_DARK_GREY + letter);
                    }
                    out.print(postSpace);
                }

                if (boardCol != 0) {
                    out.print(RESET_BG_COLOR);
                }
            }
        }
    }



}
