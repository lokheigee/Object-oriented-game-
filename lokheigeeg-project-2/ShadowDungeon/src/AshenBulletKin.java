import bagel.Image;
import bagel.util.Point;

/**
 * Represents an enemy that damages the player on contact and can shoot at fixed intervals.
 */
public class AshenBulletKin extends Enemy{
    private double coinValue;
    private double shootFrequency;
    private int lastShotFrame = -1 ;
    private final double CONTACT = 0.2;

    /**
     * Creates a new AshenBulletKin at the specified starting position.
     * @param startPos The starting position of this enemy.
     */
    public AshenBulletKin(Point startPos) {
        super(startPos);
        image = new Image("res/ashen_bullet_kin.png");
        this.health = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("ashenBulletKinHealth"));
        this.coinValue = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("ashenBulletKinCoin"));
        this.shootFrequency = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("ashenBulletKinShootFrequency"));
    }

    /**
     * Updates the enemy’s state and checks for collision with the player.
     * @param player The player in the current room.
     */
    @Override
    public void update(Player player){
        if (hasCollidedWith(player)) {
            player.receiveDamage(CONTACT);
        }
    }

    /**
     * Checks if the enemy can shoot based on the current frame.
     * @param currentFrame The current game frame.
     * @param shootFrequency The interval between shots.
     * @return True if the enemy can shoot, false otherwise.
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
     * Gets the coin value dropped when the enemy is defeated.
     * @return The coin value.
     */
    public double getCoinValue(){
        return this.coinValue;
    }
}


