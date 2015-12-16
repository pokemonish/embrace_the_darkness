package base;

import com.google.gson.JsonObject;
import game_socket_mechanics.GameWebSocket;

/**
 * @author v.chibrikov
 */
public interface WebSocketService {

    void addUser(GameWebSocket user);

    void notifyMyNewScore(GameUser user);

    void notifyStartGame(GameUser user);

    void notifyGameOver(GameUser user, boolean win);

    void notifyEnemyAction(GameUser user, JsonObject data);
}
