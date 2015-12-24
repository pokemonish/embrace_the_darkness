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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageIsAuthenticated o1 = (MessageIsAuthenticated) o;

        return !(sessionId != null ? !sessionId.equals(o1.sessionId) : o1.sessionId != null) && !(account != null ? !account.equals(o1.account) : o1.account != null) && status == o1.status;

    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

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
