package gamepad;

import base.GameMechanics;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Created by fatman on 14/12/15.
 */
public class GamepadSocketServlet extends WebSocketServlet {
    private static final int IDLE_TIME = 6000 * 1000;
    private GameMechanics gameMechanics;

    public GamepadSocketServlet(GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GamepadWebSocketCreator(gameMechanics));
    }
}
