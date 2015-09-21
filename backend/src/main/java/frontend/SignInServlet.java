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

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            pageVariables.put("loginStatus", "");
            response.getWriter().println(PageGenerator.getPage("auth.html", pageVariables));
        } else {
            String name = accountService.getSessions(userId.toString()).getLogin();
            name = name == null ? "user" : name;
            pageVariables.put("loginStatus", "Hi, " + name + ", you are logged in.");
            response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        Map<String, Object> pageVariables = new HashMap<>();

        Long userId = (Long) session.getAttribute("userId");
        String htmlToRender = "auth.html";

        //userId checking is temporary
        if (userId == null ) {
            UserProfile profile = accountService.getUser(email);
            if (profile != null && profile.getPassword().equals(password)) {

                userId = (long) (Math.random() * 1000);
                session.setAttribute("userId", userId);
                accountService.addSessions(userId.toString(), profile);

                pageVariables.put("loginStatus", "Login passed");
                htmlToRender = "authstatus.html";
            } else {
                pageVariables.put("loginStatus", "Wrong login/password");
            }
        } else {
            pageVariables.put("loginStatus", "You are alredy logged in");
        }

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(PageGenerator.getPage(htmlToRender, pageVariables));
    }
}
