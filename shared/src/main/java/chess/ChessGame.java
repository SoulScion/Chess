package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard currentBoard;
    private TeamColor currentTeamTurn = TeamColor.WHITE;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;

    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;

    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK,
        NONE
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        Collection<ChessMove> listValidatedMoves = new ArrayList<>();
        ChessPiece currentPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPosition currentPosition = new ChessPosition(0,0);


        if (currentBoard.getPiece(startPosition) != null) {
            ChessPiece countingPiece = currentBoard.getPiece(startPosition);

            Collection<ChessMove> listPieceMoves = new ArrayList<>();

            listPieceMoves = countingPiece.pieceMoves(currentBoard, startPosition);
            for (ChessMove move : listPieceMoves) {
                ChessBoard clonedBoard = new ChessBoard(currentBoard);
                currentPiece = currentBoard.getPiece(move.getStartPosition());
                currentPosition = move.getStartPosition();

                if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE && currentPiece.getPieceType() == ChessPiece.PieceType.PAWN && currentPosition.getRow() + 1 == 8) {
                    ChessPiece promotedPiece = new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece());
                    removingAndAddingPieces(move, promotedPiece, currentPiece);
                } else if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK && currentPiece.getPieceType() == ChessPiece.PieceType.PAWN && currentPosition.getRow() - 1 == 1) {
                    ChessPiece promotedPiece = new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece());
                    removingAndAddingPieces(move, promotedPiece, currentPiece);
                } else {
                    removingAndAddingPieces(move, currentPiece, currentPiece);
                }
                if(isInCheck(currentPiece.getTeamColor())) {
                    currentBoard = new ChessBoard(clonedBoard);
                } else {
                    currentBoard = new ChessBoard(clonedBoard);
                    listValidatedMoves.add(move);
                }
            }
            return listValidatedMoves;
        } else {
            return null;
        }

    }

    private void removingAndAddingPieces(ChessMove move, ChessPiece promotedPiece, ChessPiece currentPiece) {
        currentBoard.addPiece(move.getEndPosition(), promotedPiece);
        currentBoard.addPiece(move.getStartPosition(), currentPiece);
    }


    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> listPieceMoves = new ArrayList<>();
        boolean notInCheck = false;
        ChessPiece teamTurnCheckPiece = currentBoard.getPiece(move.getStartPosition());

        if (move.getEndPosition().getRow() <= 8 && move.getEndPosition().getColumn() <= 8 && move.getEndPosition().getRow() > 0 && move.getEndPosition().getColumn() > 0) {
            listPieceMoves = validMoves(move.getStartPosition());
            for (ChessMove test : listPieceMoves) {
                if (test.getEndPosition().equals(move.getEndPosition())) {
                    notInCheck = true;
                    break;
                }
            }
            if (notInCheck) {
                ChessPiece checkingPiece = currentBoard.getPiece(move.getStartPosition());

                if (checkingPiece.getTeamColor() == getTeamTurn()) {

                    if (currentBoard.getPiece(move.getEndPosition()) != null) {
                        ChessPiece enemyPiece = currentBoard.getPiece(move.getEndPosition());
                        if (checkingPiece.getTeamColor() != enemyPiece.getTeamColor()) {
                            if (checkingPiece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                                ChessPiece promotedPiece = new ChessPiece(checkingPiece.getTeamColor(), move.getPromotionPiece());
                                currentBoard.addPiece(move.getStartPosition(), checkingPiece);
                                currentBoard.addPiece(move.getEndPosition(), enemyPiece);
                                currentBoard.addPiece(move.getEndPosition(), promotedPiece);
                            } else {
                                currentBoard.addPiece(move.getStartPosition(), checkingPiece);
                                currentBoard.addPiece(move.getEndPosition(), checkingPiece);
                            }

                        } else {
                            throw new InvalidMoveException();
                        }
                    } else {
                        if (checkingPiece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                            ChessPiece promotedPiece = new ChessPiece(checkingPiece.getTeamColor(), move.getPromotionPiece());
                            removingAndAddingPieces(move, promotedPiece, checkingPiece);
                        } else {
                            removingAndAddingPieces(move, checkingPiece, checkingPiece);
                        }
                    }

                } else {
                    throw new InvalidMoveException();
                }
            } else {
                throw new InvalidMoveException();
            }
        } else {
            throw new InvalidMoveException();
        }

        if (teamTurnCheckPiece.getTeamColor() == ChessGame.TeamColor.WHITE && getTeamTurn() == ChessGame.TeamColor.WHITE) {
            setTeamTurn(ChessGame.TeamColor.BLACK);
        } else if (teamTurnCheckPiece.getTeamColor() == ChessGame.TeamColor.BLACK && getTeamTurn() == ChessGame.TeamColor.BLACK) {
            setTeamTurn(ChessGame.TeamColor.WHITE);
        }
        // These were newly added:


    }

    public boolean isInCheck(TeamColor teamColor) {

        ChessBoard chessBoardCopy = new ChessBoard();
        ChessPosition whiteKingPosition = new ChessPosition(0,0);
        ChessPosition blackKingPosition = new ChessPosition(0,0);

        if (teamColor == TeamColor.BLACK) {
            for (int row = 8; row > 1; row = row - 1) {
                for (int col = 8; col > 1; col = col - 1) {
                    ChessPosition countingPosition = new ChessPosition(row,col);
                    if (currentBoard.getPiece(countingPosition) != null) {
                        ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                        if (countingPiece.getPieceType() == ChessPiece.PieceType.KING && countingPiece.getTeamColor() == TeamColor.BLACK) {
                            blackKingPosition = countingPosition;
                        }
                    }
                }
            }


            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition countingPosition = new ChessPosition(row,col);
                    if (currentBoard.getPiece(countingPosition) != null) {
                        ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                        if (countingPiece.getTeamColor() == TeamColor.BLACK) {
                            continue;
                        } else {
                            if (pieceMovesChecker(currentBoard, countingPosition, countingPiece, blackKingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        if (teamColor == TeamColor.WHITE) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition countingPosition = new ChessPosition(row,col);
                    if (currentBoard.getPiece(countingPosition) != null) {
                        ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                        if (countingPiece.getPieceType() == ChessPiece.PieceType.KING && countingPiece.getTeamColor() == TeamColor.WHITE) {
                            whiteKingPosition = countingPosition;
                        }
                    }
                }
            }


            for (int row = 8; row > 1; row = row - 1) {
                for (int col = 8; col > 1; col = col - 1) {
                    ChessPosition countingPosition = new ChessPosition(row,col);
                    if (currentBoard.getPiece(countingPosition) != null) {
                        ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                        if (countingPiece.getTeamColor() == TeamColor.WHITE) {
                            continue;
                        } else {
                            if (pieceMovesChecker(currentBoard, countingPosition, countingPiece, whiteKingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    private boolean pieceMovesChecker(ChessBoard board, ChessPosition piecePosition, ChessPiece piece, ChessPosition allyKingPosition) {
        Collection<ChessMove> listValidMoves = new ArrayList<>();
        Boolean inCheck = false;

        listValidMoves = piece.pieceMoves(board, piecePosition);
        for (ChessMove position : listValidMoves) {
            if (allyKingPosition.getRow() == position.getEndPosition().getRow() && allyKingPosition.getColumn() == position.getEndPosition().getColumn()) {
                inCheck = true;
                break;
            }
        }


        return inCheck;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor) && isInStalemate(teamColor)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInStalemate(TeamColor teamColor) {

        Collection<ChessMove> validatedMoves = new ArrayList<>();

        while (true) {
            if (teamColor == ChessGame.TeamColor.WHITE && teamColor == getTeamTurn()) {
                for (int row = 1; row <= 8; row++) {
                    for (int col = 1; col <= 8; col++) {
                        ChessPosition countingPosition = new ChessPosition(row,col);
                        if (currentBoard.getPiece(countingPosition) != null) {
                            ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                            if (countingPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                                continue;
                            }
                        }

                        if (validMoves(countingPosition) == null || validMoves(countingPosition).isEmpty()) {
                            continue;
                        } else {
                            if (validMoves(countingPosition) != null){
                                return false;
                            }
                        }

                    }
                }
                return true;
            } else {
                break;
            }



        }

        while (true) {
            if (teamColor == ChessGame.TeamColor.BLACK && teamColor == getTeamTurn()) {
                for (int row = 8; row > 1; row = row - 1) { // Stepping through each row
                    for (int col = 8; col > 1; col = col - 1) {
                        ChessPosition countingPosition = new ChessPosition(row, col);
                        if (currentBoard.getPiece(countingPosition) != null) {
                            ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                            if (countingPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                                continue;
                            }
                        }

                        if (validMoves(countingPosition) == null || validMoves(countingPosition).isEmpty()) {
                            continue;
                        } else {
                            if (validMoves(countingPosition) != null) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            } else {
                break;
            }
        }

        return false;
    }

    public void setBoard(ChessBoard board) {
        this.currentBoard = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;

    }
}
