package base;

import org.json.simple.JSONObject;

/**
 * @author v.chibrikov
 */
@SuppressWarnings("unused")
public interface GameMechanics {

    void addUser(String user);

    void incrementScore(String userName);

    void run();

    void sendOtherPlayers(String player, JSONObject data);

    void deleteIfWaiter(String player);

    void processGameLogicData(String playerName, JSONObject data);
}
