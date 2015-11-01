package base;

import org.json.simple.JSONObject;

/**
 * @author v.chibrikov
 */
public interface GameMechanics {

    public void addUser(String user);

    public void incrementScore(String userName);

    public void run();

    public void sendOtherPlayers(String player, JSONObject data);

    public void deleteIfWaiter(String player);

    public void processGameLogicData(String playerName, JSONObject data);
}
