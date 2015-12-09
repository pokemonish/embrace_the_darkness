package db;


import base.DBService;
import base.UserProfile;
import db.dao.UsersDAO;
import db.datasets.UsersDataSet;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import resources.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by fatman on 08/12/15.
 */
public class DBIntegrationTest extends Mockito {

    private static final String TEST_FILE_PATH = "cfg/test.properties";
    private static final String USER_NAME_TEST = "Tommy";
    private static final String PASSWORD_TEST = "Ca$h";
    private static final int USERS_NUMBER_MAX = 15;
    private static final int USERS_NUMBER_MIN = 7;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Connection connection;
    private UsersDAO usersDAO;
    private UserProfile userProfileMock = mock(UserProfile.class);

    @BeforeClass
    public static void setUpBeforeClass() throws DBException{
        Config.setConfigFilePath(TEST_FILE_PATH);
        new DBServiceImpl();
    }

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(DBService.getUrl());
        usersDAO = new UsersDAO(connection);
    }

    @Test
    public void addGetAndDeleteUsersTest() throws SQLException {
        int numberOfUsers = new Random().nextInt(USERS_NUMBER_MAX) + USERS_NUMBER_MIN;

        for (int i = 0; i < numberOfUsers; ++i) {
            when(userProfileMock.getLogin()).thenReturn(USER_NAME_TEST + i);
            when(userProfileMock.getPassword()).thenReturn(PASSWORD_TEST + i);
            usersDAO.addUser(userProfileMock, connection);
        }

        assertEquals(numberOfUsers, usersDAO.countUsers(connection));

        for (int i = 0; i < numberOfUsers; ++i) {
            UsersDataSet profile = usersDAO.getUserByName(USER_NAME_TEST + i);
            if (profile == null) throw new SQLException();
            assertEquals(USER_NAME_TEST + i, profile.getName());
            assertEquals(PASSWORD_TEST + i, profile.getPassword());
        }

        for (int i = 0; i < numberOfUsers; ++i) {
            usersDAO.deleteUserByName(USER_NAME_TEST + i);
        }

        assertEquals(0, usersDAO.countUsers(connection));
    }

    @Test
    public void uniqnessTest() throws SQLException {
        exception.expect(SQLException.class);
        when(userProfileMock.getLogin()).thenReturn(USER_NAME_TEST + 0);
        when(userProfileMock.getPassword()).thenReturn(PASSWORD_TEST + 0);
        usersDAO.addUser(userProfileMock, connection);
        usersDAO.addUser(userProfileMock, connection);
    }

    @After
    public void cleanUp() throws SQLException, DBException {
        usersDAO.deleteUserByName(USER_NAME_TEST + 0);
        connection.close();
    }

    @AfterClass
    public static void cleanUpAfterClass() {
        Config.setConfigFilePath(Config.CONFIG_FILE_PATH_DEFAULT);
    }
}
