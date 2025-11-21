import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Map;
import java.util.Properties;

/**
 * Room where the game starts
 */
public class PrepRoom {
    private Player player;
    private Door door;
    private RestartArea restartArea;
    private RobotArea robotArea;
    private MarineArea marineArea;
    private boolean stopCurrentUpdateCall = false; // this determines whether to prematurely stop the update execution

    /**
     * Initializes room entities from the given game properties.
     * @param gameProperties The properties containing object configurations for this room.
     */
    public void initEntities(Properties gameProperties) {
        // find the configuration of game objects for this room
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", ShadowDungeon.PREP_ROOM_NAME);
            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString().substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();

                switch (objectType) {
                    case "door":
                        String[] coordinates = propertyValue.split(",");
                        door = new Door(IOUtils.parseCoords(propertyValue), coordinates[2]);
                        break;
                    case "restartarea":
                        restartArea = new RestartArea(IOUtils.parseCoords(propertyValue));
                        break;
                    default:
                }
            }
        }
    }

    /**
     * Renders the prep room and handles input for character selection and door logic.
     * @param input The current keyboard and mouse input.
     */
    public void update(Input input) {
        UserInterface.drawStartMessages();

        robotArea = new RobotArea(IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Robot")));
        marineArea = new MarineArea(IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Marine")));
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));
        UserInterface.drawData(ShadowDungeon.getMessageProps().getProperty("robotDescription"),fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("robotMessage") ));
        UserInterface.drawData(ShadowDungeon.getMessageProps().getProperty("marineDescription"),fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("marineMessage") ));


        if (input.wasPressed(Keys.R)) {
            ShadowDungeon.setPlayer(new Robot(ShadowDungeon.getPlayer().getPosition()));
            ShadowDungeon.getPlayer().setSelectedChar(true);
        }
        if (input.wasPressed(Keys.M)) {
            ShadowDungeon.setPlayer(new Marine(ShadowDungeon.getPlayer().getPosition()));
            ShadowDungeon.getPlayer().setSelectedChar(true);
        }

        this.player = ShadowDungeon.getPlayer();


        // update and draw all game objects in this room
        door.update(player);
        door.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }


        restartArea.update(input, player);
        restartArea.draw();

        if (robotArea != null) { robotArea.draw(); }
        if (marineArea != null) { marineArea.draw(); }


        if (player != null) {
            player.update(input);
            player.draw();
        }

        if (player.hasSelectedChar && player.getBullets() != null) {
            for (Bullet bullet : player.getBullets()) {
                bullet.setActive(true);
                bullet.update();
                bullet.draw();
            }
        }

        // door unlock mechanism
        if ((input.wasPressed(Keys.R) && !findDoor().isUnlocked()) || player.hasSelectedChar) {
            findDoor().unlock(false);
        }
    }

    private boolean stopUpdatingEarlyIfNeeded() {
        if (stopCurrentUpdateCall) {
            player = null;
            stopCurrentUpdateCall = false;
            return true;
        }
        return false;
    }

    /**
     * Sets the current player reference for this room.
     * @param player The player to associate with this room.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Flags the room to stop the current update call early.
     */
    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }
    /**
     * Finds the door in this room.
     * @return The door for this room.
     */
    public Door findDoor() {
        return door;
    }
    /**
     * Finds the door by its destination (same as {@link #findDoor()} here).
     * @return The door for this room.
     */
    public Door findDoorByDestination() {
        return door;
    }
}
