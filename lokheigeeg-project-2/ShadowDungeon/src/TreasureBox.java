import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * Chest that can be unlocked by the player to earn coins
 */
public class TreasureBox {
    private final Point position;
    private final Image image;
    private final double coinValue;
    private boolean active = true;
    /**
     * Creates a TreasureBox at the specified position with a coin reward.
     * @param position The position of the treasure box.
     * @param coinValue The number of coins awarded when unlocked.
     */
    public TreasureBox(Point position, double coinValue) {
        this.position = position;
        this.coinValue = coinValue;
        this.image = new Image("res/treasure_box.png");
    }
    /**
     * Updates the treasure box state and allows the player to unlock it with a key.
     * @param input The current keyboard input.
     * @param player The player interacting with the treasure box.
     */
    public void update(Input input, Player player) {
        if (hasCollidedWith(player) && input.wasPressed(Keys.K) && player.getKeys() > 0) {
            player.useKey();
            player.earnCoins(coinValue);
            active = false;
        }
    }

    /**
     * Draws the treasure box on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }


    /**
     * Checks if the player has collided with this treasure box.
     * @param player The player to check collision with.
     * @return true if the player's bounding box intersects the treasure box; otherwise false.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
    /**
     * Returns whether the treasure box is still active (unopened).
     * @return true if active; otherwise false.
     */
    public boolean isActive() {
        return active;
    }
}