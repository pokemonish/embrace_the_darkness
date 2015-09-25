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
 * Created by v.chibrikov on 13.09.2014.
 */
public class SignUpServlet extends HttpServlet {

    @NotNull
    private AccountService accountService;

    public SignUpServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        HttpSession session = request.getSession();

        Long userId = (Long) session.getAttribute("userId");
        String htmlToRender = "signup.html";

        if (userId == null) {
            pageVariables.put("signUpStatus", "");
        } else {

            UserProfile profile = accountService.getSessions(userId.toString());

            if (profile != null) {

                String name = profile.getLogin();

                pageVariables.put("signUpStatus", "Hi, " + name + ", you are logged in.");
            } else {
                pageVariables.put("signUpStatus", "Profile not found");
            }
            htmlToRender = "signupstatus.html";
        }

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("email");
        name = name != null ? name : "";

        String password = request.getParameter("password");
        password = password != null ? password : "";

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

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }
}
