import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Player character that can move around and between rooms, defeat enemies, collect coins
 */
public class Player {
    /** Previous frame position of the player. */
    public Point prevPosition;
    /** Current position of the player. */
    public Point position;
    /** Current sprite image used to draw the player. */
    public  Image currImage;
    /** Current health value of the player. */
    public double health;
    /** Movement speed per frame. */
    public double speed;
    /** Total coins currently held. */
    public double coins = 0;
    /** Total keys currently held. */
    public double keys = 0;
    /** Current weapon stat value. */
    public double weaponStat = 0;
    /** Whether the player is facing left. */
    public boolean faceLeft = false;
    /** Frames since the last shot was fired. */
    public double lastShot = 1000;
    /** Active bullets fired by the player. */
    public List<Bullet> bullets = new ArrayList<Bullet>();
    /** Whether a character has been selected by the player. */
    public boolean hasSelectedChar = false;
    /** Extra coins gained per kill from perks. */
    public double extraCoin = 0;


    private static final Image RIGHT_IMAGE = new Image("res/player_right.png");
    private static final Image LEFT_IMAGE = new Image("res/player_left.png");

    /**
     * Creates a player at the specified position using default speed and health from properties.
     * @param position The initial position of the player.
     */
    public Player(Point position) {
        this.position = position;
        this.currImage = RIGHT_IMAGE;
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("movingSpeed"));
        this.health = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("initialHealth"));

    }

    /**
     * Copy-constructs a player from another instance.
     * @param other The player to copy from.
     */
    public Player(Player other) {
        this.position = other.position;
        this.health = other.health;
        this.speed = other.speed;
        this.coins = other.coins;
        this.faceLeft = other.faceLeft;
        this.currImage = other.currImage;
    }

    /**
     * Updates movement, facing, bounds checking, and shooting based on current input.
     * @param input The current keyboard/mouse input.
     */
    public void update(Input input) {
        lastShot++;

        // check movement keys and mouse cursor
        double currX = position.x;
        double currY = position.y;

        if (input.isDown(Keys.A)) {
            currX -= speed;
        }
        if (input.isDown(Keys.D)) {
            currX += speed;
        }
        if (input.isDown(Keys.W)) {
            currY -= speed;
        }
        if (input.isDown(Keys.S)) {
            currY += speed;
        }

        faceLeft = input.getMouseX() < currX;

        // update the player position accordingly and ensure it can't move past the game window
        Rectangle rect = currImage.getBoundingBoxAt(new Point(currX, currY));
        Point topLeft = rect.topLeft();
        Point bottomRight = rect.bottomRight();
        if (topLeft.x >= 0 && bottomRight.x <= Window.getWidth() && topLeft.y >= 0 && bottomRight.y <=
                Window.getHeight()) {
            move(currX, currY);
        }
        if (input.isDown(MouseButtons.LEFT)){

            double shootFreq = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletFreq"));
            if (lastShot >= shootFreq){
                Point centre = getCurrImage().getBoundingBoxAt(getPosition()).centre();
                Point mouse  = input.getMousePosition();
                Vector2 dir  = new Vector2(mouse.x - centre.x, mouse.y - centre.y).normalised();
                bullets.add(new Bullet(centre, dir));
                /***
                Bullet bullet = new Bullet(position, input.directionToMouse(input.getMousePosition()));
                bullets.add(bullet);
                 **/
                lastShot = 0;
            }
        }

    }
    /**
     * Moves the player to the given coordinates.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void move(double x, double y) {
        prevPosition = position;
        position = new Point(x, y);
    }
    /**
     * Draws the player and the on-screen stats UI.
     */
    public void draw() {
        currImage = faceLeft ? LEFT_IMAGE : RIGHT_IMAGE; // NOTE: this is an example of using the ternary operator
        currImage.draw(position.x, position.y);
        UserInterface.drawStats(health, coins,keys, weaponStat);
    }
    /**
     * Adds the specified number of coins to the player's total.
     * @param coins The amount of coins to add.
     */
    public void earnCoins(double coins) {
        this.coins += coins;
    }
    /**
     * Applies damage to the player and triggers game over if health is depleted.
     * @param damage The amount of damage received.
     */
    public void receiveDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            ShadowDungeon.changeToGameOverRoom();
        }
    }

    /**
     * Returns the player's current position.
     * @return The current position.
     */
    public Point getPosition() {
        return position;
    }
    /**
     * Returns the image currently used to draw the player.
     * @return The current image.
     */
    public Image getCurrImage() {
        return currImage;
    }
    /**
     * Returns the player's previous position.
     * @return The previous position, or null if never moved.
     */
    public Point getPrevPosition() {
        return prevPosition;
    }
    /**
     * Returns the list of active bullets fired by the player.
     * @return The list of bullets.
     */
    public List<Bullet> getBullets(){
        return bullets;
    }
    /**
     * Applies coin rewards for a kill, including any perk bonus.
     * @param baseCoins The base coins earned for the kill.
     */
    public void applyPerkOnKill(double baseCoins) {
        earnCoins(baseCoins + extraCoin);
    }
    /**
     * Indicates if the player is immune to rivers (default: false).
     * @return false by default.
     */
    public boolean isImmuneToRiver() {
        return false;
    }

    /**
     * Returns whether a character has been selected.
     * @return true if a character is selected; otherwise false.
     */
    public boolean selectedChar(){
        return hasSelectedChar;
    }
    /**
     * Sets whether a character has been selected.
     * @param selected true if selected; otherwise false.
     */
    public void setSelectedChar(boolean selected){
        hasSelectedChar = selected;
    }

    /**
     * Returns the number of keys currently held.
     * @return The current key count.
     */
    public double getKeys() { return keys; }

    /**
     * Adds the specified number of keys.
     * @param n The number of keys to add.
     */
    public void addKey(int n) { keys += n; }


    /**
     * Uses one key if available.
     * @return true if a key was used; otherwise false.
     */
    public boolean useKey() {
        if (keys <= 0) return false;
        keys--;
        return true;
    }


}
