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

        String sessionId = session.getId();
        String htmlToRender = "signup.html";

        UserProfile profile = accountService.getSessions(sessionId);

        if (profile != null) {
            htmlToRender = "signupstatus.html";

            String name = profile.getLogin();
            pageVariables.put("signUpStatus", "Hi, " + name + ", you are logged in.");
        } else {
            pageVariables.put("signUpStatus", "");
        }

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("email");

        String password = request.getParameter("password");

        String htmlToRender = "signup.html";

        Map<String, Object> pageVariables = new HashMap<>();

        if (name == null) {
            pageVariables.put("signUpStatus", "login is required");
        } else if (password == null) {
            pageVariables.put("signUpStatus", "password is required");
        } else if (accountService.addUser(name, new UserProfile(name, password, ""))) {

            //pageVariables.put("signUpStatus", "New user created\n");

            //temporary, just for convenience
            final String SIGN_IN_BUTTON = "<form action=\"/api/v1/auth/signin\">" +
                    "<input type=\"submit\" value=\"Sign in\">\n" + "</form>";
            pageVariables.put("signUpStatus", "New user created\n" + SIGN_IN_BUTTON);

            htmlToRender = "signupstatus.html";
        } else {
            pageVariables.put("signUpStatus", "User with name: " + name + " already exists");
        }

        ResponseHandler.drawPage(response, htmlToRender, pageVariables);
    }
}
