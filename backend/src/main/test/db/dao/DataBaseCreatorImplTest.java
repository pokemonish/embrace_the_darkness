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

    @Before
    public void setUp() {
    }

    @Test
    public void testCreateDB() throws DBException, SQLException {
    }

    @Test
    public void testDropDB() throws DBException, SQLException {
    }
}