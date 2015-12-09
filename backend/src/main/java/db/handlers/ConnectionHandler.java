package db.handlers;

import db.DBException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fatman on 08/12/15.
 */
public interface ConnectionHandler {

    void handle(Connection connection) throws SQLException, DBException;
}
