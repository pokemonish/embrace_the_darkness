package frontend;

import base.AuthService;
import base.GameMechanics;
import base.WebSocketService;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * @author v.chibrikov
 */
public class GameWebSocketCreator implements WebSocketCreator {
    private final AuthService authService;
    private final GameMechanics gameMechanics;
    private final WebSocketService webSocketService;

    public GameWebSocketCreator(AuthService authService,
                                GameMechanics gameMechanics,
                                WebSocketService webSocketService) {
        this.authService = authService;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public GameWebSocket createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        String name = this.authService.getUserName(sessionId);
        return new GameWebSocket(name, gameMechanics, webSocketService);
    }
}
