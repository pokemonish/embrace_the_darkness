package game_socket_mechanics;

import base.GameUser;
import base.WebSocketService;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class WebSocketServiceImpl implements WebSocketService {
    private final Map<String, GameWebSocket> userSockets = new HashMap<>();

    @Override
    public void addUser(GameWebSocket user) {
        userSockets.put(user.getMyName(), user);
    }

    @Override
    public void notifyMyNewScore(GameUser user) {
        userSockets.get(user.getMyName()).setMyScore(user);
    }

    @Override
    public void notifyStartGame(GameUser user) {
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.startGame(user);
    }

    @Override
    public void notifyEnemyAction(GameUser user, JsonObject data) {
        userSockets.get(user.getMyName()).sendEnemyAction(data);
    }

    @Override
    public void notifyGameOver(GameUser user, boolean win) {
        userSockets.get(user.getMyName()).gameOver(user, win);
    }

    public void notifyMyAction(GameUser user, JsonObject data) {
        userSockets.get(user.getMyName()).sendMyAction(data);
    }
}
