package db.dao;

import db.DBException;
import db.DBServiceImpl;
import db.executor.TExecutor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import resources.Config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fatman on 07/12/15.
 */
public class DataBaseDAOImplTest extends Mockito {

    private static final String TEST_FILE_PATH = "cfg/test.properties";
    private Connection connectionMock = mock(Connection.class);
    private DataBaseDAOImpl dataBaseDAO;
    private TExecutor tExecutorMock = mock(TExecutor.class);

    @BeforeClass
    public static void setUpBeforeClass() throws DBException{
        Config.setConfigFilePath(TEST_FILE_PATH);
        new DBServiceImpl();
    }

    @Before
    public void setUp() {
        dataBaseDAO = new DataBaseDAOImpl(connectionMock, tExecutorMock);
    }

    @Test
    public void testCreateDB() throws DBException, SQLException {
        dataBaseDAO.createDB();
        verify(tExecutorMock, times(2)).execUpdate(eq(connectionMock),
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
}