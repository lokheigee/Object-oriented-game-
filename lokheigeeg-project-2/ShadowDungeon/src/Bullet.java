import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.util.List;

/**
 * Represents a bullet projectile that moves in a given direction and deals damage on collision.
 */
public class Bullet {

    private Point position;
    private Image image;
    private Point prevPos;
    private double speed;
    private double damage;
    private boolean isActive = true;
    private Vector2 d;
    private Point target;
    private double vx, vy;

    /**
     * Creates a new bullet at the given starting position and direction.
     * @param startPosition The starting position of the bullet.
     * @param dir The direction vector the bullet will travel.
     */
    public Bullet(Point startPosition, Vector2 dir){
        this.position = startPosition;
        this.image = new Image("res/bullet.png");
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletSpeed"));
        this.damage = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponStandardDamage"));
        this.vx = dir.x * speed;
        this.vy = dir.y * speed;

    }
    /**
     * Updates the bullet’s position and checks for collisions with obstacles.
     * @param obstacles A list of obstacles the bullet can collide with.
     */
    public void update(List<? extends Obstacle> obstacles){
        if (!isActive) return;

        position = new Point(position.x + vx, position.y + vy);

        Rectangle r = image.getBoundingBoxAt(position);
        Point tl = r.topLeft(), br = r.bottomRight();

        // out of screen
        if (tl.x < 0 || br.x > Window.getWidth() || tl.y < 0 || br.y > Window.getHeight()) {
            isActive = false;
            return;
        }
        for (Obstacle o : obstacles) {
            if (image.getBoundingBoxAt(position).intersects(o.getBoundingBox())) {
                isActive = false;
                return;
            }
        }

    }

    /**
     * Updates the bullet’s position without checking for obstacles.
     */
    public void update(){
        if (!isActive) return;

        position = new Point(position.x + vx, position.y + vy);

        Rectangle r = image.getBoundingBoxAt(position);
        Point tl = r.topLeft(), br = r.bottomRight();

        // out of screen
        if (tl.x < 0 || br.x > Window.getWidth() || tl.y < 0 || br.y > Window.getHeight()) {
            isActive = false; return;
        }
    }


    /**
     * Draws the bullet on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * Checks if the bullet has collided with the player.
     * @param player The player to check for collision.
     * @return True if the bullet collides with the player, false otherwise.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
    /**
     * Gets the bounding box of the bullet.
     * @return The bounding box of this bullet.
     */
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

    /**
     * Checks if the bullet is still active.
     * @return True if active, false otherwise.
     */
    public boolean isActive() {
        return isActive;
    }
    /**
     * Sets the bullet’s active status.
     * @param active True if the bullet should remain active, false otherwise.
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    /**
     * Gets the damage dealt by the bullet.
     * @return The bullet’s damage value.
     */
    public double getDamage(){
        return damage;
    }








}
