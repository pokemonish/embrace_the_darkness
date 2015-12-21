package accountservice;

import frontendservice.MessageIsAuthenticated;
import messagesystem.Address;
import messagesystem.Message;
import static accountservice.Statuses.*;

/**
 * @author e.shubin
 */
public final class MessageAuthenticate extends MessageToAccountService {
    private String name;
    private String password;
    private String sessionId;

    public MessageAuthenticate(Address from, Address to, String name, String password, String sessionId) {
        super(from, to);
        this.name = name;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(AccountServiceTh service) {
        AuthorizationStates status = service.authenticate(name, password, sessionId);
        UserProfile account = null;
        if (status == AuthorizationStates.AUTHORIZED) account = service.getSessions(sessionId);
        final Message back = new MessageIsAuthenticated(getTo(), getFrom(), sessionId, status, account);
        service.getMessageSystem().sendMessage(back);
    }
}
