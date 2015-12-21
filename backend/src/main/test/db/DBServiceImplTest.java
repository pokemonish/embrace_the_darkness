package db;

import accountservice.UserProfile;
import db.dao.UsersDAO;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.sql.Connection;

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


}