package db.executor;

import base.DBService;
import db.DBException;
import db.handlers.ConnectionConsumer;
import db.handlers.TResultHandler;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TExecutor {
    DBService dbService;
    Connection conn = null;

    public TExecutor(DBService dbService) {
        this.dbService = dbService;
    }

    @Nullable
    public<T> T execQuery(String query,
                           TResultHandler<T> handler) throws DBException {
        return dbService.connectAndReturn(connection -> {
            if (conn == null) conn = connection;
            try (Statement stmt = conn.createStatement()) {

                stmt.execute(query);

                T value;

                try (ResultSet result = stmt.getResultSet()) {
                    value = handler.handle(result);
                }
                return value;
            } finally {
                if (conn.equals(connection))conn = null;
            }
        });
    }

    public void execUpdate(String update) throws DBException {

        dbService.connectAndUpdate(connection -> {
            if (conn == null) conn = connection;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(update);
            } finally {
                if (conn.equals(connection)) conn = null;
            }
        });
    }

    public void execUpdate(Connection connection, String update) throws DBException {

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(update);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void execTransaction(ConnectionConsumer consumer) throws DBException  {
        dbService.connectAndUpdate(connection ->
        {
            if (conn == null) conn = connection;
            try {
                conn.setAutoCommit(false);
                consumer.handle(conn);
                conn.commit();
            } catch (SQLException e) {

                try {
                    conn.rollback();
                } catch (SQLException ignore) {
                }
                throw new DBException(e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignore) {
                }
                if (conn.equals(connection)) conn = null;
            }
        });
    }
}
