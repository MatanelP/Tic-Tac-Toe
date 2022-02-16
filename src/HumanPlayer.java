import java.util.Scanner;

/**
 * Represents a human Player in the game.
 * This player will ask its user for input in order to place the next mark.
 *
 * @author Matanel Pataki
 */
public class HumanPlayer implements Player {

    /* ************ PRIVATE MEMBERS *********** */
    private final Scanner in = new Scanner(System.in);
    public static final String TYPE_INPUT_MSG = "Player %s," +
            " type coordinates: ";
    public static final String INPUT_ERR_MSG = "Invalid coordinates," +
            " type again: ";

    /**
     * Given a board and a mark, this function places (if able) the mark
     * at the best location the user deems.
     *
     * @param board - The board to place the mark in.
     * @param mark  - The type of mark to be placed.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int userInput, row, col;
        do {
            System.out.println(String.format(TYPE_INPUT_MSG, mark.toString()));
            userInput = in.nextInt();
            row = (userInput / 10) - 1;
            col = (userInput % 10) - 1;
        }
        while (!playIfValidInput(board, mark, row, col));
    }

    private boolean playIfValidInput(Board brd, Mark mark, int row, int col) {
        if (brd.putMark(mark, row, col))
            return true;
        System.out.println(INPUT_ERR_MSG); // uncomment for user engagement
        return false;
    }
}