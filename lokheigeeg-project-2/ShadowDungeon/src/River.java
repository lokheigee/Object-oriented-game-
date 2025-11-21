import bagel.Image;
import bagel.util.Point;

/**
 * Hazard that applies damage for as long as the player is on it
 */
public class River{
    private final Point position;
    private final Image image;
    private final double damagePerFrame;
    /**
     * Creates a River hazard at the specified position.
     * @param position The position of the river.
     */
    public River(Point position) {
        this.position = position;
        this.image = new Image("res/river.png");
        damagePerFrame = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame"));
    }
    /**
     * Updates the river state and applies damage if the player is overlapping and not immune.
     * @param player The player to check for collision and apply damage to.
     */
    public void update(Player player) {
        if (hasCollidedWith(player) && !player.isImmuneToRiver()) {
            player.receiveDamage(damagePerFrame);
        }
    }
    /**
     * Draws the river on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }
    /**
     * Checks if the player has collided with this river.
     * @param player The player to check collision with.
     * @return true if the player overlaps with the river; otherwise false.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
}