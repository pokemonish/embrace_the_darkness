package gamepad;

import base.GameMechanics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Created by fatman on 14/12/15.
 */
@WebSocket
public class GamepadWebSocket {
    private String myName;
    private GameMechanics gameMechanics;
    private Session session;

    GamepadWebSocket(String myName, GameMechanics mechanics) {
        this.myName = myName;
        this.gameMechanics = mechanics;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        System.out.println("Got message");

        if (data.isEmpty()) return;

        JsonObject parsedData = new Gson().fromJson(data, JsonObject.class);

        if (parsedData.get("type") != null &&
                parsedData.get("type").getAsString().equals("game logic")) {
            gameMechanics.processGameLogicData(myName, parsedData);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.append("Gamepad of player ").append(myName)
                .append(" disconnected. ").append(String
                .valueOf(statusCode)).append(' ').append(reason).append('\n');
    }

    @OnWebSocketConnect
    public void onConnect(Session sessionOpen) {
        this.session = sessionOpen;
    }

}
