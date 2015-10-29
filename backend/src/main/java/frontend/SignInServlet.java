package frontend;

import main.AccountService;
import main.ResponseHandler;
import base.UserProfile;
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

        Long userId = (Long) session.getAttribute("userId");
        String htmlToRender = "auth.html";

        if (userId == null) {
            pageVariables.put("loginStatus", "");
        } else {
            UserProfile profile = accountService.getSessions(String.valueOf(userId));

            if (profile != null) {

                htmlToRender = "authstatus.html";

                String name = profile.getLogin();
                pageVariables.put("loginStatus", "Hi, " + name + ", you are logged in.");
            } else {
                pageVariables.put("loginStatus", "");
            }
        }

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {


        HttpSession session = request.getSession();
        Map<String, Object> pageVariables = new HashMap<>();

        String email = request.getParameter("email");
        email = email != null ? email: "";

        String password = request.getParameter("password");
        password = password != null ? password: "";

        String htmlToRender = "auth.html";
        Long userId = (Long) session.getAttribute("userId");

        if (email.isEmpty()) {

            pageVariables.put("loginStatus", "login is required");

        } else if (password.isEmpty()) {

            pageVariables.put("loginStatus", "password is required");

        } else if (userId == null) {

            UserProfile profile = accountService.getUser(email);

            if (profile != null && profile.getPassword().equals(password)) {

                userId = accountService.getAndIncrementID();
                String key = String.valueOf(userId);

                assert key != null;
                session.setAttribute("userId", userId);
                accountService.addSessions(key, profile);

                pageVariables.put("loginStatus", "Login passed");
                htmlToRender = "authstatus.html";
            } else {
                pageVariables.put("loginStatus", "Wrong login/password");
            }
        } else {
            pageVariables.put("loginStatus", "You are alredy logged in");
        }

//        htmlToRender = "response.json";

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }
}
