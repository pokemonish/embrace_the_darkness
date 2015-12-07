package base;

import db.DBException;

import java.sql.Connection;


/**
 * Created by fatman on 23/11/15.
 */

public interface DBService {

    Connection getConnection() throws DBException;

    UserProfile getUserByName(String name) throws DBException;

    void addUser(UserProfile user) throws DBException;

    int countUsers() throws DBException;
}
