package db.executor;

import db.DBException;
import db.handlers.TResultHandler;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TExecutor {
    @Nullable
    public <T> T execQuery(Connection connection,
                           String query,
                           TResultHandler<T> handler) throws DBException {

        try {
            try (Statement stmt = connection.createStatement()) {

                stmt.execute(query);

                T value;

                try (ResultSet result = stmt.getResultSet()) {
                    value = handler.handle(result);
                }
                return value;
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }

    }

    public void execUpdate(Connection connection, String update) throws DBException {

        try {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(update);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
