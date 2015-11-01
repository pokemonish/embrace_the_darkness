package base;

import frontend.GameWebSocket;
import org.json.simple.JSONObject;

/**
 * @author v.chibrikov
 */
public interface WebSocketService {

    void addUser(GameWebSocket user);

    void notifyMyNewScore(GameUser user);

    void notifyEnemyNewScore(GameUser user);

    void notifyStartGame(GameUser user);

    void notifyGameOver(GameUser user, boolean win);

    void notifyEnemyAction(GameUser user, JSONObject data);
}
