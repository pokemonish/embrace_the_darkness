package main;

import base.DBService;
import base.UserProfile;
import db.DBException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService {

    private DBService dbService;

    public AccountService(DBService dbService) {
        this.dbService = dbService;
    }

    @NotNull
    private final Map<String, UserProfile> sessions = new HashMap<>();

    private long id = 1;

    public long getAndIncrementID() {
        return id++;
    }

    public void addUser(UserProfile userProfile) throws AccountServiceException {

        try {
            dbService.addUser(userProfile);
        } catch (DBException e) {
            throw new AccountServiceException(e);
        }
    }

    public void addSessions(@NotNull String sessionId, @NotNull UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public int getUsersQuantity() throws RuntimeException, AccountServiceException {
        try {
            return dbService.countUsers();
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
            return dbService.getUserByName(userName);
        } catch (DBException ignore) {
            throw new AccountServiceException("Error getting user from db");
        }
    }

    @Nullable
    public UserProfile getSessions(@Nullable String sessionId) {
        return sessions.get(sessionId);
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
            dbService.deleteUserByName(name);
        } catch (DBException ignore) {
            throw new AccountServiceException("User was not deleted");
        }
    }
}
