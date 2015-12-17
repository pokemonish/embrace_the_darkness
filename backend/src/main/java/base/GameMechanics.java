package base;

import com.google.gson.JsonObject;

/**
 * @author v.chibrikov
 */

public interface GameMechanics {

    void addUser(String user);

    @SuppressWarnings("unused")
    void incrementScore(String userName);

    void run();

    void sendOtherPlayers(String player, JsonObject data);

    void sendEverybody(String playerName, JsonObject data);

    void deleteIfWaiter(String player);

    void processGameLogicData(String playerName, JsonObject data);

    void processGameLogicData(String playerName, JsonObject data, boolean isFromJoystick);

    void setIsActive(boolean value);

    boolean isActive();

    void killPlayer(String playerName);
}
