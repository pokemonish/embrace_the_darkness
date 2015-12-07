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

    public boolean addUser(String userName, UserProfile userProfile) {

        try {
            dbService.addUser(userProfile);
        } catch (DBException e) {
            return false;
        }

        return true;
    }

    public void addSessions(@NotNull String sessionId, @NotNull UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public int getUsersQuantity() throws RuntimeException{
        try {
            return dbService.countUsers();
        } catch (DBException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't count users.");
        }
    }

    public int getSessionsQuantity() {
        return sessions.size();
    }

    @Nullable
    public UserProfile getUser(@Nullable String userName) {
        if (userName == null) return null;
        try {
            return dbService.getUserByName(userName);
        } catch (DBException e) {
            e.printStackTrace();
            return null;
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
}
