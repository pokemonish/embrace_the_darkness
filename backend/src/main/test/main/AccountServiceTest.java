package main;

import base.DBService;
import base.UserProfile;
import db.DBException;
import db.DBServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.*;
import org.junit.rules.ExpectedException;
import resources.Config;
import static org.junit.Assert.*;


/**
 * Created by fatman on 23/10/15.
 */
public class AccountServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final String TEST_FILE_PATH = "cfg/test.properties";
    private AccountService accountService;
    @NotNull
    private final UserProfile testUser = new UserProfile("testLogin", "testPassword", "test@mail.ru");
    @NotNull
    private final String sessionId = "testSessionId";

    @BeforeClass
    public static void setUpBeforeClass() throws DBException{
        Config.setConfigFilePath(TEST_FILE_PATH);
        new DBServiceImpl();
    }

    @Before
    public void setUp() throws DBException {
        DBService service = new DBServiceImpl();
        accountService = new AccountService(service);
    }

    @Test
    public void testAddUser() throws AccountServiceException {
        accountService.addUser(testUser);

        final UserProfile user = accountService.getUser(testUser.getLogin());

        assertNotNull(user);
        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getPassword(), user.getPassword());

        accountService.deleteUser(testUser.getLogin());
    }


    @Test
    public void testAddSessions() throws Exception {

        accountService.addSessions(sessionId, testUser);

        final UserProfile user = accountService.getSessions(sessionId);

        assertNotNull(user);
        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getPassword(), user.getPassword());
        assertEquals(testUser.getEmail(), user.getEmail());
    }

    @Test
    public void testUserUnicness() throws AccountServiceException {
        accountService.addUser(testUser);
        exception.expect(AccountServiceException.class);
        accountService.addUser(testUser);
    }

    @Test
    public void testGetUser() throws Exception {

    }

    @Test
    public void testGetSessions() throws Exception {

    }

    @Test
    public void testGetUsersQuantity() throws RuntimeException, AccountServiceException {
        accountService.addUser(testUser);
        int quantity = accountService.getUsersQuantity();
        assertEquals(1, quantity);
        accountService.deleteUser(testUser.getLogin());
    }

    @Test
    public void testGetSessionsQuantity() throws Exception {
        accountService.addSessions(sessionId, testUser);
        assert(accountService.getSessionsQuantity() == 1);
    }

    @Test
    public void testDeleteSessions() {
        String falseID = "falseID";

        assertFalse(accountService.deleteSessions(falseID));

        accountService.deleteSessions(sessionId);
        assertNull(accountService.getSessions(sessionId));
    }

    @After
    public void cleanUp() {
    }
}