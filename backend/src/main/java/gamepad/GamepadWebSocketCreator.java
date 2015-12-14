package gamepad;

import base.GameMechanics;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fatman on 14/12/15.
 */
public class GamepadWebSocketCreator implements WebSocketCreator {
    private GameMechanics gameMechanics;

    public GamepadWebSocketCreator(GameMechanics mechanics) {
        this.gameMechanics = mechanics;
    }

    @Nullable
    @Override
    public GamepadWebSocket createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String key = req.getHttpServletRequest().getParameter("key");

        if (!GamepadServlet.getAvailableKeys().containsKey(key)) return null;

        return new GamepadWebSocket(GamepadServlet.getAvailableKeys().get(key), gameMechanics);
    }
}
