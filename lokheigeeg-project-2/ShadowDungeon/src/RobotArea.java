import bagel.Image;
import bagel.util.Point;

/**
 * Represents the area where the Robot character can be selected or displayed.
 */
public class RobotArea {
    private final Point position;
    private final Image image;
    /**
     * Creates a RobotArea at the specified position.
     * @param position The position of the Robot area.
     */
    public RobotArea(Point position) {
        this.position = position;
        this.image = new Image("res/robot_sprite.png");
    }

    /**
     * Draws the Robot area image on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }


}
