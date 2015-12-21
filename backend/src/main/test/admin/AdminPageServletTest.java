package admin;

import accountservice.AccountServiceTh;
import base.GameMechanics;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

/**
 * Created by fatman on 05/11/15.
 */
public class AdminPageServletTest extends Mockito {

    private HttpServletRequest requestMock = mock(HttpServletRequest.class);
    private HttpServletResponse responseMock = mock(HttpServletResponse.class);
    private AccountServiceTh AccountServiceThMock = mock(AccountServiceTh.class);
    private GameMechanics gameMechanicsMock = mock(GameMechanics.class);

    private AdminPageServlet adminPageServlet =
            new AdminPageServlet(AccountServiceThMock, gameMechanicsMock);

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testShutDown() throws ServletException, IOException {

        exit.expectSystemExitWithStatus(0);
        final String SHUT_DOWN_STRING = "10";
        final String SHUT_DOWN_KEY = "shutdown";
        when(requestMock.getParameter(SHUT_DOWN_KEY)).thenReturn(SHUT_DOWN_STRING);

        adminPageServlet.doGet(requestMock, responseMock);
    }



}