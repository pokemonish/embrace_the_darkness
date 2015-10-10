package frontend;

import main.AccountService;
import main.ResponseHandler;
import main.UserProfile;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class SignInServlet extends HttpServlet {

    @NotNull
    private AccountService accountService;

    public SignInServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override

    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();

        String sessionId = session.getId();
        String htmlToRender = "auth.html";

        UserProfile profile = accountService.getSessions(sessionId);

        if (profile != null) {

            htmlToRender = "authstatus.html";

            String name = profile.getLogin();
            pageVariables.put("loginStatus", "Hi, " + name + ", you are logged in.");
        } else {
            pageVariables.put("loginStatus", "");
        }

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Map<String, Object> pageVariables = new HashMap<>();

        String sessionId = session.getId();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String htmlToRender = "auth.html";

        if (email == null) {
            pageVariables.put("authstatus", "login is required");
        } else if (password == null) {
            pageVariables.put("authstatus", "password is required");
        } else if (accountService.getSessions(sessionId) == null) {
            UserProfile profile = accountService.getUser(email);
            if (profile != null && profile.getPassword().equals(password)) {

                assert sessionId != null;
                accountService.addSessions(sessionId, profile);

                pageVariables.put("loginStatus", "Login passed");
                htmlToRender = "authstatus.html";
            } else {
                pageVariables.put("loginStatus", "Wrong login/password");
            }
        } else {
            pageVariables.put("loginStatus", "You are alredy logged in");
        }

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }
}
