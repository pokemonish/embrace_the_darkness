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

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            pageVariables.put("signUpStatus", "");
            response.getWriter().println(PageGenerator.getPage("signup.html", pageVariables));
        } else {
            String name = accountService.getSessions(userId.toString()).getLogin();
            name = name == null ? "user" : name;
            pageVariables.put("signUpStatus", "Hi, " + name + ", you are logged in.");
            response.getWriter().println(PageGenerator.getPage("signupstatus.html", pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("email");
        String password = request.getParameter("password");

        String htmlToRender = "signup.html";

        Map<String, Object> pageVariables = new HashMap<>();
        if (name.isEmpty()) {
            pageVariables.put("signUpStatus", "login is required");
        } else if (password.isEmpty()) {
            pageVariables.put("signUpStatus", "password is required");
        } else if (accountService.addUser(name, new UserProfile(name, password, ""))) {
            pageVariables.put("signUpStatus", "New user created");
            htmlToRender = "signupstatus.html";
        } else {
            pageVariables.put("signUpStatus", "User with name: " + name + " already exists");
        }
        response.getWriter().println(PageGenerator.getPage(htmlToRender, pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
