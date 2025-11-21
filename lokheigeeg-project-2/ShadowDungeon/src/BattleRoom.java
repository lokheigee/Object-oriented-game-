import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Room with doors that are locked until the plaer defeats all enemies
 */
public class BattleRoom {
    private Player player;
    private Door primaryDoor;
    private Door secondaryDoor;
    private Basket basket;
    private Table table;
    private KeyBulletKin keyBulletKin;
    private ArrayList<BulletKin> bulletKins;
    private ArrayList<AshenBulletKin> ashenBulletKins;
    private ArrayList<Fireball>  fireballs;
    private ArrayList<TreasureBox> treasureBoxes;
    private ArrayList<Key> keys;
    private ArrayList<Wall> walls;
    private ArrayList<River> rivers;
    private List<Obstacle> obstacles;
    private boolean stopCurrentUpdateCall = false; // this determines whether to prematurely stop the update execution
    private boolean isComplete = false;
    private final String nextRoomName;
    private final String roomName;
    private int frame = 0;
    private int lastShotFrame =-1;
    private boolean keyDropped = false;
    private boolean kbkEverActivated = false;

    /**
     * Creates a battle room with the given name and next-room target.
     * @param roomName The name of this room.
     * @param nextRoomName The name of the room to enter via doors.
     */
    public BattleRoom(String roomName, String nextRoomName) {
        walls = new ArrayList<>();
        rivers = new ArrayList<>();
        treasureBoxes = new ArrayList<>();
        keys = new ArrayList<>();
        bulletKins = new ArrayList<>();
        ashenBulletKins = new ArrayList<>();
        fireballs = new ArrayList<>();
        obstacles = new ArrayList<>();
        this.roomName = roomName;
        this.nextRoomName = nextRoomName;
    }
    /**
     * Initializes entities for this room from game properties.
     * @param gameProperties The game configuration properties.
     */
    public void initEntities(Properties gameProperties) {
        // find the configuration of game objects for this room
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", roomName);

            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString()
                        .substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();

                // ignore if the value is 0
                if (propertyValue.equals("0")) {
                    continue;
                }

                String[] coordinates;
                for (String coords: propertyValue.split(";")) {
                    switch (objectType) {
                        case "table":
                            table = new Table(IOUtils.parseCoords(propertyValue));
                            break;
                        case "basket":
                            basket = new Basket(IOUtils.parseCoords(propertyValue));
                            break;

                        case "ashenBulletKin":
                            AshenBulletKin ashenBulletKin = new AshenBulletKin((IOUtils.parseCoords(coords)));
                            ashenBulletKins.add(ashenBulletKin);
                            break;
                        case "bulletKin":
                            BulletKin bulletKin = new BulletKin(IOUtils.parseCoords(coords));
                            bulletKins.add(bulletKin);
                            break;
                        case "primarydoor":
                            coordinates = propertyValue.split(",");
                            primaryDoor = new Door(IOUtils.parseCoords(propertyValue), coordinates[2], this);
                            break;
                        case "secondarydoor":
                            coordinates = propertyValue.split(",");
                            secondaryDoor = new Door(IOUtils.parseCoords(propertyValue), coordinates[2], this);
                            break;
                        case "keyBulletKin":
                            String start = propertyValue.split(";")[0];
                            keyBulletKin = new KeyBulletKin(IOUtils.parseCoords(start),roomName);
                            break;
                        case "wall":
                            Wall wall = new Wall(IOUtils.parseCoords(coords));
                            walls.add(wall);
                            break;
                        case "treasurebox":
                            TreasureBox treasureBox = new TreasureBox(IOUtils.parseCoords(coords),
                                    Double.parseDouble(coords.split(",")[2]));
                            treasureBoxes.add(treasureBox);
                            break;
                        case "river":
                            River river = new River(IOUtils.parseCoords(coords));
                            rivers.add(river);
                            break;
                        default:
                    }
                }
            }
        }
    }

    /**
     * Updates and renders all active objects in the room for this frame.
     * @param input The current input state.
     */
    public void update(Input input) {
        frame++;
        // update and draw all active game objects in this room
        primaryDoor.update(player);
        primaryDoor.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }

        secondaryDoor.update(player);
        secondaryDoor.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }

        basket.update(player);
        if(!basket.isDestoryed()){
            basket.draw();
        }

        table.update(player);
        if(!table.isDestoryed()){
            table.draw();
        }

        if (keyBulletKin.isActive()) {
            kbkEverActivated = true;
            keyBulletKin.update(player);
            keyBulletKin.draw();
        }

        if(!keyDropped && kbkEverActivated && !keyBulletKin.isActive()){
            keys.add(new Key(keyBulletKin.getPosition()));
            keyDropped = true;
        }
        for (int i = keys.size() - 1; i >= 0; i--){
            Key k = keys.get(i);
            k.update(player);
            if (!k.isActive()) keys.remove(i);
            else k.draw();
        }



        for (BulletKin bulletKin: bulletKins) {
            if(bulletKin.isActive()) {
                bulletKin.update(player);
                if (bulletKin.canShoot(frame, bulletKin.getShootFrequency())){
                    Point startPos = bulletKin.getPosition();
                    Fireball fireball = new Fireball(startPos);
                    fireballs.add(fireball);
                }
                bulletKin.draw();
            }
        }
        for (AshenBulletKin ashenBulletKin: ashenBulletKins) {
            if(ashenBulletKin.isActive()) {
                ashenBulletKin.update(player);
                if (ashenBulletKin.canShoot(frame, ashenBulletKin.getShootFrequency())){
                    Point startPos = ashenBulletKin.getPosition();
                    Fireball fireball = new Fireball(startPos);
                    fireballs.add(fireball);
                }
                ashenBulletKin.draw();
            }
        }

        obstacles.clear();
        obstacles.addAll(walls);
        obstacles.add(primaryDoor);
        obstacles.add(secondaryDoor);

        for (int i = fireballs.size() - 1; i >= 0; i--) {
            Fireball fireball = fireballs.get(i);
            if (!fireball.isActive()) {
                fireballs.remove(i);      // remove dead one, continue loop
                continue;
            }
            fireball.update(player, obstacles);
            fireball.draw();
        }

        for (int i = player.getBullets().size() - 1; i >= 0; i--) {
            Bullet bullet = player.getBullets().get(i);
            if (!bullet.isActive()) {
                player.getBullets().remove(i);
                continue;
            }
            bullet.update(obstacles);
            for (BulletKin bk : bulletKins) {
                if (bk.isActive() && bullet.getBoundingBox().intersects(bk.getBoundingBox())) {
                    bk.receiveDamage(bullet.getDamage());
                    bullet.setActive(false);
                    if(bk.isDead()){
                        player.applyPerkOnKill(bk.getCoinValue());
                    }
                    break;
                }
            }
            for (AshenBulletKin abk : ashenBulletKins) {
                if (abk.isActive() && bullet.getBoundingBox().intersects(abk.getBoundingBox())) {
                    abk.receiveDamage(bullet.getDamage());
                    bullet.setActive(false);
                    if(abk.isDead()){
                        player.applyPerkOnKill(abk.getCoinValue());
                    }
                    break;
                }
            }
            if (keyBulletKin.isActive() && bullet.getBoundingBox().intersects(keyBulletKin.getBoundingBox())) {
                keyBulletKin.receiveDamage(bullet.getDamage());
                bullet.setActive(false);
                break;
            }
            if (!basket.isDestoryed() && bullet.getBoundingBox().intersects(basket.getBoundingBox())) {
                player.earnCoins(basket.getCoins());
                basket.setIsDestoryed(true);
            }
            if (!table.isDestoryed() && bullet.getBoundingBox().intersects(table.getBoundingBox())) {
                table.setIsDestoryed(true);
            }
            bullet.draw();
        }


        for (Wall wall: walls) {
            wall.update(player);
            wall.draw();
        }

        for (River river: rivers) {
            river.update(player);
            river.draw();
        }

        for (TreasureBox treasureBox: treasureBoxes) {
            if (treasureBox.isActive()) {
                treasureBox.update(input, player);
                treasureBox.draw();
            }
        }

        if (player != null) {
            player.update(input);
            player.draw();
        }

        if (noMoreEnemies() && !isComplete()) {
            setComplete(true);
            unlockAllDoors();
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
     * Requests this room to stop updating after the current call.
     */
    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }
    /**
     * Sets the player instance for this room.
     * @param player The player to control and render.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
    /**
     * Finds the door that leads to the specified destination room.
     * @param roomName The destination room name.
     * @return The matching door to that room.
     */
    public Door findDoorByDestination(String roomName) {
        if (primaryDoor.toRoomName.equals(roomName)) {
            return primaryDoor;
        } else {
            return secondaryDoor;
        }
    }

    private void unlockAllDoors() {
        primaryDoor.unlock(false);
        secondaryDoor.unlock(false);
    }

    /**
     * Indicates whether this room is complete.
     * @return True if all enemies are defeated, otherwise false.
     */
    public boolean isComplete() {
        return isComplete;
    }
    /**
     * Sets whether this room is complete.
     * @param complete True if complete, otherwise false.
     */
    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    /**
     * Activates all enemies and projectiles in this room.
     */
    public void activateEnemies() {
        keyBulletKin.setActive(true);
        for (BulletKin bulletKin: bulletKins) {
            bulletKin.setActive(true);
        }
        for(AshenBulletKin ashenBulletKin: ashenBulletKins) {
            ashenBulletKin.setActive(true);
        }
        for (Fireball fireball: fireballs) {
            fireball.setActive(true);
        }
    }

    /**
     * Checks if there are no active enemies left.
     * @return True if all enemies are dead, otherwise false.
     */
    public boolean noMoreEnemies() {
        return keyBulletKin.isDead() && noMoreBulletKins() && noMoreAshenBulletKins();
    }
    /**
     * Checks if all BulletKins are dead.
     * @return True if no BulletKin remains active, otherwise false.
     */
    public boolean noMoreBulletKins(){
        for (BulletKin bulletKin: bulletKins) {
            if (!bulletKin.isDead()){
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if all AshenBulletKins are dead.
     * @return True if no AshenBulletKin remains active, otherwise false.
     */
    public boolean noMoreAshenBulletKins(){
        for (AshenBulletKin ashenBulletKin: ashenBulletKins) {
            if (!ashenBulletKin.isDead()){
                return false;
            }
        }
        return true;
    }
}
