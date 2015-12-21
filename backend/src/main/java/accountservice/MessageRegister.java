package accountservice;

import static accountservice.Statuses.*;

import db.AlreadyExistsException;
import frontendservice.MessageRegistered;
import messagesystem.Address;
import messagesystem.Message;

/**
 * @author e.shubin
 */
public final class MessageRegister extends MessageToAccountService {
    private UserProfile userProfile;

    public MessageRegister(Address from, Address to, UserProfile userProfile) {
        super(from, to);
        this.userProfile = userProfile;
    }

    @Override
    protected void exec(AccountServiceTh service) {
        SignUpStatuses result;

        try {
            service.addUser(userProfile);
            result = SignUpStatuses.SUCCESS;
        } catch (AccountServiceException e) {
            if (e.getCause().getClass().equals(AlreadyExistsException.class)) {
                result = SignUpStatuses.USER_ALREADY_EXISTS;
            } else {
                result = SignUpStatuses.ERROR;
            }
        }
        final Message back = new MessageRegistered(getTo(), getFrom(), userProfile.getLogin(), result);
        service.getMessageSystem().sendMessage(back);
    }
}
