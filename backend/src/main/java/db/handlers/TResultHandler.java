package db.handlers;

import db.DBException;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TResultHandler<T> {

    @Nullable
    T handle(ResultSet resultSet) throws SQLException, DBException;
}
