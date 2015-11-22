package resources;

import base.GameMechanics;
import base.UserProfile;
import base.WebSocketService;
import frontend.WebSocketServiceImpl;
import mechanics.GameMechanicsImpl;
import mechanics.MechanicsParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by fatman on 09/11/15.
 */
public class ReadXMLFileSAXTest extends Mockito {

    private static final String USERNAME_TEST = "testLogin";
    private static final String PASSWORD_TEST = "testPassword";
    private static final  String EMAIL_TEST = "testEmail";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testReadXML() throws Exception {
        UserProfile testUserFromXml = (UserProfile)ReadXMLFileSAX.readXML("data/test.xml");
        UserProfile testUser = new UserProfile(USERNAME_TEST, PASSWORD_TEST, EMAIL_TEST);
        assertEquals(testUser, testUserFromXml);
    }
}