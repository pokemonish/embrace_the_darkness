package db.handlers;

import db.DBException;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fatman on 08/12/15.
 */
public interface ConnectionHandler<T> {

    @Nullable
    T handle(Connection connection) throws SQLException, DBException;
}
