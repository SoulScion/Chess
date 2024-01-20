package chess;

import java.util.Objects;
import java.util.Vector;
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
        Collection<ChessMove> possibleMoveCollection = new Vector<ChessMove>();
        ChessPiece movingPiece = board.getPiece(myPosition);

        if (movingPiece.type == PieceType.KING) {
            return null;
        } else if (movingPiece.type == PieceType.QUEEN){
            return null;
        } else if (movingPiece.type == PieceType.BISHOP) {
            return bishopMoves(movingPiece, board, myPosition);
        } else if (movingPiece.type == PieceType.KNIGHT) {
            return null;
        } else if (movingPiece.type == PieceType.ROOK) {
            return null;
        } else if (movingPiece.type == PieceType.PAWN) {
            return null;
        }

        throw new RuntimeException("Not implemented");
    }

    private void validMove(int x, int y, ChessBoard board, ChessPiece pieceChecked, ChessPosition piecePosition, Vector<ChessMove> validMoves) {
        int tempx = x;
        int tempy = y;
        if (tempx <= 8 && tempy <= 8 && tempx > 0 && tempy > 0) { // First Check
            if (board.getPiece(new ChessPosition(tempx, tempy)) == null || pieceChecked.pieceColor != board.getPiece(new ChessPosition(tempx, tempy)).pieceColor) { // Second Check
                validMoves.add(new ChessMove(piecePosition, new ChessPosition(tempx, tempy), null));
            }
        }
    }

    private Collection<ChessMove> kingPiece(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        Vector<ChessMove> validKingMoves = new Vector<>();
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

    private Collection<ChessMove> queenMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        return null;
    }

    private Collection<ChessMove> bishopMoves(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        Vector<ChessMove> validBishopMoves = new Vector<>();
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
                    System.out.printf("%d, %d\n", tempx, tempy);
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
                    System.out.printf("%d, %d\n", tempx, tempy);
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
                    System.out.printf("%d, %d\n", tempx, tempy);
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

    private Collection<ChessMove> knightMoves(ChessPiece piece, ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    private Collection<ChessMove> pawnMoves(ChessPiece piece, ChessBoard board, ChessPosition myPosition) {
        return null;
    }




























}

