package gamesocketmech;

import base.GameMechanics;
import base.WebSocketService;
import accountservice.AccountService;
import frontendservice.FrontEnd;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * This class represents a servlet starting a webSocket application
 */
@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/gameplay"})
public class WebSocketGameServlet extends WebSocketServlet {
    private static final int IDLE_TIME = 6000 * 1000;
    private final FrontEnd frontEnd;
    private final GameMechanics gameMechanics;
    private final WebSocketService webSocketService;

    public WebSocketGameServlet(FrontEnd frontEnd,
                                GameMechanics gameMechanics,
                                WebSocketService webSocketService) {
        this.frontEnd = frontEnd;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(frontEnd, gameMechanics, webSocketService));
    }
}
