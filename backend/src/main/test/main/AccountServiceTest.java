package main;

import base.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by fatman on 23/10/15.
 */
public class AccountServiceTest {

    @NotNull
    private final AccountService accountService = new AccountService();
    @NotNull
    private final UserProfile testUser = new UserProfile("testLogin", "testPassword", "test@mail.ru");

    @Test
    public void testAddUser() throws Exception {
        accountService.addUser(testUser.getLogin(), testUser);

        final UserProfile user = accountService.getUser(testUser.getLogin());

        assertNotNull(user);
        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getPassword(), user.getPassword());
        assertEquals(testUser.getEmail(), user.getEmail());
    }

    @Test
    public void testAddSessions() throws Exception {

    }

    @Test
    public void testGetUser() throws Exception {

    }

    @Test
    public void testGetSessions() throws Exception {

    }
}