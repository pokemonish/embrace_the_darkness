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

        Long userId = (Long) session.getAttribute("userId");
        String htmlToRender = "auth.html";

        if (userId == null) {
            pageVariables.put("loginStatus", "");
        } else {
            UserProfile profile = accountService.getSessions(userId.toString());

            if (profile != null) {

                String name = profile.getLogin();
                pageVariables.put("loginStatus", "Hi, " + name + ", you are logged in.");
            } else {
                pageVariables.put("loginStatus", "Profile not found");
            }
            htmlToRender = "authstatus.html";
        }

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        email = email != null ? email : "";
        password = password != null ? password : "";

        HttpSession session = request.getSession();
        Map<String, Object> pageVariables = new HashMap<>();

        Long userId = (Long) session.getAttribute("userId");
        String htmlToRender = "auth.html";

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

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }
}
