/**
 * Represents a tournament between 2 players, which will be chosen by the user.
 * The user will also supply the number of rounds to be play in the tournament.
 *
 * @author Matanel Pataki
 */
public class Tournament {

    /* ************ PRIVATE MEMBERS *********** */
    private static final String BAD_INPUT_ERR =
            "Usage: java Tournament [round count]" +
                    " [render target: console/none]" +
                    " [/player1: human/clever/whatever/snartypamts]" +
                    " [player2: human/clever/whatever/snartypamts]";
    private static final String END_TOURNAMENT_MSG = "=== player 1: %d |" +
            " player 2: %d | Draws: %d ===\r";
    private final int rounds;
    private final Renderer renderer;
    private final Player[] Players;
    private static final int ROUNDS = 0;
    private static final int RENDERER = 1;
    private static final int PLAYER1 = 2;
    private static final int PLAYER2 = 3;

    /**
     * CONSTRUCTOR -
     * Initializes the tournament.
     *
     * @param rounds   - Number of rounds to be played.
     * @param renderer - Represents the renderer of the board.
     * @param players  - Array with Represents of the players.
     */
    public Tournament(int rounds, Renderer renderer, Player[] players) {
        this.Players = players;
        this.renderer = renderer;
        this.rounds = rounds;
    }

    /* ************ PUBLIC METHODS *********** */

    /**
     * Starting to play the games the amount of rounds specified by the user.
     * Alternating between the two player in every round.
     * Keeping scores of each player's winning games and draws.
     */
    public void playTournament() {
        int[] winnings = new int[3];
        /* winnings[0] - player1 winnings
           winnings[1] - player2 winnings
           winnings[2] - draws, */
        for (int i = 0; i < this.rounds; i++) {
            Game game = new Game(Players[i % Players.length],
                    Players[(i + 1) % Players.length], this.renderer);
            Mark winner = game.run();
            updateWinnings(winnings, i, winner);
        }
        printWinnings(winnings);
    }

    /* ************ PRIVATE METHODS *********** */

    /*
        Updates the scores after each round played in the tournament.
     */
    private void updateWinnings(int[] winnings, int i, Mark winner) {
        switch (winner) {
            case X:
                if (i % Players.length == 0) {
                    winnings[0]++;
                } else {
                    winnings[1]++;
                }
                break;
            case O:
                if (i % Players.length == 0) {
                    winnings[1]++;
                } else {
                    winnings[0]++;
                }
                break;
            case BLANK:
                winnings[2]++;
        }
    }

    /*
        Printing the winning.
     */
    private static void printWinnings(int[] winnings) {
        System.out.println(String.format(END_TOURNAMENT_MSG,
                winnings[0], winnings[1], winnings[2]));
    }

    /*
        Checking valid input from command line
     */
    private static boolean inputIsNotValid(String[] args) {
        if (args.length == 4 && Integer.parseInt(args[ROUNDS]) >= 0) {
            return false;
        }
        System.err.println(BAD_INPUT_ERR);
        return true;
    }

    /* ************ MAIN *********** */

    public static void main(String[] args) {
        if (inputIsNotValid(args)) return;
        PlayerFactory playerFactory = new PlayerFactory();
        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(args[RENDERER]);
        Player player1 = playerFactory.buildPlayer(args[PLAYER1]);
        Player player2 = playerFactory.buildPlayer(args[PLAYER2]);
        if (renderer == null || player1 == null || player2 == null)
        {
            System.out.println(BAD_INPUT_ERR);
            return;
        }
        Tournament tournament = new Tournament(Integer.parseInt(args[ROUNDS]),
                renderer, new Player[]{player1, player2});
        tournament.playTournament();
    }
}