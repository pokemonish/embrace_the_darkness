package frontendservice;

import static accountservice.Statuses.*;
import messagesystem.Address;

/**
 * @author e.shubin
 */
public final class MessageRegistered extends MessageToFrontEnd {
    private String name;
    private SignUpStatuses registerStatus;

    public MessageRegistered(Address from, Address to, String name, SignUpStatuses result) {
        super(from, to);
        this.name = name;
        this.registerStatus = result;
    }

    @Override
    protected void exec(FrontEnd frontEnd) {
        frontEnd.registered(name, registerStatus);
    }
}
