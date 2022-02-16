/**
 * A Renderer type factory, building renderer according to given input.
 *
 * @author Matanel Pataki
 */
public class RendererFactory {

    /* ************ PUBLIC MEMBERS *********** */
    public static final String CONSOLE = "console";
    public static final String NONE = "none";

    /**
     * @param rendererType - a String representing the type of player to build.
     * @return A new player typed object accordingly, null if bad input.
     */
    public Renderer buildRenderer(String rendererType) {
        switch (rendererType) {
            case CONSOLE:
                return new ConsoleRenderer();
            case NONE:
                return new VoidRenderer();
        }
        return null;
    }
}