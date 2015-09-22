package frontend;

import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;

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
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session = request.getSession();
        String sessionId = session.getId();

        UserProfile profile = accountService.getSessions(sessionId);

        if (profile != null) {
            pageVariables.put("profile", profile.getLogin());
        } else {
            pageVariables.put("profile", null);
        }

        response.getWriter().println(PageGenerator.getPage("login.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();

        // Check auth
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        UserProfile profile = accountService.getSessions(sessionId);

        if (profile != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println();

            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);

        profile = accountService.getUser(name);
        if (profile != null && profile.getPassword().equals(password)) {
            accountService.addSessions(sessionId, profile);

            pageVariables.put("status", "Login passed");
        } else {
            pageVariables.put("status", "Wrong login/password");
        }



        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
    }
}
