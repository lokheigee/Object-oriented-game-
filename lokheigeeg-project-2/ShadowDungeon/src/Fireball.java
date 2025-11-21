import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.util.List;

/**
 * Represents a fireball projectile shot by enemies that damages the player on impact.
 */
public class Fireball {
    private Point position;
    private Image image;
    private Point prevPos;
    private double speed;
    private double damage;
    private boolean isActive = true;

    /**
     * Creates a fireball at the specified starting position.
     * @param startPos The starting position of the fireball.
     */
    public Fireball(Point startPos) {
        this.position = startPos;

        this.image = new Image("res/fireball.png");
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballSpeed"));
        this.damage = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballDamage"));
    }

    /**
     * Updates the fireball’s position and checks for collisions with the player and obstacles.
     * @param player The player to check for collision.
     * @param obstacles The list of obstacles the fireball can collide with.
     */
    public void update(Player player, List<? extends Obstacle> obstacles) {
        Point target = player.getPosition();
        Vector2 d = new Vector2(target.x - this.position.x, target.y - this.position.y);
        double distance = d.length();
        if (distance < 0) return;

        double directionX = d.normalised().x;
        double directionY = d.normalised().y;

        double currX = position.x;
        double currY = position.y;


        currX += directionX * speed;
        currY += directionY * speed;
        // update the player position accordingly and ensure it can't move past the game window
        Rectangle rect = image.getBoundingBoxAt(new Point(currX, currY));
        Point topLeft = rect.topLeft();
        Point bottomRight = rect.bottomRight();
        if (topLeft.x >= 0 && bottomRight.x <= Window.getWidth() && topLeft.y >= 0 && bottomRight.y <= Window.getHeight()) {
            move(currX, currY);
        }
        if (distance < speed) {
            move(target.x, target.y);
        }
        if (hasCollidedWith(player)){
            player.receiveDamage(damage);
            isActive = false;
        }
        for (Obstacle o : obstacles) {
            if (image.getBoundingBoxAt(position).intersects(o.getBoundingBox())) {
                isActive = false;
                return;
            }
        }

    }

    /**
     * Draws the fireball on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * Checks if the fireball has collided with the player.
     * @param player The player to check for collision.
     * @return True if the fireball collides with the player, false otherwise.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    /**
     * Moves the fireball to a new position.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void move(double x, double y) {
        this.prevPos = position;
        position = new Point(x, y);
    }
    /**
     * Checks if the fireball is active.
     * @return True if active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets whether the fireball is active.
     * @param active True if active, false otherwise.
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
}




