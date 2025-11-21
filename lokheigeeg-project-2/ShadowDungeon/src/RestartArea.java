import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * Area in Prep or End Room where the player can trigger a game reset
 */
public class RestartArea {
    private final Point position;
    private final Image image;

    /**
     * Creates a RestartArea at the specified position.
     * @param position The position of the restart area.
     */
    public RestartArea(Point position) {
        this.position = position;
        this.image = new Image("res/restart_area.png");
    }
    /**
     * Updates the restart area and resets the game if the player overlaps and presses ENTER.
     * @param input The current keyboard input.
     * @param player The player interacting with the area.
     */
    public void update(Input input, Player player) {
        if (hasCollidedWith(player) && input.wasPressed(Keys.ENTER)) {
            ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
        }
    }

    /**
     * Draws the restart area on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * Checks if the player has collided with this restart area.
     * @param player The player to check collision with.
     * @return true if the player's bounding box intersects the restart area; otherwise false.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
}
