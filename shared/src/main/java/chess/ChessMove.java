package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition starterPosition;
    private ChessPosition endingPosition;
    private ChessPiece.PieceType promotePiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {

        this.starterPosition = startPosition;
        this.endingPosition = endPosition;
        this.promotePiece = promotionPiece;

    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {

        return starterPosition;

        // FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {

        return endingPosition;

        // FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        if (promotePiece == null) {
            return null;
        } else {
            return promotePiece;
        }

        // FINISHED: throw new RuntimeException("Not implemented");
    }
}
