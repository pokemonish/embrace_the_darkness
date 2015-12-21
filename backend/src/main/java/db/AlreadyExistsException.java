package db;

/**
 * Created by fatman on 20/12/15.
 */
public class AlreadyExistsException extends DBException {
    public AlreadyExistsException(Throwable throwable) { super(throwable);}
    public AlreadyExistsException(String message) {
        super(message);
    }
}
