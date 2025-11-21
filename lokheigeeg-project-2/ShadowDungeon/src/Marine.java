import bagel.Image;
import bagel.util.Point;

/**
 * Represents the Marine player character who is immune to rivers and has unique appearance when facing left or right.
 */
public class Marine extends Player{
    private static final Image RIGHT_IMAGE = new Image("res/marine_right.png");
    private static final Image LEFT_IMAGE = new Image("res/marine_left.png");

    /**
     * Creates a new Marine at the specified position.
     * @param position The starting position of the Marine.
     */
    public Marine(Point position){
        super(position);
        this.currImage = RIGHT_IMAGE;
    }

    /**
     * Checks whether the Marine is immune to river terrain.
     * @return true since the Marine is immune to rivers.
     */
    @Override
    public boolean isImmuneToRiver() {
        return true;
    }
    /**
     * Draws the Marine on the screen based on its facing direction and updates player stats display.
     */
    @Override
    public void draw() {
        currImage = faceLeft ? LEFT_IMAGE : RIGHT_IMAGE; // NOTE: this is an example of using the ternary operator
        currImage.draw(position.x, position.y);
        UserInterface.drawStats(health, coins, keys, weaponStat);
    }
}
