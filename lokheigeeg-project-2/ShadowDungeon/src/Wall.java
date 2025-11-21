import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Obstacle that blocks the player from moving through it
 */
public class Wall implements Obstacle {
    private final Point position;
    private final Image image;

    /**
     * Creates a Wall at the specified position.
     * @param position The position of the wall.
     */
    public Wall(Point position) {
        this.position = position;
        this.image = new Image("res/wall.png");
    }
    /**
     * Updates the wall and prevents the player from passing through if a collision occurs.
     * @param player The player attempting to move near the wall.
     */
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            // set the player to its position prior to attempting to move through this wall
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }
    /**
     * Draws the wall on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * Checks if the player has collided with this wall.
     * @param player The player to check collision with.
     * @return true if the player's bounding box intersects the wall; otherwise false.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }


    /**
     * Returns the bounding box of the wall for collision detection.
     * @return The bounding box of the wall.
     */
    @Override
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

}