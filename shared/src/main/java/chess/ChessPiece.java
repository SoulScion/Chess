package chess;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public ChessPiece(ChessPiece originalPiece) {
        this.pieceColor = originalPiece.pieceColor;
        this.type = originalPiece.type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;


        // FINISHED: hrow new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;

        // FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece movingPiece = board.getPiece(myPosition);

        if (movingPiece.type == PieceType.KING) {
            return kingMoves(movingPiece, board, myPosition);
        } else if (movingPiece.type == PieceType.QUEEN){
            return queenMoves(movingPiece, board, myPosition);
        } else if (movingPiece.type == PieceType.BISHOP) {
            return bishopMoves(movingPiece, board, myPosition);
        } else if (movingPiece.type == PieceType.KNIGHT) {
            return knightMoves(movingPiece, board, myPosition);
        } else if (movingPiece.type == PieceType.ROOK) {
            return rookMoves(movingPiece, board, myPosition);
        } else if (movingPiece.type == PieceType.PAWN) {
            return pawnMoves(movingPiece, board, myPosition);
        }

        throw new RuntimeException("Not implemented");
    }

    private void validMove(int x, int y, ChessBoard board, ChessPiece pieceChecked, ChessPosition piecePosition, ArrayList<ChessMove> validMoves) {
        int tempx = x;
        int tempy = y;
        if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
            if (board.getPiece(new ChessPosition(tempx, tempy)) == null || pieceChecked.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                validMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
            }
        }
    }

    private void validPawnMoveHelper(int x, int y, ChessBoard board, ChessPiece pieceChecked, ChessPosition piecePosition, ArrayList<ChessMove> validMoves) {
        int tempx = x;
        int tempy = y;
        if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
            if (board.getPiece(new ChessPosition(tempx, tempy)) == null) { // Second Check
                validMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
            }
        }
    }


    private ArrayList<ChessMove> kingMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessMove> validKingMoves = new ArrayList<>();
        int tempx = piecePosition.getRow();
        int tempy = piecePosition.getColumn();

        validMove(piecePosition.getRow() + 1, piecePosition.getColumn(), board, piece, piecePosition, validKingMoves);
        validMove(piecePosition.getRow(), piecePosition.getColumn() + 1, board, piece, piecePosition, validKingMoves);
        validMove(piecePosition.getRow() - 1, piecePosition.getColumn(), board, piece, piecePosition, validKingMoves);
        validMove(piecePosition.getRow(), piecePosition.getColumn() - 1, board, piece, piecePosition, validKingMoves);
        validMove(piecePosition.getRow() + 1, piecePosition.getColumn() + 1, board, piece, piecePosition, validKingMoves);
        validMove(piecePosition.getRow() - 1, piecePosition.getColumn() - 1, board, piece, piecePosition, validKingMoves);
        validMove(piecePosition.getRow() + 1, piecePosition.getColumn() - 1, board, piece, piecePosition, validKingMoves);
        validMove(piecePosition.getRow() - 1, piecePosition.getColumn() + 1, board, piece, piecePosition, validKingMoves);

        return validKingMoves;
    }

    private ArrayList<ChessMove> queenMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessMove> validQueenMoves = new ArrayList<>();
        validQueenMoves = bishopMoves(piece, board, piecePosition);
        validQueenMoves.addAll(rookMoves(piece, board, piecePosition));

        return validQueenMoves;
    }

    private ArrayList<ChessMove> bishopMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessMove> validBishopMoves = new ArrayList<>();
        int tempx = piecePosition.getRow();
        int tempy = piecePosition.getColumn();

        while(true) {
            tempx = tempx + 1;
            tempy = tempy + 1;
            if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
                if (board.getPiece(new ChessPosition(tempx, tempy )) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                    // ChessPiece tempPiece = board.getPiece(new ChessPosition(tempx, tempy));
                    // if (piece.pieceColor != tempPiece.pieceColor) {
                    validBishopMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                        // System Check:
                    // System.out.printf("%d, %d\n", tempx, tempy);
                    //} else {
                    //    break;
                    //}
                    if (board.getPiece(new ChessPosition(tempx, tempy )) != null) {
                        if (piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        while(true) {
            tempx = tempx - 1;
            tempy = tempy + 1;
            if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
                if (board.getPiece(new ChessPosition(tempx, tempy )) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                    // ChessPiece tempPiece = board.getPiece(new ChessPosition(tempx, tempy));
                    // if (piece.pieceColor != tempPiece.pieceColor) {
                    validBishopMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    // System Check:
                    // System.out.printf("%d, %d\n", tempx, tempy);
                    //} else {
                    //    break;
                    //}
                    if (board.getPiece(new ChessPosition(tempx, tempy )) != null) {
                        if (piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        while(true) {
            tempx = tempx + 1;
            tempy = tempy - 1;
            if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
                if (board.getPiece(new ChessPosition(tempx, tempy )) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                    // ChessPiece tempPiece = board.getPiece(new ChessPosition(tempx, tempy));
                    // if (piece.pieceColor != tempPiece.pieceColor) {
                    validBishopMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    // System Check:
                    // System.out.printf("%d, %d\n", tempx, tempy);
                    //} else {
                    //    break;
                    //}
                    if (board.getPiece(new ChessPosition(tempx, tempy )) != null) {
                        if (piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        while(true) {
            tempx = tempx - 1;
            tempy = tempy - 1;
            if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
                if (board.getPiece(new ChessPosition(tempx, tempy )) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                    validBishopMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    // System Check:
                    // System.out.printf("%d, %d\n", tempx, tempy);
                    if (board.getPiece(new ChessPosition(tempx, tempy )) != null) { // Third Check
                        if (piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return validBishopMoves;
        // FINISHING: throw new RuntimeException("Not implemented");
    }

    private ArrayList<ChessMove> knightMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessMove> validKnightMoves = new ArrayList<>();
        int tempx = piecePosition.getRow();
        int tempy = piecePosition.getColumn();

        validMove(piecePosition.getRow() + 2, piecePosition.getColumn() + 1, board, piece, piecePosition, validKnightMoves);
        validMove(piecePosition.getRow() + 2, piecePosition.getColumn() - 1, board, piece, piecePosition, validKnightMoves);
        validMove(piecePosition.getRow() - 2, piecePosition.getColumn() + 1, board, piece, piecePosition, validKnightMoves);
        validMove(piecePosition.getRow() - 2, piecePosition.getColumn() - 1, board, piece, piecePosition, validKnightMoves);
        validMove(piecePosition.getRow() - 1, piecePosition.getColumn() + 2, board, piece, piecePosition, validKnightMoves);
        validMove(piecePosition.getRow() + 1, piecePosition.getColumn() + 2, board, piece, piecePosition, validKnightMoves);
        validMove(piecePosition.getRow() - 1, piecePosition.getColumn() - 2, board, piece, piecePosition, validKnightMoves);
        validMove(piecePosition.getRow() + 1, piecePosition.getColumn() - 2, board, piece, piecePosition, validKnightMoves);

        return validKnightMoves;
    }

    private ArrayList<ChessMove> rookMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessMove> validRookMoves = new ArrayList<>();
        int tempx = piecePosition.getRow();
        int tempy = piecePosition.getColumn();

        while(true) {
            tempx = tempx + 1;
            if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
                if (board.getPiece(new ChessPosition(tempx, tempy)) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                    // ChessPiece tempPiece = board.getPiece(new ChessPosition(tempx, tempy));
                    // if (piece.pieceColor != tempPiece.pieceColor) {
                    validRookMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    // System Check:
                    // System.out.printf("%d, %d\n", tempx, tempy);
                    //} else {
                    //    break;
                    //}
                    if (board.getPiece(new ChessPosition(tempx, tempy )) != null) {
                        if (piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        while(true) {
            tempx = tempx - 1;
            if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
                if (board.getPiece(new ChessPosition(tempx, tempy )) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                    // ChessPiece tempPiece = board.getPiece(new ChessPosition(tempx, tempy));
                    // if (piece.pieceColor != tempPiece.pieceColor) {
                    validRookMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    // System Check:
                    // System.out.printf("%d, %d\n", tempx, tempy);
                    //} else {
                    //    break;
                    //}
                    if (board.getPiece(new ChessPosition(tempx, tempy )) != null) {
                        if (piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        while(true) {
            tempy = tempy + 1;
            if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
                if (board.getPiece(new ChessPosition(tempx, tempy )) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                    // ChessPiece tempPiece = board.getPiece(new ChessPosition(tempx, tempy));
                    // if (piece.pieceColor != tempPiece.pieceColor) {
                    validRookMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    // System Check:
                    // System.out.printf("%d, %d\n", tempx, tempy);
                    //} else {
                    //    break;
                    //}
                    if (board.getPiece(new ChessPosition(tempx, tempy )) != null) {
                        if (piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        while(true) {
            tempy = tempy - 1;
            if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
                if (board.getPiece(new ChessPosition(tempx, tempy )) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                    validRookMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    // System Check:
                    // System.out.printf("%d, %d\n", tempx, tempy);
                    if (board.getPiece(new ChessPosition(tempx, tempy )) != null) { // Third Check
                        if (piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return validRookMoves;
    }

    private ArrayList<ChessMove> pawnMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessMove> validPawnMoves = new ArrayList<>();
        int tempx = piecePosition.getRow();
        int tempy = piecePosition.getColumn();

        // Check for Starting Position Special Move:
        if (piece.pieceColor == ChessGame.TeamColor.WHITE) {
            if (piecePosition.getRow() == 2 && board.getPiece(new ChessPosition(tempx + 1, tempy)) == null && board.getPiece(new ChessPosition(tempx + 2, tempy)) == null) {
                validPawnMoveHelper(piecePosition.getRow() + 1, piecePosition.getColumn(), board, piece, piecePosition, validPawnMoves);
                validPawnMoveHelper(piecePosition.getRow() + 2, piecePosition.getColumn(), board, piece, piecePosition, validPawnMoves);
            }
        }
        if (piece.pieceColor == ChessGame.TeamColor.BLACK) {
            if (piecePosition.getRow() == 7 && board.getPiece(new ChessPosition(tempx - 1, tempy)) == null && board.getPiece(new ChessPosition(tempx - 2, tempy)) == null) {
                validPawnMoveHelper(piecePosition.getRow() - 1, piecePosition.getColumn(), board, piece, piecePosition, validPawnMoves);
                validPawnMoveHelper(piecePosition.getRow() - 2, piecePosition.getColumn(), board, piece, piecePosition, validPawnMoves);
            }
        }

        // Moving up one space:
        tempx = piecePosition.getRow() + 1;
        // Check for Regular Move & Promotion:
        if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
            if (board.getPiece(new ChessPosition(tempx, tempy)) == null) {
                if (piece.pieceColor == ChessGame.TeamColor.WHITE) {
                    if (piecePosition.getRow() + 1 == 8) {
                        // piece = ChessMove.getPromotionPiece(); POSSIBLY HAVE TO ADD THIS
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.QUEEN));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.BISHOP));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.KNIGHT));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.ROOK));
                    } else {
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    }
                }
            }
            tempx = piecePosition.getRow() - 1;
            if (board.getPiece(new ChessPosition(tempx, tempy)) == null) {
                if (piece.pieceColor == ChessGame.TeamColor.BLACK) {
                    if (piecePosition.getRow() - 1 == 1) {
                        // piece = ChessMove.getPromotionPiece(); POSSIBLY HAVE TO ADD THIS
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.QUEEN));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.BISHOP));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.KNIGHT));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.ROOK));
                    } else {
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    }
                }
            }

        }


        //Taking an Enemy Piece, Right Up Or Right Down:
        tempx = piecePosition.getRow() + 1;
        tempy = piecePosition.getColumn() + 1;
        if (piecePosition.getColumn() <= 7) {
            if (board.getPiece(new ChessPosition(tempx, tempy)) != null && piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                if (piece.pieceColor == ChessGame.TeamColor.WHITE) {
                    if (piecePosition.getRow() + 1 == 8) {
                        // piece = ChessMove.getPromotionPiece(); POSSIBLY HAVE TO ADD THIS
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.QUEEN));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.BISHOP));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.KNIGHT));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.ROOK));
                    } else {
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    }
                }
            }
        }
        tempx = piecePosition.getRow() - 1;
        tempy = piecePosition.getColumn() - 1;
        if (piecePosition.getColumn() > 1) {
            if (board.getPiece(new ChessPosition(tempx, tempy)) != null && piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                if (piece.pieceColor == ChessGame.TeamColor.BLACK) {
                    if (piecePosition.getRow() - 1 == 1) {
                        // piece = ChessMove.getPromotionPiece(); POSSIBLY HAVE TO ADD THIS
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.QUEEN));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.BISHOP));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.KNIGHT));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.ROOK));
                    } else {
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    }
                }
            }
        }


        //Taking an Enemy Piece, Left Up:
        tempx = piecePosition.getRow() + 1;
        tempy = piecePosition.getColumn() - 1;
        if (piecePosition.getColumn() > 1) {
            if (board.getPiece(new ChessPosition(tempx, tempy)) != null && piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                if (piece.pieceColor == ChessGame.TeamColor.WHITE) {
                    if (piecePosition.getRow() + 1 == 8) {
                        // piece = ChessMove.getPromotionPiece(); POSSIBLY HAVE TO ADD THIS
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.QUEEN));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.BISHOP));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.KNIGHT));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.ROOK));
                    } else {
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    }
                }
            }
        }
        tempx = piecePosition.getRow() - 1;
        tempy = piecePosition.getColumn() + 1;
        if (piecePosition.getColumn() <= 7) {
            if (board.getPiece(new ChessPosition(tempx, tempy)) != null && piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                if (piece.pieceColor == ChessGame.TeamColor.BLACK) {
                    if (piecePosition.getRow() - 1 == 1) {
                        // piece = ChessMove.getPromotionPiece(); POSSIBLY HAVE TO ADD THIS
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.QUEEN));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.BISHOP));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.KNIGHT));
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.ROOK));
                    } else {
                        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                    }
                }
            }
        }


        return validPawnMoves;
    }




























}

