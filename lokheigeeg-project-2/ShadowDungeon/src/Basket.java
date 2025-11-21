import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Represents a basket obstacle that blocks player movement and provides coins when destroyed.
 */
public class Basket implements Obstacle{
    private Point position;
    private Image image;
    private double coins;
    private boolean isDestoryed = false;

    /**
     * Creates a new basket at the specified position.
     * @param position The position of the basket.
     */
    public Basket(Point position){
        this.position = position;
        this.image = new Image("res/basket.png");
        this.coins = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("basketCoin"));
    }

    /**
     * Updates the basket’s state and checks for collision with the player.
     * @param player The player in the current room.
     */
    public void update(Player player) {
        if (isDestoryed){
            return;
        }
        if (hasCollidedWith(player)) {
            // set the player to its position prior to attempting to move through this wall
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }

    /**
     * Draws the basket on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * Checks if the basket collides with the player.
     * @param player The player to check collision with.
     * @return True if a collision occurs, false otherwise.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }


    /**
     * Gets the bounding box of the basket.
     * @return The bounding box of the basket.
     */
    @Override
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }


    /**
     * Sets whether the basket is destroyed.
     * @param isDestoryed True if destroyed, false otherwise.
     */
    public void setIsDestoryed(boolean isDestoryed) {
        this.isDestoryed = isDestoryed;
    }
    /**
     * Checks if the basket is destroyed.
     * @return True if destroyed, false otherwise.
     */
    public boolean isDestoryed() {
        return isDestoryed;
    }
    /**
     * Gets the number of coins dropped by the basket.
     * @return The coin value.
     */
    public double getCoins() {
        return coins;
    }


}
