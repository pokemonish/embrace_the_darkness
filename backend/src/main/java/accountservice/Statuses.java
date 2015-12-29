package accountservice;

/**
 * Created by fatman on 20/12/15.
 */
public class Statuses {
    public enum SignUpStatuses {
        USER_ALREADY_EXISTS,
        ERROR,
        SUCCESS
    }

    public enum AuthorizationStates {
        WAITING_FOR_REGISTRATION,
        WAITING_FOR_AUTHORIZATION,
        AUTHORIZED,
        WRONG_AUTHORIZATION_DATA,
        ERROR
    }
}
