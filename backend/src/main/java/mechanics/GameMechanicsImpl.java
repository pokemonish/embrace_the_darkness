package mechanics;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import org.json.simple.JSONObject;
import utils.TimeHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author v.chibrikov
 */
public class GameMechanicsImpl implements GameMechanics {
    private static final int PLAYERS_NUMBER = 3;

    private static final int STEP_TIME = 100;

    private static final int GAME_TIME = 15 * 1000;

    private WebSocketService webSocketService;

    private Map<String, GameSession> nameToGame = new HashMap<>();

    private Set<GameSession> allSessions = new HashSet<>();

    private String[] waiters = new String[PLAYERS_NUMBER];

    public GameMechanicsImpl(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void addUser(String user) {

        System.out.print(countWaiters());

        int waitersNumber = countWaiters();

        System.out.print(PLAYERS_NUMBER - 1);
        System.out.print(waitersNumber == PLAYERS_NUMBER - 1);
        System.out.print(" User is " + user + ' ');

        if (waitersNumber == PLAYERS_NUMBER - 1) {
            waiters[waitersNumber] = user;

            startGame();

            waiters = new String[PLAYERS_NUMBER];
        } else {
            waiters[waitersNumber] = user;
        }
    }

    private int countWaiters() {
        int waitersNumber = 0;
        for (int i = 0; i < waiters.length; ++i) {
            if (waiters[i] != null) {
                ++waitersNumber;
            }
        }
        return waitersNumber;
    }

    @Override
    public void deleteIfWaiter(String user) {
        for (int i = 0; i < waiters.length; ++i) {
            if (waiters[i] != null && waiters[i].equals(user)) {
                waiters[i] = null;
            }
        }
    }

    @Override
    public void incrementScore(String userName) {
        GameSession myGameSession = nameToGame.get(userName);
        GameUser myUser = myGameSession.getSelf(userName);
        myUser.incrementMyScore();
        GameUser enemyUser = myGameSession.getEnemy(userName);
        enemyUser.incrementEnemyScore();
        webSocketService.notifyMyNewScore(myUser);
        webSocketService.notifyEnemyNewScore(enemyUser);
    }

    @Override
    public void run() {
        while (true) {
            gmStep();
            TimeHelper.sleep(STEP_TIME);
        }
    }

    private void gmStep() {
        for (GameSession session : allSessions) {
            if (session.getSessionTime() > GAME_TIME) {

                GameUser winner = session.determineWinner();
                Map<String, GameUser> users = session.getUsers();

                for(Map.Entry<String, GameUser> entry : users.entrySet()) {
                    GameUser user = entry.getValue();
                    String name = entry.getKey();

                    boolean isWinner = winner.getMyName().equals(name);

                    webSocketService.notifyGameOver(user, isWinner);
                }
            }
        }
    }

    @Override
    public void processGameLogicData(String playerName, JSONObject data) {

        String action = data.get("data").toString();
        System.out.print(data.toJSONString());
        System.out.print(action);

        JSONObject response = new JSONObject();
        response.put("activePlayer", playerName);
        response.put("action", action);

        if (action != null) {
            sendOtherPlayers(playerName, response);
        }
    }

    private void startGame() {

        GameSession gameSession = new GameSession(waiters);
        allSessions.add(gameSession);
        for (int i = 0; i < waiters.length; ++i) {
            nameToGame.put(waiters[i], gameSession);
            webSocketService.notifyStartGame(gameSession.getSelf(waiters[i]));
        }
    }

    @Override
    public void sendOtherPlayers(String playerName, JSONObject data) {
        GameSession gameSession = nameToGame.get(playerName);
        Map<String, GameUser> users = gameSession.getUsers();

        for(Map.Entry<String, GameUser> entry : users.entrySet()) {
            GameUser user = entry.getValue();
            String name = entry.getKey();

            if (!name.equals(playerName)) {
                webSocketService.notifyEnemyAction(user, data);
            }
        }
    }
}
