import bagel.Image;
import bagel.util.Point;
/**
 * Represents the area where the Marine character can be selected or displayed.
 */
public class MarineArea {
    private final Point position;
    private final Image image;

    /**
     * Creates a MarineArea at the specified position.
     * @param position The position of the Marine area.
     */
    public MarineArea(Point position) {
        this.position = position;
        this.image = new Image("res/marine_sprite.png");
    }
    /**
     * Draws the Marine area image on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }


}
