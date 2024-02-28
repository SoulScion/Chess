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
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
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
        if (x <= 8 && y <= 8 && x > 0 && y > 0) { // First Check
            if (board.getPiece(new ChessPosition(x, y)) == null) { // Second Check
                validMoves.add(new ChessMove(piecePosition, new ChessPosition(x, y), null));
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

        do {
            tempx = tempx + 1;
            tempy = tempy + 1;
        } while (!rookMoveHelper(piece, board, piecePosition, validBishopMoves, tempx, tempy));

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        do {
            tempx = tempx - 1;
            tempy = tempy + 1;
        } while (!rookMoveHelper(piece, board, piecePosition, validBishopMoves, tempx, tempy));

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        do {
            tempx = tempx + 1;
            tempy = tempy - 1;
        } while (!rookMoveHelper(piece, board, piecePosition, validBishopMoves, tempx, tempy));

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        do {
            tempx = tempx - 1;
            tempy = tempy - 1;
        } while (!rookMoveHelper(piece, board, piecePosition, validBishopMoves, tempx, tempy));

        return validBishopMoves;
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

        do {
            tempx = tempx + 1;
        } while (!rookMoveHelper(piece, board, piecePosition, validRookMoves, tempx, tempy));

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        do {
            tempx = tempx - 1;
        } while (!rookMoveHelper(piece, board, piecePosition, validRookMoves, tempx, tempy));

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        do {
            tempy++;
        } while (!rookMoveHelper(piece, board, piecePosition, validRookMoves, tempx, tempy));

        tempx = piecePosition.getRow();
        tempy = piecePosition.getColumn();

        do {
            tempy = tempy - 1;
        } while (!rookMoveHelper(piece, board, piecePosition, validRookMoves, tempx, tempy));

        return validRookMoves;
    }

    private boolean rookMoveHelper(ChessPiece piece, ChessBoard board, ChessPosition piecePosition, ArrayList<ChessMove> validRookMoves, int tempx, int tempy) {
        if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) {
            if (board.getPiece(new ChessPosition(tempx, tempy )) == null || piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                validRookMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
                if (board.getPiece(new ChessPosition(tempx, tempy )) != null) {
                    return piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private ArrayList<ChessMove> pawnMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        ArrayList<ChessMove> validPawnMoves = new ArrayList<>();
        int tempx = piecePosition.getRow();
        int tempy = piecePosition.getColumn();

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

        tempx = piecePosition.getRow() + 1;
        if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) {
            if (board.getPiece(new ChessPosition(tempx, tempy)) == null) {
                pawnMovesPosDiagonal(piece, piecePosition, validPawnMoves, tempx, tempy);
            }
            tempx = piecePosition.getRow() - 1;
            if (board.getPiece(new ChessPosition(tempx, tempy)) == null) {
                pawnMovesNegDiagonal(piece, piecePosition, validPawnMoves, tempx, tempy);
            }

        }


        tempx = piecePosition.getRow() + 1;
        tempy = piecePosition.getColumn() + 1;
        if (piecePosition.getColumn() <= 7) {
            pawnSettingMoves(piece, board, piecePosition, tempx, tempy, validPawnMoves);
        }
        tempx = piecePosition.getRow() - 1;
        tempy = piecePosition.getColumn() - 1;
        if (piecePosition.getColumn() > 1) {
            if (board.getPiece(new ChessPosition(tempx, tempy)) != null && piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                pawnMovesNegDiagonal(piece, piecePosition, validPawnMoves, tempx, tempy);
            }
        }


        tempx = piecePosition.getRow() + 1;
        tempy = piecePosition.getColumn() - 1;
        if (piecePosition.getColumn() > 1) {
            pawnSettingMoves(piece, board, piecePosition, tempx, tempy, validPawnMoves);
        }
        tempx = piecePosition.getRow() - 1;
        tempy = piecePosition.getColumn() + 1;
        if (piecePosition.getColumn() <= 7) {
            if (board.getPiece(new ChessPosition(tempx, tempy)) != null && piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
                pawnMovesNegDiagonal(piece, piecePosition, validPawnMoves, tempx, tempy);
            }
        }


        return validPawnMoves;
    }

    private void pawnSettingMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition, int tempx, int tempy, ArrayList<ChessMove> validPawnMoves) {
        if (board.getPiece(new ChessPosition(tempx, tempy)) != null && piece.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) {
            pawnMovesPosDiagonal(piece, piecePosition, validPawnMoves, tempx, tempy);
        }
    }

    private void pawnMovesNegDiagonal(ChessPiece piece, ChessPosition piecePosition, ArrayList<ChessMove> validPawnMoves, int tempx, int tempy) {
        if (piece.pieceColor == ChessGame.TeamColor.BLACK) {
            if (piecePosition.getRow() - 1 == 1) {
                pawnPromotionChecking(piecePosition, validPawnMoves, tempx, tempy);
            } else {
                validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
            }
        }
    }

    private void pawnPromotionChecking(ChessPosition piecePosition, ArrayList<ChessMove> validPawnMoves, int tempx, int tempy) {
        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.QUEEN));
        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.BISHOP));
        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.KNIGHT));
        validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), PieceType.ROOK));
    }

    private void pawnMovesPosDiagonal(ChessPiece piece, ChessPosition piecePosition, ArrayList<ChessMove> validPawnMoves, int tempx, int tempy) {
        if (piece.pieceColor == ChessGame.TeamColor.WHITE) {
            if (piecePosition.getRow() + 1 == 8) {
                pawnPromotionChecking(piecePosition, validPawnMoves, tempx, tempy);
            } else {
                validPawnMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
            }
        }
    }


}

