/**
 * Represents a game.
 * Creating a new board for the player to play on.
 * After receiving 2 player and a renderer to render the board,
 * The class is able to run a full game until it received confirmation that
 * there is a winner to game.
 *
 * @author Matanel Pataki
 */
public class Game {

    /* ************ PRIVATE MEMBERS *********** */
    private final Player[] Players;
    private final Mark[] marks;
    private final Renderer renderer;
    private final Board board = new Board();
    /* messages to print games status, if needed:
    private static final String STARS_SEP = "*************************";
    private static final String GAME_ENDED_MSG = STARS_SEP + "\nGame Over!\t";
    private static final String DRAW_MSG = "it's a DRAW!";
    private static final String O_WON_MSG = "O has WON!";
    private static final String X_WON_MSG = "X has WON!"; */

    /**
     * CONSTRUCTOR -
     * Initializes the game.
     *
     * @param player1  - Represents the first player.
     * @param player2  - Represents the first player.
     * @param renderer - Represents the renderer of the board.
     */
    public Game(Player player1, Player player2, Renderer renderer) {
        this.Players = new Player[]{player1, player2};
        this.marks = new Mark[]{Mark.X, Mark.O};
        this.renderer = renderer;
    }

    /* ************ PUBLIC METHODS *********** */

    /**
     * Running the game by invoking the playTurn function for each player at
     * its turn.
     * Monitoring whether the game has ended.
     *
     * @return A mark, representing the winner of the current game.
     */
    public Mark run() {
        renderer.renderBoard(board);
        Player currentPlayer;
        for (int i = 0; !board.gameEnded(); i++) {
            currentPlayer = Players[i % Players.length];
            currentPlayer.playTurn(board, marks[i % marks.length]);
            renderer.renderBoard(board);
        }
//        return declareWinner(board.getWinner()); // to get winner massage
        return board.getWinner(); // comment out if declareWinner() is called
    }

    /* for printing game status (uncomment line 53 above as well):
    private Mark declareWinner(Mark status) {
        System.out.print(GAME_ENDED_MSG);
        switch (status) {
            case BLANK:
                System.out.println(DRAW_MSG);
                break;
            case O:
                System.out.println(O_WON_MSG);
                break;
            case X:
                System.out.println(X_WON_MSG);
        }
        System.out.println(STARS_SEP);
        return status;
    } */
}