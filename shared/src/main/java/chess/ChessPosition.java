package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int pieceRow;
    private int pieceCol;

    public ChessPosition(int row, int col) {
        this.pieceRow = row;
        this.pieceCol = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {

        return pieceRow;

        // FINISHED: throw new RuntimeException("Not implemented");
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {

        return pieceCol;

        // FINISHED: throw new RuntimeException("Not implemented");
    }
}
