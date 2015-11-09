package resources;

import base.GameMechanics;
import base.UserProfile;
import base.WebSocketService;
import frontend.WebSocketServiceImpl;
import mechanics.GameMechanicsImpl;
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

    private static final WebSocketService webSocketServiceMock = mock(WebSocketServiceImpl.class);
    private static final WebSocketService webSocketService = new WebSocketServiceImpl();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testReadXML() throws Exception {
        UserProfile testUserFromXml = (UserProfile)ReadXMLFileSAX.readXML("data/test.xml");
        UserProfile testUser = new UserProfile(USERNAME_TEST, PASSWORD_TEST, EMAIL_TEST);
        assertEquals(testUser, testUserFromXml);
    }

    @Test
    public void testReadXML1() throws Exception {
        GameMechanics gameMechanicsTest =
                (GameMechanics)ReadXMLFileSAX.readXML("data/GameMechanics.xml",
                                                        GameMechanicsImpl.class.getName(),
                                                        webSocketService);

        assertNotNull(gameMechanicsTest);


    }
}