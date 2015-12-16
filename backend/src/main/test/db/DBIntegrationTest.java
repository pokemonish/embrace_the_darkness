package db;


import base.DBService;
import base.UserProfile;
import db.dao.UsersDAO;
import db.executor.TExecutor;
import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by fatman on 08/12/15.
 */
public class DBIntegrationTest extends BasicDBTest {

    private static final String USER_NAME_TEST = "Tommy";
    private static final String PASSWORD_TEST = "Ca$h";
    private static final int USERS_NUMBER_MAX = 15;
    private static final int USERS_NUMBER_MIN = 7;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Connection connection;
    private UsersDAO usersDAO;
    private UserProfile testProfile = new UserProfile(USER_NAME_TEST, PASSWORD_TEST, "");
    private UserProfile userProfileMock = mock(UserProfile.class);
    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(DBService.getUrl());
        usersDAO = new UsersDAO(new TExecutor(s_dbService));
    }

    @Test
    public void addGetAndDeleteUsersTest() throws SQLException, DBException {
        int numberOfUsers = new Random().nextInt(USERS_NUMBER_MAX) + USERS_NUMBER_MIN;

        for (int i = 0; i < numberOfUsers; ++i) {
            when(userProfileMock.getLogin()).thenReturn(USER_NAME_TEST + i);
            when(userProfileMock.getPassword()).thenReturn(PASSWORD_TEST + i);
            usersDAO.addUser(userProfileMock);
        }

        assertEquals(numberOfUsers, usersDAO.countUsers());

        for (int i = 0; i < numberOfUsers; ++i) {
            UserProfile profile = usersDAO.getUserByName(USER_NAME_TEST + i);
            if (profile == null) throw new SQLException();
            assertEquals(USER_NAME_TEST + i, profile.getLogin());
            assertEquals(PASSWORD_TEST + i, profile.getPassword());
        }

        for (int i = 0; i < numberOfUsers; ++i) {
            usersDAO.deleteUserByName(USER_NAME_TEST + i);
        }

        assertEquals(0, usersDAO.countUsers());
    }

    @Test
    public void uniqnessTest() throws SQLException, DBException {
        exception.expect(DBException.class);
        usersDAO.addUser(testProfile);
        usersDAO.addUser(testProfile);
    }

    @After
    public void cleanUp() throws SQLException, DBException {
        usersDAO.deleteUserByName(USER_NAME_TEST);
        connection.close();
    }
}
