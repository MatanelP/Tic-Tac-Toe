/**
 * Represents a board on which the game is being played on.
 * The board updates according to the putMark function.
 * Also, the board provides valid info of the game status
 * to the players, regarding mark placements ect.
 *
 *  @author Matanel Pataki
 */
public class Board {

    /* ************ PUBLIC MEMBERS *********** */
    public static final int SIZE = 6;
    public static final int WIN_STREAK = 4;

    /* ************ PRIVATE MEMBERS *********** */
    private final Mark[][] board = new Mark[SIZE][SIZE];
    private int marksPlaced; // Number of marks currently on board
    private Mark winner; // The winner of the game, according to the board

    /**
     * CONSTRUCTOR -
     * Initializes the board to be filled with all blanks marks.
     */
    public Board() {
        this.marksPlaced = 0;
        this.winner = null;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = Mark.BLANK;
            }
        }
    }

    /* ************ PUBLIC METHODS *********** */

    /**
     * Accessing the board at the given coordinates to
     * return the marked placed in them.
     *
     * @param row  - The row to access on the board.
     * @param col- The col to access on the board.
     * @return The mark set at the given location.
     */
    public Mark getMark(int row, int col) {
        if (validCoordinates(row, col))
            return board[row][col];
        return Mark.BLANK;
    }

    /**
     * Accessing the board at the given coordinates to
     * place the mark given in them.
     *
     * @param mark - The mark to be placed.
     * @param row  - The row to access on the board.
     * @param col- The col to access on the board.
     * @return - True if the placement was successful, False otherwise.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (validInputToPutNewMark(mark, row, col)) {
            this.board[row][col] = mark;
            this.marksPlaced += 1;
            checkForAWinStreak(mark, row, col);
            return true;
        }
        return false;
    }

    /**
     * @return A mark, representing the winner of the game (null if none).
     */
    public Mark getWinner() {
        return this.winner;
    }

    /**
     * @return An indication whether the game is still on going or not.
     */
    public boolean gameEnded() {
        return this.winner != null;
    }

    /* ************ PRIVATE METHODS *********** */

    /*
       Checks whether the given mark and coordinates are valid as input
       for pubMark.
     */
    private boolean validInputToPutNewMark(Mark mark, int row, int col) {
        return (mark == Mark.X || mark == Mark.O)
                && validCoordinates(row, col)
                && getMark(row, col) == Mark.BLANK;
    }

    /*
        By traversing the board horizontally, vertically and diagonally,
        determining whether there is a winner to the game.
        If so, updating the winner.
        The function is being called after each mark placement.
     */
    private void checkForAWinStreak(Mark mark, int row, int col) {
        // horizontal and vertical streaks:
        int vertical = countMarks(row, col, 0, -1, mark) +
                countMarks(row, col, 0, 1, mark) - 1;
        int horizontal = countMarks(row, col, -1, 0, mark) +
                countMarks(row, col, 1, 0, mark) - 1;
        // diagonals streaks:
        int diagonal1 = countMarks(row, col, -1, 1, mark) +
                countMarks(row, col, 1, -1, mark) - 1;
        int diagonal2 = countMarks(row, col, 1, 1, mark) +
                countMarks(row, col, -1, -1, mark) - 1;
        if (horizontal >= WIN_STREAK || vertical >= WIN_STREAK ||
                diagonal1 >= WIN_STREAK || diagonal2 >= WIN_STREAK) {
            if (mark == Mark.X) {
                this.winner = Mark.X;
            } else {
                this.winner = Mark.O;
            }
            return;
        }
        if (this.marksPlaced == SIZE * SIZE) { // in case of a draw
            this.winner = Mark.BLANK;
        }
    }

    /*
        Counts how many marks are on the streak in a given direction.
        The direction is being represented by rowDelta and colDelta.
     */
    private int countMarks(int row, int col,
                           int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        while (row < SIZE && row >= 0 &&
                col < SIZE && col >= 0 &&
                board[row][col] == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    /*
        Checks whether the given coordinates are in the board dimensions.
     */
    private boolean validCoordinates(int row, int col) {
        return 0 <= row && row < SIZE && 0 <= col && col < SIZE;
    }
}