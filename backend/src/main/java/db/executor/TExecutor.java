package db.executor;

import base.DBService;
import db.DBException;
import db.handlers.ConnectionConsumer;
import db.handlers.TResultHandler;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TExecutor {
    DBService dbService;

    public TExecutor(DBService dbService) {
        this.dbService = dbService;
    }

    @Nullable
    public<T> T execQuery(String query,
                           TResultHandler<T> handler) throws DBException {
        return dbService.connectAndReturn(connection -> {
            try (Statement stmt = connection.createStatement()) {

                stmt.execute(query);

                T value;

                try (ResultSet result = stmt.getResultSet()) {
                    value = handler.handle(result);
                }
                return value;
            }
        });
    }

    public void execUpdate(String update) throws DBException {

        dbService.connectAndUpdate(connection -> {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(update);
            }
        });
    }

    public void execTransaction(ConnectionConsumer consumer) throws DBException  {
        dbService.connectAndUpdate(connection ->
        {
            try {
                connection.setAutoCommit(false);
                consumer.handle(connection);
                connection.commit();
            } catch (SQLException e) {

                try {
                    connection.rollback();
                } catch (SQLException ignore) {
                }
                throw new DBException(e);
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignore) {
                }
            }
        });
    }
}
