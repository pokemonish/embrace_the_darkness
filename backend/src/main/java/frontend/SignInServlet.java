package frontend;

import main.AccountService;
import main.ResponseHandler;
import base.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import utils.JsonRequestParser;

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
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        JSONObject jsonResponse = new JSONObject();

        String sessionId = session.getId();

        JSONObject jsonObject = JsonRequestParser.parse(request);

        Object requestEmail = jsonObject.get("email");
        Object requestPassword = jsonObject.get("password");

        String email = requestEmail == null ? "" : requestEmail.toString();
        String password = requestPassword == null ? "" : requestPassword.toString();

        System.out.append(email).append('\n').append(password);

        if (email.isEmpty()) {
            jsonResponse.put("Status", "login is required");
        } else if (password.isEmpty()) {
            jsonResponse.put("Status", "password is required");
        } else if (accountService.getSessions(sessionId) == null) {
            UserProfile profile = accountService.getUser(email);

            if (profile != null && profile.getPassword().equals(password)) {

                assert sessionId != null;
                accountService.addSessions(sessionId, profile);

                jsonResponse.put("Status", "Login passed");
            } else {
                jsonResponse.put("Status", "Wrong login/password");
            }
        } else {
            jsonResponse.put("Status", "You are alredy logged in");
        }

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
