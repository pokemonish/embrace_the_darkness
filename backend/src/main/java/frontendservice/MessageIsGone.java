package frontendservice;

import messagesystem.Address;

/**
 * Created by fatman on 20/12/15.
 */
public class MessageIsGone extends MessageToFrontEnd {
    private String userId;

    public MessageIsGone(Address from, Address to, String userId) {
        super(from, to);
        this.userId = userId;
    }

    @Override
    public void exec(FrontEnd frontEnd) {
        frontEnd.exited(userId);
    }
}
