package mechanics;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import utils.TimeHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author v.chibrikov
 */
@SuppressWarnings({"unchecked", "InfiniteLoopStatement"})
public class GameMechanicsImpl implements GameMechanics {

    private int playersNumber = 4;

    private static final int STEP_TIME_DEFAULT = 300;
    private int stepTime = STEP_TIME_DEFAULT;

    private static final int GAME_TIME_DEFAULT = 65;
    private int gameTime = GAME_TIME_DEFAULT * 1000;

    private final WebSocketService webSocketService;

    private final Map<String, GameSession> nameToGame = new HashMap<>();

    private final Set<GameSession> allSessions = new HashSet<>();

    private String[] waiters;

    public GameMechanicsImpl(WebSocketService webSocketService, @Nullable MechanicsParameters parameters) {
        this.webSocketService = webSocketService;
        if (parameters != null) {
            playersNumber = parameters.getPlayersNumber();
            stepTime = parameters.getStepTime();
            gameTime = parameters.getGameTime();
        }
        waiters = new String[playersNumber];
    }

    @Override
    public void addUser(String user) {

        System.out.print(countWaiters());

        int waitersNumber = countWaiters();

        System.out.print(playersNumber - 1);
        System.out.print(waitersNumber == playersNumber - 1);
        System.out.print(" User is " + user + ' ');

        if (waitersNumber == playersNumber - 1) {
            waiters[waitersNumber] = user;

            startGame();

            waiters = new String[playersNumber];
        } else {
            waiters[waitersNumber] = user;
        }
    }

    private int countWaiters() {
        int waitersNumber = 0;
        for (String waiter : waiters) {
            if (waiter != null) {
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
        webSocketService.notifyMyNewScore(myUser);
    }

    @Override
    public void run() {
        while (true) {
            gmStep();
            TimeHelper.sleep(stepTime);
        }
    }

    private void gmStep() {
        allSessions.stream().filter(session ->
                session.getSessionTime() > gameTime).forEach(session -> {

            GameUser winner = session.determineWinner();
            Map<String, GameUser> users = session.getUsers();

            for (Map.Entry<String, GameUser> entry : users.entrySet()) {
                GameUser user = entry.getValue();
                String name = entry.getKey();

                boolean isWinner = winner.getMyName().equals(name);

                webSocketService.notifyGameOver(user, isWinner);
            }
        });
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
        for (String waiter : waiters) {
            nameToGame.put(waiter, gameSession);
            webSocketService.notifyStartGame(gameSession.getSelf(waiter));
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
