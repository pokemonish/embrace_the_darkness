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
import java.util.Map;

@WebSocket
public class GameWebSocket {
    private String myName;
    private Session session;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

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
            for (int i = 0; i < enemies.length; ++i) {
                JSONObject enemy = new JSONObject();
                enemy.put("name", enemies[i]);
                JSONenemies.add(enemy.toJSONString());
            }
            jsonStart.put("enemyNames", JSONenemies);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void gameOver(GameUser user, boolean win) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            jsonStart.put("win", win);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        System.out.append("Got message\n");

        if (data.isEmpty()) return;

        JSONObject parsedData = (JSONObject) JSONValue.parse(data);

        if (parsedData.get("type") != null && parsedData.get("type").toString().equals("game logic")) {
            gameMechanics.processGameLogicData(myName, parsedData);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        System.out.append("On open started\n");
        setSession(session);
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
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void setEnemyScore(GameUser user) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "increment");
        String[] enemies = user.getEnemyNames();
        JSONObject[] JSONenemies = new JSONObject[enemies.length];
        for (int i = 0; i < enemies.length; ++i) {
            JSONObject enemy = new JSONObject();
            enemy.put("name", enemies[i]);
            JSONenemies[i] = enemy;
        }
        jsonStart.put("name", JSONenemies);

        jsonStart.put("score", user.getEnemyScore());
        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void sendEnemyAction(GameUser user, JSONObject data) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "action");
        jsonStart.put("activePlayer", data.get("activePlayer"));
        jsonStart.put("action", data.get("action"));
        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print(e.toString());
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.append("On close " + statusCode + ' ' + reason + '\n');
        gameMechanics.deleteIfWaiter(myName);
    }
}
