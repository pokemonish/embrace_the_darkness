package base;

/**
 * @author v.chibrikov
 */
public class GameUser {
    private String myName;
    private String[] enemyNames = new String[2];
    private int myScore = 0;
    public GameUser(String myName) {
        this.myName = myName;
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
