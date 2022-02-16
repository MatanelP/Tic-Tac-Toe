import java.util.Random;

/**
 * Represents a somewhat "clever" Player in the game.
 * This player is determined to keep on placing it mark in a certain direction.
 * By Doing that, it is able to make streaks faster than in random placements.
 *
 *  @author Matanel Pataki
 */
public class CleverPlayer implements Player {

    /* ************ PRIVATE MEMBERS *********** */
    private final Random random = new Random();
    private final int[] lastPlacedLocation = // keeps the last mark location
            new int[]{random.nextInt(Board.SIZE), random.nextInt(Board.SIZE)};
    private Direction currentDirection = Direction.U; // current direction

    /* ************ PUBLIC METHODS *********** */

    /**
     * Given a board and a mark, this function places (if able) the mark
     * at the best location it deems.
     *
     * @param board - The board to place the mark in.
     * @param mark  - The type of mark to be placed.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int[] newMarkLocation =
                new int[]{lastPlacedLocation[0], lastPlacedLocation[1]};
        keepOnCurrentDirection(newMarkLocation);
        while (!board.putMark(mark, newMarkLocation[0], newMarkLocation[1])) {
            findNewDirection(board);
            keepOnCurrentDirection(newMarkLocation);
        }
        lastPlacedLocation[0] = newMarkLocation[0];
        lastPlacedLocation[1] = newMarkLocation[1];
    }

    /* ************ PRIVATE METHODS *********** */

    /*
        updates the location to the new mark to be placed according to
        the current direction.
     */
    private void keepOnCurrentDirection(int[] newMarkLocation) {
        switch (currentDirection) {
            case U:
                newMarkLocation[0] = lastPlacedLocation[0] - 1;
                newMarkLocation[1] = lastPlacedLocation[1];
                break;
            case R:
                newMarkLocation[0] = lastPlacedLocation[0];
                newMarkLocation[1] = lastPlacedLocation[1] + 1;
                break;
            case D:
                newMarkLocation[0] = lastPlacedLocation[0] + 1;
                newMarkLocation[1] = lastPlacedLocation[1];
                break;
            case L:
                newMarkLocation[0] = lastPlacedLocation[0];
                newMarkLocation[1] = lastPlacedLocation[1] - 1;
                break;
            case UR:
                newMarkLocation[0] = lastPlacedLocation[0] - 1;
                newMarkLocation[1] = lastPlacedLocation[1] + 1;
                break;
            case UL:
                newMarkLocation[0] = lastPlacedLocation[0] - 1;
                newMarkLocation[1] = lastPlacedLocation[1] - 1;
                break;
            case DR:
                newMarkLocation[0] = lastPlacedLocation[0] + 1;
                newMarkLocation[1] = lastPlacedLocation[1] + 1;
                break;
            case DL:
                newMarkLocation[0] = lastPlacedLocation[0] + 1;
                newMarkLocation[1] = lastPlacedLocation[1] - 1;
                break;
        }
    }

    /*
        Finds new direction to placed marks in according to vacancy on the
        board. A useful function in case the player gets "stuck".
     */
    private void findNewDirection(Board board) {
        int row = lastPlacedLocation[0];
        int col = lastPlacedLocation[1];
        if (validCoordinates(row + 1, col) &&
                board.getMark(row + 1, col) == Mark.BLANK) {
            currentDirection = Direction.D;
            return;
        }
        if (validCoordinates(row, col + 1) &&
                board.getMark(row, col + 1) == Mark.BLANK) {
            currentDirection = Direction.R;
            return;
        }
        if (validCoordinates(row - 1, col) &&
                board.getMark(row - 1, col) == Mark.BLANK) {
            currentDirection = Direction.U;
            return;
        }
        if (validCoordinates(row, col - 1) &&
                board.getMark(row, col - 1) == Mark.BLANK) {
            currentDirection = Direction.L;
            return;
        }
        if (validCoordinates(row + 1, col + 1) &&
                board.getMark(row + 1, col + 1) == Mark.BLANK) {
            currentDirection = Direction.DR;
            return;
        }
        if (validCoordinates(row - 1, col - 1) &&
                board.getMark(row - 1, col - 1) == Mark.BLANK) {
            currentDirection = Direction.UL;
            return;
        }
        if (validCoordinates(row + 1, col - 1) &&
                board.getMark(row + 1, col - 1) == Mark.BLANK) {
            currentDirection = Direction.DL;
            return;
        }
        if (validCoordinates(row - 1, col + 1) &&
                board.getMark(row - 1, col + 1) == Mark.BLANK) {
            currentDirection = Direction.UR;
            return;
        }
        // if got here, there is no open direction to go in, there for,
        // randomize the next location on the board:
        lastPlacedLocation[0] = random.nextInt(Board.SIZE);
        lastPlacedLocation[1] = random.nextInt(Board.SIZE);
    }

    /*
        Checks whether the given coordinates are in the board dimensions.
    */
    private boolean validCoordinates(int row, int col) {
        return 0 <= row && row < Board.SIZE && 0 <= col && col < Board.SIZE;
    }
}