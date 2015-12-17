package db;

import base.DBService;
import base.DataBaseCreator;
import db.dao.DataBaseCreatorImpl;
import db.executor.TExecutor;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Created by fatman on 12/12/15.
 */
public class BasicDBTest extends TestWithConfig {

    protected static DBService s_dbService;

    private static boolean s_alreadyCreated = false;
    private static boolean s_alreadyDeleted = false;

    @BeforeClass
    public static void setUpBeforeClass() throws DBException {
        setTestConfig();
        if (!s_alreadyCreated) {
            s_dbService = new DBServiceImpl();
            s_alreadyCreated = true;
            s_alreadyDeleted = false;
        }
    }

    @AfterClass
    public static void cleanUpAfterClass() {
        if (!s_alreadyDeleted) {
            try {
                DataBaseCreator dataBaseCreator =
                        new DataBaseCreatorImpl(new TExecutor(s_dbService));
                dataBaseCreator.dropDB();
            } catch (DBException e) {
                e.printStackTrace();
                System.out.println("Database was not deleted");
            }
            s_alreadyDeleted = true;
            s_alreadyCreated = false;
        }
    }

}
