import bagel.Image;
import bagel.util.Point;
/**
 * Represents the Robot player character who earns extra coins per enemy kill.
 */
public class Robot extends Player{

    private static final Image RIGHT_IMAGE = new Image("res/robot_right.png");
    private static final Image LEFT_IMAGE = new Image("res/robot_left.png");
    private double extraCoin = Double.parseDouble(ShadowDungeon.getGameProps().getProperty
            ("robotExtraCoin"));

    /**
     * Creates a new Robot at the specified position.
     * @param position The starting position of the Robot.
     */
    public Robot(Point position){
        super(position);
        this.currImage = RIGHT_IMAGE;
    }
    /**
     * Applies the Robot’s perk when defeating an enemy by adding bonus coins.
     * @param baseCoins The base amount of coins gained from the kill.
     */
    @Override
    public void applyPerkOnKill(double baseCoins) {
        earnCoins(baseCoins + extraCoin);

    }

    /**
     * Draws the Robot character on the screen based on its facing direction and stats.
     */
    @Override
    public void draw() {
        currImage = faceLeft ? LEFT_IMAGE : RIGHT_IMAGE; // NOTE: this is an example of using the ternary operator
        currImage.draw(position.x, position.y);
        UserInterface.drawStats(health, coins,keys, weaponStat);
    }


}
