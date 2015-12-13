package main;

/**
 * Created by fatman on 09/12/15.
 */
public class AccountServiceException extends Exception {
    public AccountServiceException() {}
    public AccountServiceException(Throwable throwable) { super(throwable);}
    public AccountServiceException(String message) {
        super(message);
    }
}
