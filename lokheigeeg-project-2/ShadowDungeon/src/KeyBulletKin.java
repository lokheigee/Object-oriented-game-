import bagel.Image;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.util.ArrayList;

/**
 * Enemy that follows a preset route, damages the player on contact, and is removed when overlapped.
 */
public class KeyBulletKin extends Enemy{
    private String property;
    private int currentIndex = 0;
    private ArrayList<Point> route = new ArrayList<>();
    private final int START = 1;
   // private Point prevPosition;
    private int currentInde = 0;
    private final double CONTACT = 0.2;

    /**
     * Creates a KeyBulletKin at the given position and loads its route for the specified room.
     * @param startPos The starting position of the enemy.
     * @param room The room key used to load the patrol route from properties.
     */
    public KeyBulletKin(Point startPos, String room) {
        super(startPos);
        this.image = new Image("res/key_bullet_kin.png");
        this.health = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinHealth"));
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinSpeed"));
        this.active = false;
        this.dead = false;
        this.property = ShadowDungeon.getGameProps().getProperty("keyBulletKin."+ room);
        for (String coords : property.split(";")) {
            route.add((IOUtils.parseCoords(coords)));
        }
    }
    /**
     * Updates movement along the route and checks collision with the player.
     * @param player The player to interact with and damage on contact.
     */
@Override
    public void update(Player player) {
        if (route.isEmpty()) return ;

        Point target = route.get(currentIndex);

        Vector2 d = new Vector2(target.x - this.position.x, target.y - this.position.y); //Set direction
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
            currentIndex++;

            // Loop back to first point
            if (currentIndex >= route.size()) {
                currentIndex = 0;
            }
        }

        if (hasCollidedWith(player)) {
            dead = true;
            active = false;
            player.receiveDamage(CONTACT);
        }

    }
    /**
     * Moves the enemy to the specified coordinates.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void move(double x, double y) {
        this.prevPos = position;
        position = new Point(x, y);
    }

    /**
     * Draws the enemy on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }
    /**
     * Checks if this enemy collides with the player.
     * @param player The player to check for collision.
     * @return True if a collision occurs, false otherwise.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
    /**
     * Checks if this enemy is dead.
     * @return True if dead, false otherwise.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Checks if this enemy is active.
     * @return True if active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }
    /**
     * Sets the active state of this enemy.
     * @param active True to activate, false to deactivate.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
