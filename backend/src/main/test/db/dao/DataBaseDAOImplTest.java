package db.dao;

import db.DBException;
import db.executor.TExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import resources.Config;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by fatman on 07/12/15.
 */
public class DataBaseDAOImplTest extends Mockito {

    private Connection connectionMock = mock(Connection.class);
    private DataBaseDAOImpl dataBaseDAO;
    private TExecutor tExecutorMock = mock(TExecutor.class);
    private UsersDAO usersDAOMock = mock(UsersDAO.class);
    private static final String TEST_FILE_PATH = "cfg/test.properties";

    @Before
    public void setUp() {
        Config.setConfigFilePath(TEST_FILE_PATH);
        dataBaseDAO = spy(new DataBaseDAOImpl(connectionMock));
        when(dataBaseDAO.makeUsersDAO()).thenReturn(usersDAOMock);
        when(dataBaseDAO.makeTExecutor()).thenReturn(tExecutorMock);
    }

    @Test
    public void testCreateDB() throws DBException, SQLException {
        dataBaseDAO.createDB();
        verify(tExecutorMock).execUpdate(eq(connectionMock),
                matches("(.*CREATE.*" + Config.getInstance().getDbName()
                        + ".*)|(.*create.*" + Config.getInstance().getDbName() + ".*)"));
    }

    @Test
    public void testDropDB() throws DBException, SQLException {
        dataBaseDAO.dropDB();
        verify(tExecutorMock).execUpdate(eq(connectionMock),
                matches("(.*DROP.*" + Config.getInstance().getDbName()
                        + ".*)|(.*drop.*" + Config.getInstance().getDbName() + ".*)"));
    }

    @After
    public void cleanUp() {
        Config.setConfigFilePath(Config.CONFIG_FILE_PATH_DEFAULT);
    }
}