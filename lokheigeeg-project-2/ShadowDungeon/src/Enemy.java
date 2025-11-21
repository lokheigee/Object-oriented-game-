import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Represents an abstract enemy with health, position, and collision logic.
 */
public abstract class Enemy {
    /** The current position of the enemy. */
    public Point position;
    /** The image representing the enemy. */
    public Image image;
    /** The previous position of the enemy. */
    public Point prevPos;
    /** The current health of the enemy. */
    public double health;
    /** The number of coins the enemy may drop upon death. */
    public double coins = 0;
    /** The movement speed of the enemy. */
    public double speed;
    /** Whether the enemy is currently active in the battle. */
    public boolean active = false; // only true when the Battle Room has been activated
    /** Whether the enemy has been defeated. */
    public boolean dead = false;

    /**
     * Creates an enemy at the given position.
     * @param position The starting position of the enemy.
     */
    public Enemy(Point position){
        this.position = position;
    }

    /**
     * Updates the enemy’s state and behavior.
     * @param player The player to interact with.
     */
    public void update(Player player) {
    }
    /**
     * Draws the enemy on the screen.
     */
    public void draw(){
        image.draw(position.x, position.y);
    }

    /**
     * Checks if the enemy has collided with the player.
     * @param player The player to check for collision.
     * @return True if the enemy collides with the player, false otherwise.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition())
        );
    }
    /**
     * Checks if the enemy is dead.
     * @return True if dead, false otherwise.
     */
    public boolean isDead() {
        return dead;
    }
    /**
     * Checks if the enemy is currently active.
     * @return True if active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }
    /**
     * Sets the enemy’s active state.
     * @param active True if active, false otherwise.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    /**
     * Gets the enemy’s current position.
     * @return The enemy’s position.
     */
    public Point getPosition(){
        return position;
    }

    /**
     * Applies damage to the enemy and checks if it dies.
     * @param damage The amount of damage to apply.
     */
    public void receiveDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            setActive(false);
            dead = true;
        }
    }

    /**
     * Gets the bounding box of the enemy for collision detection.
     * @return The bounding box of the enemy.
     */
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }





}
