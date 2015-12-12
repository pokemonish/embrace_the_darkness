package db.dao;

import db.DBException;
import db.TestWithConfig;
import db.executor.TExecutor;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import resources.Config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fatman on 07/12/15.
 */
public class DataBaseCreatorImplTest extends TestWithConfig {

    private Connection connectionMock = mock(Connection.class);
    private DataBaseCreatorImpl dataBaseDAO;
    private TExecutor tExecutorMock = mock(TExecutor.class);

    @Before
    public void setUp() {
        dataBaseDAO = new DataBaseCreatorImpl(connectionMock, tExecutorMock);
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