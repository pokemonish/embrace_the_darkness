package main;

import base.DBService;
import base.UserProfile;
import db.DBException;
import db.DBServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import resources.Config;

import java.sql.SQLException;

import static org.junit.Assert.*;


/**
 * Created by fatman on 23/10/15.
 */
public class AccountServiceTest {

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
    public void testAddUser() throws Exception {
        accountService.addUser(testUser.getLogin(), testUser);

        final UserProfile user = accountService.getUser(testUser.getLogin());

        assertNotNull(user);
        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getPassword(), user.getPassword());

        accountService.deleteUser(testUser.getLogin());
        //assertEquals(testUser.getEmail(), user.getEmail());
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
    public void testUserUnicness() throws Exception {
        accountService.addUser(testUser.getLogin(), testUser);
        assertFalse(accountService.addUser(testUser.getLogin(), testUser));
        accountService.deleteUser(testUser.getLogin());
    }

    @Test
    public void testGetUser() throws Exception {

    }

    @Test
    public void testGetSessions() throws Exception {

    }

    @Test
    public void testGetUsersQuantity() throws RuntimeException {
        accountService.addUser(testUser.getLogin(), testUser);
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