package base;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by fatman on 07/11/15.
 */
public class GameUserTest extends Mockito {

    private static final String USER_NAME_TEST = "myTestName";
    private static final String ENEMY_NAME_TEST_1 = "enemy1TestName";
    private static final String ENEMY_NAME_TEST_2 = "enemy2TestName";
    private String[] enemyTestNames = new String[2];

    private GameUser gameUser = new GameUser(USER_NAME_TEST, 2);

    @Before
    public void setUp() {
        enemyTestNames[0] = ENEMY_NAME_TEST_1;
        enemyTestNames[1] = ENEMY_NAME_TEST_2;
    }

    @Test
    public void testGetMyName() throws Exception {
        assertEquals(gameUser.getMyName(), USER_NAME_TEST);
    }

    @Test
    public void testGetMyScore() throws Exception {
        assertEquals(gameUser.getMyScore(), 0);
    }

    @Test
    public void testIncrementMyScore() throws Exception {
        gameUser.incrementMyScore();
        assertEquals(gameUser.getMyScore(), 1);
    }

    @Test
    public void testSetEnemyNames() throws Exception {
        gameUser.setEnemyNames(enemyTestNames);
    }
}