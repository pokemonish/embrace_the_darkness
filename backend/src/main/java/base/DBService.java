package base;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fatman on 23/11/15.
 */

public interface DBService {
    Connection getConnection();

    UserProfile getUserByName(String name) throws SQLException;

    void addUser(UserProfile user) throws SQLException;

    int countUsers() throws SQLException;
}
