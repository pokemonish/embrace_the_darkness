package frontend;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;

@SuppressWarnings("unchecked")
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
        System.out.append("WebSocket created\n");
    }

    public String getMyName() {
        return myName;
    }

    public void startGame(GameUser user) {
        System.out.append("StartGame started\n");
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "start");
            String[] enemies = user.getEnemyNames();
            JSONArray JSONenemies = new JSONArray();
            for (String nextEnemy : enemies) {
                JSONObject enemy = new JSONObject();
                enemy.put("name", nextEnemy);
                JSONenemies.add(enemy);
            }
            jsonStart.put("enemyNames", JSONenemies);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (IOException e) {
            System.out.print(e.toString());
        }
    }

    public void gameOver(GameUser user, boolean win) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            jsonStart.put("winner", user.getMyName());
            jsonStart.put("win", win);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (IOException e) {
            System.out.print(e.toString());
        }
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        System.out.append("Got message\n");

        if (data.isEmpty()) return;

        JSONObject parsedData = (JSONObject) JSONValue.parse(data);

        if (parsedData.get("type") != null &&
                parsedData.get("type").toString().equals("game logic")) {
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
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "increment");
        jsonStart.put("name", myName);
        jsonStart.put("score", user.getMyScore());
        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (IOException e) {
            System.out.print(e.toString());
        }
    }

    public void sendEnemyAction(JSONObject data) {
        if (isValidFieldInData(data, "activePlayer") &&
                isValidFieldInData(data, "action")) {
            data.put("status", "action");

            try {
                session.getRemote().sendString(data.toJSONString());
            } catch (IOException e) {
                System.out.print(e.toString());
            }
        }
    }

    private boolean isValidFieldInData(JSONObject data, String fieldName) {
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
