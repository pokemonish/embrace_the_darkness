package frontendservice;

import static accountservice.Statuses.*;
import accountservice.MessageAuthenticate;
import accountservice.MessageExit;
import accountservice.MessageRegister;
import accountservice.UserProfile;
import main.ThreadSettings;
import messagesystem.Abonent;
import messagesystem.Address;
import messagesystem.Message;
import messagesystem.MessageSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author e.shubin
 */
public class FrontEnd implements FrontEndService, Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private final Map<String, SignUpStatuses> waitingUsers = new HashMap<>();
    private final Map<String, AuthorizationStates> userAuthStates = new HashMap<>();
    private final Map<String, UserProfile> accountMap = new HashMap<>();

    public FrontEnd(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerFrontEnd(this);
    }

    @Override
    public void register(UserProfile userProfile) {
        final Message messageRegister = new MessageRegister(
                address, messageSystem.getAddressService().getAccountServiceAddress(), userProfile);
        messageSystem.sendMessage(messageRegister);
    }

    @Override
    public SignUpStatuses getRegistrationResult(String name) {
        return waitingUsers.get(name);
    }

    @Override
    public String authenticate(String name, String password) {
        final String sessionId = UUID.randomUUID().toString();
        setAuthStatus(sessionId, AuthorizationStates.WAITING_FOR_AUTHORIZATION);
        Message messageAuthenticate =
                new MessageAuthenticate(address, messageSystem.getAddressService().getAccountServiceAddress(),
                                             name, password, sessionId);
        messageSystem.sendMessage(messageAuthenticate);
        return sessionId;
    }

    @Override
    public UserProfile isAuthenticated(String sessionId) {
        return accountMap.get(sessionId);
    }

    void registered(String name, SignUpStatuses result) {
        waitingUsers.put(name, result);
    }

    void authenticated(String sessionId, UserProfile account) {
        accountMap.put(sessionId, account);
    }

    public void setAuthStatus(String sessionId, AuthorizationStates status) {
        userAuthStates.put(sessionId, status);
    }

    public AuthorizationStates getAuthStatus(String sessionId) {
        return userAuthStates.get(sessionId);
    }

    @Override
    public void exit(String userId) {
        Message messageExit =
                new MessageExit(address, messageSystem.getAddressService().getAccountServiceAddress(), userId);
        messageSystem.sendMessage(messageExit);
    }

    @Override
    public void exited(String userId) {
        accountMap.remove(userId);
        userAuthStates.put(userId, null);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true) {
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(ThreadSettings.SERVICE_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
