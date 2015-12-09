package db;

import base.DBService;
import base.UserProfile;
import db.dao.UsersDAO;
import db.handlers.ConnectionHandler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import resources.Config;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by fatman on 23/11/15.
 */
public class DBServiceImplTest {

    private static final String TEST_FILE_PATH = "cfg/test.properties";
    private static final String TEST_USER = "testLogin";
    private final UserProfile testUser = new UserProfile(TEST_USER, "testPassword", "test@mail.ru");
    private Connection connectionMock = mock(Connection.class);
    private UsersDAO usersDAOMock = mock(UsersDAO.class);

    private DBServiceImpl dbService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws DBException {
        dbService = spy(new DBServiceImpl());
        when(dbService.makeUsersDAO(any())).thenReturn(usersDAOMock);
        when(dbService.getConnection()).thenReturn(connectionMock);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws DBException{
        Config.setConfigFilePath(TEST_FILE_PATH);
        new DBServiceImpl();
    }

    @Test
    public void testAddUser() throws DBException, SQLException {
        dbService.addUser(testUser);
        verify(usersDAOMock).addUser(eq(testUser), eq(connectionMock));
    }

    @Test
    public void testGetUserByName() throws DBException, SQLException {
        dbService.getUserByName(TEST_USER);
        verify(usersDAOMock).getUserByName(eq(TEST_USER));
    }

    @Test
    public void testDeleteUserByName() throws DBException, SQLException {
        dbService.deleteUserByName(TEST_USER);
        verify(usersDAOMock).deleteUserByName(eq(TEST_USER));
    }

    @Test
    public void testCountUsers() throws DBException, SQLException {
        dbService.countUsers();
        verify(usersDAOMock).countUsers(eq(connectionMock));
    }

    @Test
    public void verifyNoSqlExceptionsAdd() throws DBException, SQLException {
        doThrow(new SQLException()).when(usersDAOMock).addUser(any(), any());
        exception.expect(DBException.class);
        dbService.addUser(testUser);
    }

    @Test
    public void verifyNoSqlExceptionsGet() throws DBException, SQLException {
        doThrow(new SQLException()).when(usersDAOMock).getUserByName(any());
        exception.expect(DBException.class);
        dbService.getUserByName(TEST_USER);
    }

    @Test
    public void verifyNoSqlExceptionsCount() throws DBException, SQLException {
        doThrow(new SQLException()).when(usersDAOMock).countUsers(any());
        exception.expect(DBException.class);
        dbService.countUsers();
    }

    @Test
    public void verifyNoSqlExceptionsDelete() throws DBException, SQLException {
        doThrow(new SQLException()).when(usersDAOMock).deleteUserByName(any());
        exception.expect(DBException.class);
        dbService.deleteUserByName(TEST_USER);
    }
}