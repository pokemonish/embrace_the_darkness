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
 * Created by v.chibrikov on 13.09.2014.
 */
public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
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

        response.getWriter().println(PageGenerator.getPage("signup.html", pageVariables));
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

        if (accountService.addUser(name, new UserProfile(name, password, ""))) {
            pageVariables.put("status", "New user created");
        } else {
            pageVariables.put("status", "User with name: " + name + " already exists");
        }

        // Fake JSON
        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
    }

}
