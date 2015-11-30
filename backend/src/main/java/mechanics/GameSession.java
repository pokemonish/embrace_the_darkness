package mechanics;

import base.GameUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class GameSession {
    private final long startTime;

    private final Map<String, GameUser> users = new HashMap<>();
    private GameUser winner;

    private byte deadPlayers = 0;

    public byte getDeadPlayers() {
        return deadPlayers;
    }

    public void incrementDeadPlayers() {
        ++this.deadPlayers;
    }


    public GameSession(String[] players) {
        startTime = new Date().getTime();

        for (int i = 0; i < players.length; ++i) {
            GameUser gameUser = new GameUser(players[i]);
            String[] enemies = new String[2];
            int k = 0;
            for (int j = 0; j < players.length; ++j) {
                if (i != j) {
                    enemies[k] = players[j];
                    k++;
                }
            }
            gameUser.setEnemyNames(enemies);

            users.put(players[i], gameUser);
        }

    }

    public GameUser getSelf(String user) {
        return users.get(user);
    }

    public long getSessionTime(){
        return new Date().getTime() - startTime;
    }

public GameUser getWinner() {
        return winner;
    }

    public void setWinner(GameUser winner) {
        this.winner = winner;
    }


    @SuppressWarnings("unused")
    public GameUser determineWinner() {
        for(Map.Entry<String, GameUser> entry : users.entrySet()) {
            GameUser user = entry.getValue();
            if(user.getMyScore() > winner.getMyScore()) {
                winner = user;
            }
        }
        return winner;
    }

    public Map<String, GameUser> getUsers() {
        return users;
    }
}
