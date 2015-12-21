package db;

/**
 * Created by fatman on 06/12/15.
 */
public class DBException extends Exception {

    public DBException(Throwable throwable) {
        super(throwable);
    }

    public DBException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DBException(String message) {
        super(message);
    }
}
