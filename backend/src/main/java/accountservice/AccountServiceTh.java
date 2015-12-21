package accountservice;

import base.DBService;
import db.DBException;
import main.ThreadSettings;
import messagesystem.Abonent;
import messagesystem.Address;
import messagesystem.MessageSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static accountservice.Statuses.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author e.shubin
 */
public final class AccountServiceTh implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private DBService dBService;

    @NotNull
    private final Map<String, UserProfile> sessions = new HashMap<>();

    private long id = 1;

    public AccountServiceTh(MessageSystem messageSystem, DBService dbService) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);
        this.dBService =dbService;
    }

    public long getAndIncrementID() {
        return id++;
    }

    public void addUser(UserProfile userProfile) throws AccountServiceException {

        try {
            dBService.getUsersDAO().addUser(userProfile);
        } catch (DBException e) {
            throw new AccountServiceException(e);
        }
    }

    public void addSessions(@NotNull String sessionId, @NotNull UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public int getUsersQuantity() throws RuntimeException, AccountServiceException {
        try {
            return dBService.getUsersDAO().countUsers();
        } catch (DBException ignore) {
            throw new AccountServiceException("Can't count users.");
        }
    }

    public int getSessionsQuantity() {
        return sessions.size();
    }

    @Nullable
    public UserProfile getUser(@Nullable String userName) throws AccountServiceException {
        if (userName == null) return null;
        try {
            return dBService.getUsersDAO().getUserByName(userName);
        } catch (DBException ignore) {
            ignore.printStackTrace();
            throw new AccountServiceException("Error getting user from db");
        }
    }

    @Nullable
    public UserProfile getSessions(@Nullable String sessionId) {
        return sessions.get(sessionId);
    }

    public AuthorizationStates authenticate(String name, String password, String userId) {
        if (getSessions(userId) != null) return AuthorizationStates.AUTHORIZED;
        UserProfile user;
        try {
            user = getUser(name);
        } catch (AccountServiceException e) {
            e.printStackTrace();
            return AuthorizationStates.ERROR;
        }

        if (user == null) return AuthorizationStates.WRONG_AUTHORIZATION_DATA;
        if (user.getLogin().equals(name) && user.getPassword().equals(password)) {
            addSessions(userId, user);
            return AuthorizationStates.AUTHORIZED;
        }
        return AuthorizationStates.WRONG_AUTHORIZATION_DATA;
    }

    public boolean deleteSessions(@Nullable String sessionId) {
        if (sessions.get(sessionId) != null) {
            sessions.remove(sessionId);
            return true;
        }
        return false;
    }

    public void deleteUser(String name) throws AccountServiceException {
        try {
            dBService.getUsersDAO().deleteUserByName(name);
        } catch (DBException ignore) {
            throw new AccountServiceException("User was not deleted");
        }
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true){
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
