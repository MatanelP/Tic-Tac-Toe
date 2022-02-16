/**
 * A Player type factory, building player according to given input.
 *
 * @author Matanel Pataki
 */
public class PlayerFactory {

    /* ************ PUBLIC MEMBERS *********** */
    public static final String HUMAN = "human";
    public static final String WHATEVER = "whatever";
    public static final String CLEVER = "clever";
    public static final String SNARTYPAMTS = "snartypamts";

    /**
     * @param playerType - a String representing the type of player to build.
     * @return A new player typed object accordingly, null if bad input.
     */
    public Player buildPlayer(String playerType) {
        switch (playerType) {
            case HUMAN:
                return new HumanPlayer();
            case WHATEVER:
                return new WhateverPlayer();
            case CLEVER:
                return new CleverPlayer();
            case SNARTYPAMTS:
                return new SnartypamtsPlayer();
        }
        return null;
    }
}