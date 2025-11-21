import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
/**
 * Represents a table obstacle that blocks player movement until destroyed.
 */
public class Table implements Obstacle{
        private Point position;
        private Image image;
        private boolean isDestoryed = false;
    /**
     * Creates a Table at the specified position.
     * @param position The position of the table.
     */
        public Table(Point position){
            this.position = position;
            this.image = new Image("res/table.png");
        }
    /**
     * Updates the table and prevents the player from passing through if it’s not destroyed.
     * @param player The player attempting to move near the table.
     */
        public void update(Player player) {
            if(isDestoryed){
                return;
            }
            if (hasCollidedWith(player)) {
                // set the player to its position prior to attempting to move through this wall
                player.move(player.getPrevPosition().x, player.getPrevPosition().y);
            }
        }
    /**
     * Draws the table on the screen.
     */
        public void draw() {
            image.draw(position.x, position.y);
        }
    /**
     * Checks if the player has collided with the table.
     * @param player The player to check collision with.
     * @return true if the player collides with the table; otherwise false.
     */
        public boolean hasCollidedWith(Player player) {
            return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
        }
    /**
     * Returns the bounding box of the table for collision detection.
     * @return The bounding box of the table.
     */
        @Override
        public Rectangle getBoundingBox() {
            return image.getBoundingBoxAt(position);
        }
    /**
     * Sets whether the table is destroyed.
     * @param isDestoryed true if destroyed; otherwise false.
     */
        public void setIsDestoryed(boolean isDestoryed) {
            this.isDestoryed = isDestoryed;
        }

    /**
     * Checks whether the table has been destroyed.
     * @return true if destroyed; otherwise false.
     */
        public boolean isDestoryed() {
            return isDestoryed;
        }



}
