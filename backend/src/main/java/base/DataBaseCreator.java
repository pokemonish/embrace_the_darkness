package base;

import db.DBException;

/**
 * Created by fatman on 07/12/15.
 */

public interface DataBaseCreator {

    void createDB() throws DBException;

    void dropDB() throws DBException;
}
