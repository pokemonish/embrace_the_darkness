package accountservice;

import messagesystem.Abonent;
import messagesystem.Address;
import messagesystem.Message;

/**
 * @author e.shubin
 */
public abstract class MessageToAccountService extends Message {
    public MessageToAccountService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof AccountServiceTh) {
            exec((AccountServiceTh) abonent);
        }
    }

    protected abstract void exec(AccountServiceTh service);
}
