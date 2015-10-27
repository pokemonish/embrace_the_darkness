package frontend;

import base.AuthService;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by fatman on 26/10/15.
 */


public class PostNameServlet extends HttpServlet {

    private AuthService authService;

    public PostNameServlet(@NotNull AuthService authService) { this.authService = authService; }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        String sessionId = session.getId();
        this.authService.saveUserName(sessionId, request.getParameter("username"));
    }
}