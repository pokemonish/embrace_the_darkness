package gamesocketmech;

import accountservice.UserProfile;
import base.GameMechanics;
import base.WebSocketService;
import frontendservice.FrontEnd;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.Nullable;

/**
 * @author v.chibrikov
 */
public class GameWebSocketCreator implements WebSocketCreator {
    private final FrontEnd frontEnd;
    private final GameMechanics gameMechanics;
    private final WebSocketService webSocketService;

    public GameWebSocketCreator(FrontEnd frontEnd,
                                GameMechanics gameMechanics,
                                WebSocketService webSocketService) {
        this.frontEnd = frontEnd;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Nullable
    @Override
    public GameWebSocket createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String userId = String.valueOf(req.getHttpServletRequest().getSession().getAttribute("userId"));
        UserProfile profile = this.frontEnd.isAuthenticated(userId);

        if (profile == null) {
            System.out.println("Unauthorized user tryed to establish connection to " +
                    "websocket with " + req.getHttpServletRequest().getHeader("User-Agent"));
            return null;
        }

        String name = profile.getLogin();

        return new GameWebSocket(name, gameMechanics, webSocketService);
    }
}
