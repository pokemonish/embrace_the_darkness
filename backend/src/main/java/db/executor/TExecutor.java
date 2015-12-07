package db.executor;

import db.handlers.TResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TExecutor {
    public <T> T execQuery(Connection connection,
                           String query,
                           TResultHandler<T> handler)
            throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.getResultSet()) {

            stmt.execute(query);
            T value = handler.handle(result);
            result.close();
            return value;
        }
    }

    public void execUpdate(Connection connection, String update) throws SQLException {

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(update);
            stmt.close();
        }
    }
}
