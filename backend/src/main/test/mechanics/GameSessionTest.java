package mechanics;

import base.GameUser;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by fatman on 07/11/15.
 */
public class GameSessionTest {

    private final String[] testPlayers = new String[2];
    private static final String TEST_ENEMY = "TEST_ENEMY";

    private long startTime;
    private GameSession gameSession;

    @Before
    public void setUp() {
        for (int i = 0; i < 2; ++i) {
            testPlayers[i] = TEST_ENEMY + i;
        }
        startTime = new Date().getTime();
        gameSession = new GameSession(testPlayers);
    }

    @Test
    public void testGetSelf() throws Exception {
        for (String testPlayer : testPlayers) {
            GameUser user = gameSession.getSelf(testPlayer);
            assertEquals(user.getMyName(), testPlayer);
        }
    }

    @Test
    public void testGetSessionTime() throws Exception {
        assertEquals(gameSession.getSessionTime(), new Date().getTime() - startTime);
    }

    @Test
    public void testDetermineWinner() throws Exception {

    }

    @Test
    public void testGetUsers() throws Exception {
        Map<String, GameUser> userMap = gameSession.getUsers();

        outerloop:
        for (Map.Entry<String, GameUser> user : userMap.entrySet()) {

            String name = user.getValue().getMyName();
            for (String player : testPlayers) {
                if (player.equals(name)) {
                    continue outerloop;
                }
            }
            fail();
        }
    }
}