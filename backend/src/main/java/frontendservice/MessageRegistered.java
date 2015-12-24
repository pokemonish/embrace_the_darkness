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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageRegistered o1 = (MessageRegistered) o;

        return !(name != null ? !name.equals(o1.name) : o1.name != null) &&
                registerStatus == o1.registerStatus;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (registerStatus != null ? registerStatus.hashCode() : 0);
        return result;
    }

    @Override
    protected void exec(FrontEnd frontEnd) {
        frontEnd.registered(name, registerStatus);
    }
}
