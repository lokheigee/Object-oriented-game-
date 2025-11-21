import bagel.util.Point;
import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * Represents a BulletKin enemy that damages the player on contact and can shoot at intervals.
 */
public class BulletKin extends Enemy {

    private double coinValue;
    private double shootFrequency;
    private int lastShotFrame = -1;
    private final double CONTACT = 0.2;

    /**
     * Creates a BulletKin at the given starting position.
     * @param startPos The starting position of this enemy.
     */
    public BulletKin(Point startPos) {
        super(startPos);
        this.image = new Image("res/bullet_kin.png");
        this.health = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinHealth"));
        this.coinValue = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinCoin"));
        this.shootFrequency = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinShootFrequency"));
    }

    /**
     * Updates the BulletKin’s state and checks for collision with the player.
     * @param player The player in the current room.
     */
    @Override
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            player.receiveDamage(CONTACT);

        }
    }

    /**
     * Checks if the BulletKin can shoot based on the current frame.
     * @param currentFrame The current frame number of the game.
     * @param shootFrequency The interval between shots.
     * @return True if the BulletKin can shoot, false otherwise.
     */
    public boolean canShoot(int currentFrame, double shootFrequency ){
        if (lastShotFrame == -1) {
            lastShotFrame = currentFrame;
            return true;
        }
        // otherwise, shoot again only when enough frames have passed
        if (currentFrame - lastShotFrame >= shootFrequency) {
            lastShotFrame = currentFrame;
            return true;
        }
        return false;
    }
    /**
     * Gets the shooting frequency of this enemy.
     * @return The shoot frequency value.
     */
    public double getShootFrequency(){
        return this.shootFrequency;
    }
    /**
     * Gets the coin value dropped when this enemy is defeated.
     * @return The coin value.
     */
    public double getCoinValue(){
        return this.coinValue;
    }
}



