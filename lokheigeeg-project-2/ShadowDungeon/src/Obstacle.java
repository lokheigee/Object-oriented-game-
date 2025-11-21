import bagel.util.Rectangle;
/**
 * Represents an obstacle that has a bounding box for collision detection.
 */
public interface Obstacle {
    /**
     * Returns the bounding box of the obstacle.
     * @return The bounding box of this obstacle.
     */
    Rectangle getBoundingBox();
}
