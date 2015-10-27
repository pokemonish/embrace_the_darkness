package mechanics;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
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

    private void startGame() {

        /*if (countWaiters() != PLAYERS_NUMBER) {
            System.out.print("Not enough players to start the game!");
            throw new Exception("Not enough players to start the game!");
        }*/

        GameSession gameSession = new GameSession(waiters);
        allSessions.add(gameSession);
        for (int i = 0; i < waiters.length; ++i) {
            nameToGame.put(waiters[i], gameSession);
            webSocketService.notifyStartGame(gameSession.getSelf(waiters[i]));
        }
    }

    @Override
    public void sendOtherPlayers(String playerName, String data) {
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
