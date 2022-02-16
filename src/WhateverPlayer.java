import java.util.Random;

/**
 * Represents a somewhat "random" Player in the game.
 * This player chooses an evenly distributed open location on the board
 * to place the mark in.
 *
 * @author Matanel Pataki
 */
public class WhateverPlayer implements Player {

    /* ************ PRIVATE MEMBERS *********** */
    private final Random random = new Random();

    /**
     * Given a board and a mark, this function places the mark at random.
     *
     * @param board - The board to place the mark in.
     * @param mark  - The type of mark to be placed.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        board.putMark(mark, this.random.nextInt(Board.SIZE),
                this.random.nextInt(Board.SIZE));
    }

}