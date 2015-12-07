package db.handlers;

import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TResultHandler<T> {

    @Nullable
    T handle(ResultSet resultSet) throws SQLException;
}
