package base;

/**
 * @author v.chibrikov
 */
public class GameUser {
    private final String myName;
    private String[] enemyNames;
    private int myScore = 0;

    public boolean isDead() {
        return isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    private boolean isDead = false;

    public GameUser(String myName, int enemiesNumber) {
        this.myName = myName;
        this.enemyNames = new String[enemiesNumber];
    }

    public String getMyName() {
        return myName;
    }

    public String[] getEnemyNames() {
        return enemyNames;
    }

    public int getMyScore() {
        return myScore;
    }

    public void incrementMyScore() {
        myScore++;
    }

    public void setEnemyNames(String[] enemyNames) {
        this.enemyNames = enemyNames;
    }
}
