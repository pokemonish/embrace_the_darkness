package frontendservice;

import accountservice.UserProfile;
import messagesystem.Address;
import org.jetbrains.annotations.Nullable;
import static accountservice.Statuses.*;

/**
 * @author e.shubin
 */
public final class MessageIsAuthenticated extends MessageToFrontEnd {
    private String sessionId;
    private UserProfile account;
    private AuthorizationStates status;

    public MessageIsAuthenticated(Address from, Address to, String sessionId, AuthorizationStates status, @Nullable UserProfile account) {
        super(from, to);
        this.sessionId = sessionId;
        this.account = account;
        this.status = status;
    }

    @Override
    protected void exec(FrontEnd frontEnd) {
        frontEnd.setAuthStatus(sessionId, status);
        if (account != null) frontEnd.authenticated(sessionId, account);
    }
}
