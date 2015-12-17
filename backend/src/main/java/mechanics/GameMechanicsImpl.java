package mechanics;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import utils.TimeHelper;

import java.util.*;

/**
 * @author v.chibrikov
 */

public class GameMechanicsImpl implements GameMechanics {

    private static final int PLAYERS_NUMBER_DEFAULT = 4;
    private int playersNumber = PLAYERS_NUMBER_DEFAULT;

    private static final int STEP_TIME_DEFAULT = 300;
    private int stepTime = STEP_TIME_DEFAULT;

    private static final int GAME_TIME_DEFAULT = 65;
    private int gameTime = GAME_TIME_DEFAULT * 1000;

    private final WebSocketService webSocketService;

    private final Map<String, GameSession> dinoraika = new HashMap<>();

    private final Set<GameSession> allSessions = new HashSet<>();

    private boolean isActive = true;

    @Override
    public boolean isActive() {
        return isActive;
    }


    @Override
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    private String[] waiters;

    public GameMechanicsImpl(WebSocketService webSocketService,
                             @Nullable MechanicsParameters parameters) {
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
        GameSession myGameSession = dinoraika.get(userName);
        GameUser myUser = myGameSession.getSelf(userName);
        myUser.incrementMyScore();
        webSocketService.notifyMyNewScore(myUser);
    }

    @Override
    public void run() {
        while (isActive) {

            gmStep();
            TimeHelper.sleep(stepTime);
        }
    }

    private void checkEverybodyDied() {
        for(Iterator<GameSession> it = allSessions.iterator(); it.hasNext(); ) {
            GameSession session = it.next();
            if(session.getDeadPlayers() == playersNumber) {
                handleGameOver(session);
                it.remove();
            }
        }
    }

    @SuppressWarnings("unused")
    private void checkTimeIsOver() {
        allSessions.stream().filter(session ->
            session.getSessionTime() > gameTime).forEach(this::handleGameOver);
    }

    private void handleGameOver(GameSession session) {
        GameUser winner = session.getWinner();
        Map<String, GameUser> users = session.getUsers();

        for (Map.Entry<String, GameUser> entry : users.entrySet()) {
            GameUser user = entry.getValue();
            String name = entry.getKey();

            boolean isWinner = winner.getMyName().equals(name);

            webSocketService.notifyGameOver(user, isWinner);
        }
    }

    private void gmStep() {
        checkEverybodyDied();
        //checkTimeIsOver();
    }

    @Override
    public void processGameLogicData(String playerName, JsonObject data) {
        processGameLogicData(playerName, data, false);
    }

    @Override
    public void processGameLogicData(String playerName, JsonObject data, boolean isFromJoystick) {

        if (!allSessions.contains(dinoraika.get(playerName))) return;

        String action = data.get("data").getAsString();
        System.out.println(data.toString());

        JsonObject response = new JsonObject();
        response.addProperty("activePlayer", playerName);
        response.addProperty("action", action);

        if (action != null) {
            if (isFromJoystick) {
                sendEverybody(playerName, response);
            } else {
                sendOtherPlayers(playerName, response);
            }
            if (action.equals("dead")) {
                killPlayer(playerName);
            }
        }
    }

    @Override
    public void killPlayer(String playerName) {
        GameSession gameSession = dinoraika.get(playerName);
        if (gameSession.getDeadPlayers() == playersNumber - 1) {
            gameSession.setWinner(gameSession.getUsers().get(playerName));
        }
        gameSession.incrementDeadPlayers();
        gameSession.getUsers().get(playerName).setIsDead(true);
    }

    private void startGame() {

        GameSession gameSession = new GameSession(waiters);
        allSessions.add(gameSession);
        for (String waiter : waiters) {
            dinoraika.put(waiter, gameSession);
            webSocketService.notifyStartGame(gameSession.getSelf(waiter));
        }
    }

    @Override
    public void sendOtherPlayers(String playerName, JsonObject data) {
        GameSession gameSession = dinoraika.get(playerName);
        Map<String, GameUser> users = gameSession.getUsers();

        for(Map.Entry<String, GameUser> entry : users.entrySet()) {
            GameUser user = entry.getValue();
            String name = entry.getKey();

            if (!name.equals(playerName)) {
                webSocketService.notifyEnemyAction(user, data);
            }
        }
    }

    @Override
    public void sendEverybody(String playerName, JsonObject data) {
        GameSession gameSession = dinoraika.get(playerName);
        Map<String, GameUser> users = gameSession.getUsers();

        for(Map.Entry<String, GameUser> entry : users.entrySet()) {
            GameUser user = entry.getValue();
            String name = entry.getKey();

            if (!name.equals(playerName)) {
                webSocketService.notifyEnemyAction(user, data);
            } else {
                JsonObject newData = new JsonObject();
                for(Map.Entry<String, JsonElement> dataEntry : data.entrySet()) {
                    System.out.println(dataEntry.getKey());
                    System.out.println(dataEntry.getValue());
                    if (dataEntry.getKey().equals("status")) {
                        newData.addProperty(dataEntry.getKey(), "self_" + dataEntry.getValue().getAsString());
                        System.out.println("Sending special: " + "self_" + dataEntry.getKey() +
                                            ' ' +  dataEntry.getValue().getAsString());
                    } else {
                        newData.addProperty(dataEntry.getKey(), dataEntry.getValue().getAsString());
                    }
                }

                webSocketService.notifyEnemyAction(user, newData);
            }
        }
    }
}
