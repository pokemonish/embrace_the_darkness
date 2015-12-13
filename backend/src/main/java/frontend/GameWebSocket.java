package frontend;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

@WebSocket
public class GameWebSocket {
    private final String myName;
    private Session session;
    private final GameMechanics gameMechanics;
    private final WebSocketService webSocketService;

    public GameWebSocket(String myName, GameMechanics gameMechanics,
                         WebSocketService webSocketService) {
        this.myName = myName;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
        System.out.println("WebSocket created");
    }

    public String getMyName() {
        return myName;
    }

    public void startGame(GameUser user) {
        System.out.append("StartGame started\n");
        try {
            JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("status", "start");
            String[] enemies = user.getEnemyNames();
            JsonArray JSONenemies = new JsonArray();
            for (String nextEnemy : enemies) {
                JsonObject enemy = new JsonObject();
                enemy.addProperty("name", nextEnemy);
                JSONenemies.add(enemy);
            }
            jsonStart.add("enemyNames", JSONenemies);
            session.getRemote().sendString(jsonStart.toString());
        } catch (IOException | WebSocketException e) {
            System.out.print(e.toString());
        }
    }

    public void gameOver(GameUser user, boolean win) {
        try {
            JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("status", "Game Over!!!");
            jsonStart.addProperty("winner", user.getMyName());
            jsonStart.addProperty("win", win);
            session.getRemote().sendString(jsonStart.toString());
        } catch (IOException | WebSocketException e) {
            System.out.print(e.toString());
        }
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

    @OnWebSocketConnect
    public void onOpen(Session sessionOpen) {
        System.out.append("On open started\n");
        this.session = sessionOpen;
        webSocketService.addUser(this);
        gameMechanics.addUser(myName);
        System.out.append("On open finised\n");
    }

    public void setMyScore(GameUser user) {
        JsonObject jsonStart = new JsonObject();
        jsonStart.addProperty("status", "increment");
        jsonStart.addProperty("name", myName);
        jsonStart.addProperty("score", user.getMyScore());
        try {
            session.getRemote().sendString(jsonStart.toString());
        } catch (IOException | WebSocketException e) {
            System.out.print(e.toString());
        }
    }

    public void sendEnemyAction(JsonObject data) {
        if (isValidFieldInData(data, "activePlayer") &&
                isValidFieldInData(data, "action")) {
            data.addProperty("status", "action");

            try {
                session.getRemote().sendString(data.toString());
            } catch (IOException | WebSocketException e) {
                System.out.println("Connection with player " + myName + " lost.");
                System.out.println(e.toString());
            }
        }
    }

    private boolean isValidFieldInData(JsonObject data, String fieldName) {
        return data.get(fieldName) != null &&
            !data.get(fieldName).toString().isEmpty();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.append("On close ").append(String
                .valueOf(statusCode)).append(' ').append(reason).append('\n');
        gameMechanics.deleteIfWaiter(myName);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        if (obj.getClass() != getClass()) {
            return false;
        }

        GameWebSocket gameWebSocket = (GameWebSocket)obj;
        System.out.append(myName).append(' ').append(gameWebSocket.myName);
        return (gameWebSocket.myName.equals(myName) &&
                gameWebSocket.gameMechanics.equals(gameMechanics) &&
                gameWebSocket.webSocketService.equals(webSocketService));
    }

    @Override
    public int hashCode() {
        int result = myName != null ? myName.hashCode() : 0;
        result = 31 * result + (session != null ? session.hashCode() : 0);
        result = 31 * result + (gameMechanics != null ? gameMechanics.hashCode() : 0);
        result = 31 * result + (webSocketService != null ? webSocketService.hashCode() : 0);
        return result;
    }
}
