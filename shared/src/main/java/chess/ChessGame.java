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
        Collection<ChessMove> listValidatedMoves = new ArrayList<>();
        ChessPiece currentPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPosition currentPosition = new ChessPosition(0,0);


        if (currentBoard.getPiece(startPosition) != null) {
            ChessPiece countingPiece = currentBoard.getPiece(startPosition);

            Collection<ChessMove> listPieceMoves = new ArrayList<>();
            // Collection<ChessMove> listValidatedMoves = new ArrayList<>();

            listPieceMoves = countingPiece.pieceMoves(currentBoard, startPosition);
            for (ChessMove move : listPieceMoves) {
                ChessBoard clonedBoard = new ChessBoard(currentBoard);
                currentPiece = currentBoard.getPiece(move.getStartPosition());
                currentPosition = move.getStartPosition();

                if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE && currentPiece.getPieceType() == ChessPiece.PieceType.PAWN && currentPosition.getRow() + 1 == 8) {
                    ChessPiece promotedPiece = new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece());
                    currentBoard.addPiece(move.getEndPosition(), promotedPiece);
                    currentBoard.addPiece(move.getStartPosition(), currentPiece);
                } else if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK && currentPiece.getPieceType() == ChessPiece.PieceType.PAWN && currentPosition.getRow() - 1 == 1) {
                    ChessPiece promotedPiece = new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece());
                    currentBoard.addPiece(move.getEndPosition(), promotedPiece);
                    currentBoard.addPiece(move.getStartPosition(), currentPiece);
                } else {
                    currentBoard.addPiece(move.getEndPosition(), currentPiece);
                    currentBoard.addPiece(move.getStartPosition(), currentPiece);
                }
                if(isInCheck(currentPiece.getTeamColor()) == true) {
                    currentBoard = new ChessBoard(clonedBoard);
                    continue;
                } else {
                    currentBoard = new ChessBoard(clonedBoard);
                    listValidatedMoves.add(move);
                }
            }
            return listValidatedMoves;
        } else {
            return null;
        }




        //FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> listPieceMoves = new ArrayList<>();
        boolean notInCheck = false;
        ChessPiece teamTurnCheckPiece = currentBoard.getPiece(move.getStartPosition());

        if (move.getEndPosition().getRow() <= 8 && move.getEndPosition().getColumn() <= 8 && move.getEndPosition().getRow() > 0 && move.getEndPosition().getColumn() > 0) {
            listPieceMoves = validMoves(move.getStartPosition());
            for (ChessMove test : listPieceMoves) {
                if (test.getEndPosition().equals(move.getEndPosition())) {
                    notInCheck = true;
                }
            }
            if (notInCheck == true) {
                ChessPiece checkingPiece = currentBoard.getPiece(move.getStartPosition());

                if (checkingPiece.getTeamColor() == getTeamTurn()) {

                    if (currentBoard.getPiece(move.getEndPosition()) != null) {
                        ChessPiece enemyPiece = currentBoard.getPiece(move.getEndPosition());
                        if (checkingPiece.getTeamColor() != enemyPiece.getTeamColor()) {
                            if (checkingPiece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                                ChessPiece promotedPiece = new ChessPiece(checkingPiece.getTeamColor(), move.getPromotionPiece());
                                currentBoard.addPiece(move.getStartPosition(), checkingPiece);
                                currentBoard.addPiece(move.getEndPosition(), enemyPiece); // removes the enemy piece that was there
                                currentBoard.addPiece(move.getEndPosition(), promotedPiece);
                            } else {
                                currentBoard.addPiece(move.getStartPosition(), checkingPiece); // removes the enemy piece that was there
                                currentBoard.addPiece(move.getEndPosition(), checkingPiece);
                            }

                        } else {
                            throw new InvalidMoveException();
                        }
                    } else {
                        if (checkingPiece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                            ChessPiece promotedPiece = new ChessPiece(checkingPiece.getTeamColor(), move.getPromotionPiece());
                            currentBoard.addPiece(move.getEndPosition(), promotedPiece);
                            currentBoard.addPiece(move.getStartPosition(), checkingPiece);
                        } else {
                            currentBoard.addPiece(move.getEndPosition(), checkingPiece);
                            currentBoard.addPiece(move.getStartPosition(), checkingPiece); //This removes the piece at the beginning position
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

        /**
        ChessPiece currentPiece = currentBoard.getPiece(move.getStartPosition());
        ChessPosition currentPosition = move.getStartPosition();
        //There's no reason to check if the piece can't move there because we are sending makeMove(...) a move that has already been validated by the "pieceMoves" method.
        // Making the move:

        // currentBoard[move.getEndPosition().getRow()][move.getEndPosition().getColumn())] = currentPiece;

        // currentPosition = new ChessPosition(move.getEndPosition().getRow(), move.getEndPosition().getColumn()); // Doesn't work as a way to add a piece.

        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE && currentPiece.getPieceType() == ChessPiece.PieceType.PAWN && currentPosition.getRow() + 1 == 8) {
            ChessPiece promotedPiece = new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece());
            currentBoard.addPiece(move.getEndPosition(), promotedPiece);
            currentBoard.addPiece(move.getStartPosition(), currentPiece);

        }
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK && currentPiece.getPieceType() == ChessPiece.PieceType.PAWN && currentPosition.getRow() - 1 == 1) {
            ChessPiece promotedPiece = new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece());
            currentBoard.addPiece(move.getEndPosition(), promotedPiece);
            currentBoard.addPiece(move.getStartPosition(), currentPiece);
        }


        currentBoard.addPiece(move.getEndPosition(), currentPiece);
        currentBoard.addPiece(move.getStartPosition(), currentPiece); // This should be the deleting version.
        if(currentPiece.getTeamColor() != getTeamTurn() || isInCheck(currentPiece.getTeamColor()) == true) {
            throw new InvalidMoveException();
        }
        **/

        //FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        //ChessPosition countingPosition = new ChessPosition(0,0);

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
                            } else {
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
                            } else {
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
        // ChessPiece kingPiece = currentBoard.getPiece(allyKingPosition);
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
        // System.out.printf("%b, %b\n", isInCheck(teamColor), isInStalemate(teamColor));
        if (isInCheck(teamColor) == true && isInStalemate(teamColor) == true) {
            return true;
        } else {
            return false;
        }



        /**
        while (true) {
            if (teamColor == ChessGame.TeamColor.WHITE) {
                for (int row = 1; row <= 8; row++) { // Stepping through each row
                    for (int col = 1; col <= 8; col++) {
                        ChessPosition countingPosition = new ChessPosition(row,col);
                        if (currentBoard.getPiece(countingPosition) != null) {
                            ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                            if (countingPiece.getTeamColor() == TeamColor.BLACK) {
                                continue;
                            } else {
                                Collection<ChessMove> listPieceMoves = new ArrayList<>();
                                listPieceMoves = countingPiece.pieceMoves(currentBoard, countingPosition);
                                for (ChessMove move : listPieceMoves) {
                                    ChessBoard clonedBoard = new ChessBoard(currentBoard);
                                    try {
                                        makeMove(move);
                                    } catch (InvalidMoveException e) {
                                        currentBoard = new ChessBoard(clonedBoard);
                                        continue;
                                    }
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
            }



        }
        **/


        //FINISHED: throw new RuntimeException("Not implemented");
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        Collection<ChessMove> ValidatedMoves = new ArrayList<>();

        while (true) {
            if (teamColor == ChessGame.TeamColor.WHITE && teamColor == getTeamTurn()) {
                for (int row = 1; row <= 8; row++) { // Stepping through each row
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
                            //ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                            //if (countingPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                            //    continue;
                            //} else
                            if (validMoves(countingPosition) != null){
                                return false;
                            }
                        }



                        /**
                        if (currentBoard.getPiece(countingPosition) != null) {
                            ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                            if (countingPiece.getTeamColor() == TeamColor.BLACK) {
                                continue;
                            } else {
                                Collection<ChessMove> listPieceMoves = new ArrayList<>();
                                listPieceMoves = countingPiece.pieceMoves(currentBoard, countingPosition);
                                for (ChessMove move : listPieceMoves) {
                                    ChessBoard clonedBoard = new ChessBoard(currentBoard);
                                    try {
                                        makeMove(move);
                                    } catch (InvalidMoveException e) {
                                        currentBoard = new ChessBoard(clonedBoard);
                                        continue;
                                    }
                                    return false;
                                }
                            }
                        }
                        **/
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
                            //ChessPiece countingPiece = currentBoard.getPiece(countingPosition);
                            //if (countingPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            //    continue;
                            //} else
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
        //FINISHED: throw new RuntimeException("Not implemented");
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
