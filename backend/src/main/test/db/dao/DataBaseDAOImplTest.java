package db.dao;

import base.DataBaseDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.junit.Assert.*;

/**
 * Created by fatman on 07/12/15.
 */
public class DataBaseDAOImplTest extends Mockito {

    private Connection connectionMock = mock(Connection.class);
    private DataBaseDAO dataBaseDAO = spy(DataBaseDAOImpl.makeDataBaseDAO(connectionMock));

    @Before
    public void setUp() {

    }

    @Test
    public void testCreateDB() throws Exception {

    }

    @Test
    public void testDropDB() throws Exception {

    }
}