import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Door which can be locked or unlocked, allows the player to move to the room it's connected to
 */
public class Door implements Obstacle {
    private final Point position;
    private Image image;
    public final String toRoomName;
    public BattleRoom battleRoom; // only set if this door is inside a Battle Room
    private boolean unlocked = false;
    private boolean justEntered = false; // when the player only just entered this door's room
    private boolean shouldLockAgain = false;

    private static final Image LOCKED = new Image("res/locked_door.png");
    private static final Image UNLOCKED = new Image("res/unlocked_door.png");

    /**
     * Creates a new door connected to another room.
     * @param position The position of the door.
     * @param toRoomName The name of the room this door leads to.
     */
    public Door(Point position, String toRoomName) {
        this.position = position;
        this.image = LOCKED;
        this.toRoomName = toRoomName;
    }
    /**
     * Creates a new door within a battle room.
     * @param position The position of the door.
     * @param toRoomName The name of the room this door leads to.
     * @param battleRoom The battle room containing this door.
     */
    public Door(Point position, String toRoomName, BattleRoom battleRoom) {
        this.position = position;
        this.image = LOCKED;
        this.toRoomName = toRoomName;
        this.battleRoom = battleRoom;
    }

    /**
     * Updates the door’s interaction with the player.
     * @param player The player to check for collision and transition.
     */
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            onCollideWith(player);
        } else {
            onNoLongerCollide();
        }
    }
    /**
     * Draws the door on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * Unlocks the door and updates its image.
     * @param justEntered True if the player has just entered this door's room.
     */
    public void unlock(boolean justEntered) {
        unlocked = true;
        image = UNLOCKED;
        this.justEntered = justEntered;
    }
    /**
     * Checks if the door collides with the player.
     * @param player The player to check collision with.
     * @return True if the player collides with the door, false otherwise.
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    private void onCollideWith(Player player) {
        // when the player only just entered this door's room, overlapping with the unlocked door shouldn't trigger room transition
        if (unlocked && !justEntered) {
            ShadowDungeon.changeRoom(toRoomName);
        }
        if (!unlocked) {
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }

    private void onNoLongerCollide() {
        // when the player only just moved away from the unlocked door after walking through it
        if (unlocked && justEntered) {
            justEntered = false;

            // Battle Room activation conditions
            if (shouldLockAgain && battleRoom != null && !battleRoom.isComplete()) {
                unlocked = false;
                image = LOCKED;
                battleRoom.activateEnemies();
            }
        }
    }
    /**
     * Locks the door and updates its image.
     */
    public void lock() {
        unlocked = false;
        image = LOCKED;
    }
    /**
     * Checks if the door is currently unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isUnlocked() {
        return unlocked;
    }
    /**
     * Marks that this door should lock again after the player enters.
     */
    public void setShouldLockAgain() {
        this.shouldLockAgain = true;
    }

    /**
     * Gets the door’s position.
     * @return The position of the door.
     */
    public Point getPosition() {
        return position;
    }
    /**
     * Gets the door’s bounding box for collision detection.
     * @return The bounding box of this door.
     */
    @Override
    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(position);
    }

}
