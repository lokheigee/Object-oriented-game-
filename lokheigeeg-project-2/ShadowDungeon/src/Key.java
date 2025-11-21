import bagel.Image;
import bagel.util.Point;
/**
 * Represents a collectible key that the player can pick up.
 */
public class Key {
    private Point position;
    private Image image;
    private boolean isActive = true;
    /**
     * Creates a key at the specified position.
     * @param position The position of the key.
     */
    public Key(Point position) {
        this.position = position;
        this.image = new Image("res/key.png");
    }
    /**
     * Updates the key’s state and checks for collision with the player.
     * @param player The player to check for collision.
     */
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            player.addKey(1);
            isActive = false;
        }
    }

    /**
     * Draws the key on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }
    /**
     * Checks if the key collides with the player.
     * @param player The player to check collision with.
     * @return True if a collision occurs, false otherwise.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    /**
     * Checks if the key is still active (not yet collected).
     * @return True if active, false otherwise.
     */
    public boolean isActive() { return isActive; }

}


