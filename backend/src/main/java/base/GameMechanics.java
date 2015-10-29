package base;

/**
 * @author v.chibrikov
 */
public interface GameMechanics {

    public void addUser(String user);

    public void incrementScore(String userName);

    public void run();

    public void sendOtherPlayers(String player, String data);

    public void deleteIfWaiter(String player);
}
