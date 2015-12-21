package accountservice;

import frontendservice.MessageIsGone;
import messagesystem.Address;
import messagesystem.Message;

/**
 * Created by fatman on 20/12/15.
 */
public class MessageExit extends MessageToAccountService {
    private final String userId;

    public MessageExit(Address from, Address to, String userId) {
        super(from, to);
        this.userId = userId;
    }

    @Override
    protected void exec(AccountServiceTh service) {
        service.deleteSessions(userId);
        final Message back = new MessageIsGone(getTo(), getFrom(), userId);
        service.getMessageSystem().sendMessage(back);
    }
}
