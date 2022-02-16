import java.util.Random;

/**
 * Represents a somewhat "smart" Player in the game.
 * This player is determined to keep on placing it mark in a certain direction.
 * By Doing that, it is able to make streaks faster than in random placements.
 * Also, this player is able to block incoming win streaks of the opponent in
 * some situation.
 * This player is tailored to win over the clever player (and by result the
 * random player as well) by knowing its mechanic (which he uses as well).
 *
 * @author Matanel Pataki
 */
public class SnartypamtsPlayer implements Player {

    /* ************ PRIVATE MEMBERS *********** */
    private final Random random = new Random();
    private final int[] lastPlacedLocation =
            new int[]{random.nextInt(Board.SIZE), random.nextInt(Board.SIZE)};
    private Direction currentDirection = Direction.U;

    /* ************ PUBLIC METHODS *********** */

    /**
     * Given a board and a mark, this function places (if able) the mark
     * at the best location it deems.
     *
     * @param board - The board to place the mark in.
     * @param mark  - The type of mark to be placed.
     */
    public void playTurn(Board board, Mark mark) {
        if (!couldWinRightNow(board, mark) &&
                !StoppedOpponentFromWinning(board, mark)) {
            placeNewMark(board, mark);
        }
    }

    /* ************ PRIVATE METHODS *********** */

    /*
        Checking to see if there is a location on the board that if marked
        by the player current mark will result in a win.
        Return true if able to place the winning mark, false otherwise.
     */
    private boolean couldWinRightNow(Board brd, Mark mark) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                int[] winLocation = getLocation(brd, mark, row, col);
                if (winLocation[0] != -1) {
                    brd.putMark(mark, winLocation[0], winLocation[1]);
                    return true;
                }
            }
        }
        return false;
    }

    /*
        This function is being called if the was no need to block an opponent
        winning streak, and there was no possible placement to place a winning
        mark.
        It will place the next mark in a way similar to the clever player.
        Meaning it will keep placing marks in the current direction of the
        player, therefore building potential winning streak.
     */
    private void placeNewMark(Board board, Mark mark) {
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

    /*
        Checking to see if there is a location on the board that if marked
        by the player current mark will result in stopping the opponent from
        gaining a winning streak.
        Return true if able to block the opponent, false otherwise.
     */
    private boolean StoppedOpponentFromWinning(Board brd, Mark mark) {
        Mark opponentMark = getOpponentMark(mark);
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                int[] blockLocation = getLocation(brd, opponentMark, row, col);
                if (blockLocation[0] != -1) {
                    brd.putMark(mark, blockLocation[0], blockLocation[1]);
                    return true;
                }

            }
        }

        return false;
    }

    /*
        Traversing the board from a given coordinates in every direction.
        Finds the coordinates that if marked by the given mark, will result
        in the winning of that mark, than returns it.
     */
    private int[] getLocation(Board board, Mark mark, int row, int col) {
        int[][] optionalLocation = new int[8][2];
        // counting streak in all directions:
        int[] L = countMarks(board, row, col, 0, -1, mark);
        int[] R = countMarks(board, row, col, 0, 1, mark);
        int[] U = countMarks(board, row, col, -1, 0, mark);
        int[] D = countMarks(board, row, col, 1, 0, mark);
        int[] UR = countMarks(board, row, col, -1, 1, mark);
        int[] DL = countMarks(board, row, col, 1, -1, mark);
        int[] DR = countMarks(board, row, col, 1, 1, mark);
        int[] UL = countMarks(board, row, col, -1, -1, mark);
        // collect all possibles coordinates:
        getOptionalLocations(optionalLocation, L, R, U, D, UR, DL, DR, UL);
        // horizontal and vertical streaks:
        int vertical = L[0] + R[0] - 1;
        int horizontal = U[0] + D[0] - 1;
        // diagonals streaks:
        int diagonal1 = UR[0] + DL[0] - 1;
        int diagonal2 = DR[0] + UL[0] - 1;
        // remove invalid location (won't result in a winning streak):
        removeInvalidLocations(optionalLocation, vertical, horizontal,
                diagonal1, diagonal2);
        // from all possible location collected, return a random valid one:
        for (int i = 0; i < 8; i++) {
            if (optionalLocation[i][0] != -1) {
                int randomOptionalIndex;
                do {
                    randomOptionalIndex = random.nextInt(8);
                } while (optionalLocation[randomOptionalIndex][0] == -1);
                return optionalLocation[randomOptionalIndex];
            }
        }
        // if got here, no valid location has found. return indicator:
        return new int[]{-1, -1};
    }

    /*
        Removes invalid location that won't result in a winning streak
        due to the streak found shorter than the needed winning streak.
     */
    private void removeInvalidLocations(int[][] optionalLocation,
                                        int vertical, int horizontal,
                                        int diagonal1, int diagonal2) {
        if (vertical != Board.WIN_STREAK - 1) {
            optionalLocation[0][0] = -1;
            optionalLocation[0][1] = -1;
            optionalLocation[1][0] = -1;
            optionalLocation[1][1] = -1;
        }
        if (horizontal != Board.WIN_STREAK - 1) {
            optionalLocation[2][0] = -1;
            optionalLocation[2][1] = -1;
            optionalLocation[3][0] = -1;
            optionalLocation[3][1] = -1;
        }
        if (diagonal1 != Board.WIN_STREAK - 1) {
            optionalLocation[4][0] = -1;
            optionalLocation[4][1] = -1;
            optionalLocation[5][0] = -1;
            optionalLocation[5][1] = -1;
        }
        if (diagonal2 != Board.WIN_STREAK - 1) {
            optionalLocation[6][0] = -1;
            optionalLocation[6][1] = -1;
            optionalLocation[7][0] = -1;
            optionalLocation[7][1] = -1;
        }
    }

    /*
        Updates the optional locations array with the locations found in the
        streak collecting array.
     */
    private void getOptionalLocations(int[][] optionalLocation,
                                      int[] L, int[] R, int[] U, int[] D,
                                      int[] UR, int[] DL, int[] DR, int[] UL) {
        optionalLocation[0][0] = L[1];
        optionalLocation[0][1] = L[2];

        optionalLocation[1][0] = R[1];
        optionalLocation[1][1] = R[2];

        optionalLocation[2][0] = U[1];
        optionalLocation[2][1] = U[2];

        optionalLocation[3][0] = D[1];
        optionalLocation[3][1] = D[2];

        optionalLocation[4][0] = UR[1];
        optionalLocation[4][1] = UR[2];

        optionalLocation[5][0] = DL[1];
        optionalLocation[5][1] = DL[2];

        optionalLocation[6][0] = DR[1];
        optionalLocation[6][1] = DR[2];

        optionalLocation[7][0] = UL[1];
        optionalLocation[7][1] = UL[2];
    }

    /*
    Counts how many marks are on the streak in a given direction.
    The direction is being represented by rowDelta and colDelta.
    */
    private int[] countMarks(Board board, int row, int col,
                             int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        while (row < Board.SIZE && row >= 0 && col < Board.SIZE && col >= 0 &&
                board.getMark(row, col) == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        if (validCoordinates(row, col) &&
                board.getMark(row, col) == Mark.BLANK) {
            return new int[]{count, row, col};
        }
        return new int[]{count, -1, -1};
    }

    /*
        Determines the opponent's mark based on given mark and returns it.
     */
    private Mark getOpponentMark(Mark mark) {
        Mark opponentMark;
        if (mark == Mark.X) {
            opponentMark = Mark.O;
        } else {
            opponentMark = Mark.X;
        }
        return opponentMark;
    }

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