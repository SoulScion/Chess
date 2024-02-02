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
    private TeamColor currentTeamTurn;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;


        //FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;


        //FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        // Make sure the king doesn't move into check. That will be done in Valid Moves or Make Move


        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        //ChessPosition countingPosition = new ChessPosition(0,0);
        Boolean teamInCheck = false;
        ChessBoard chessBoardCopy = new ChessBoard();
        ChessPosition whiteKingPosition = new ChessPosition(0,0);
        ChessPosition blackKingPosition = new ChessPosition(0,0);

        if (teamColor == TeamColor.BLACK) { // We want to check the enemy team's pieces, so if it's black's turn, we need to check every white piece.
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


            for (int row = 1; row <= 8; row++) { // Stepping through each row
                for (int col = 1; col <= 8; col++) {
                    ChessPosition countingPosition = new ChessPosition(row,col);
                    if (currentBoard.getPiece(countingPosition) != null) {
                        ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                        if (countingPiece.getTeamColor() == TeamColor.BLACK) {
                            continue;
                        } else {
                            if (countingPiece.getPieceType() != ChessPiece.PieceType.KING) {
                                if (pieceMovesChecker(currentBoard, countingPosition, countingPiece, blackKingPosition) == true) {
                                    return true;
                                }
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


            for (int row = 8; row > 1; row = row - 1) { // Stepping through each row
                for (int col = 8; col > 1; col = col - 1) {
                    ChessPosition countingPosition = new ChessPosition(row,col);
                    if (currentBoard.getPiece(countingPosition) != null) {
                        ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                        if (countingPiece.getTeamColor() == TeamColor.WHITE) {
                            continue;
                        } else {
                            if (countingPiece.getPieceType() != ChessPiece.PieceType.KING) {
                                if (pieceMovesChecker(currentBoard, countingPosition, countingPiece, whiteKingPosition) == true) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;

        // FINISHED: throw new RuntimeException("Not implemented");
    }


    private boolean pieceMovesChecker(ChessBoard board, ChessPosition piecePosition, ChessPiece piece, ChessPosition allyKingPosition) {
        Collection<ChessMove> listValidMoves = new ArrayList<>();
        ChessPiece kingPiece = currentBoard.getPiece(allyKingPosition);
        Boolean inCheck = false;

        listValidMoves = piece.pieceMoves(board, piecePosition);
        for (ChessMove position : listValidMoves) { // Not sure if this works.
            // System.out.printf("%d, %d, %d, %d\n", allyKingPosition.getRow(), allyKingPosition.getColumn(), position.getEndPosition().getRow(), position.getEndPosition().getColumn());
            if (allyKingPosition.getRow() == position.getEndPosition().getRow() && allyKingPosition.getColumn() == position.getEndPosition().getColumn()) { // Not sure if this works.
                inCheck = true;
            }
        }


        return inCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        while (true) {

        }




        //FINISHED: throw new RuntimeException("Not implemented");
    }

    private void copyBoard(ChessBoard board) {


    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.currentBoard = board; // Not sure if I need to use "this."


        //FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;


        //FINISHED: throw new RuntimeException("Not implemented");
    }
}
