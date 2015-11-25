package frontend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

import utils.JsonRequestParser;

/**
 * @author v.chibrikov
 */


public class SignInServlet extends HttpServlet {

    @NotNull
    private final AccountService accountService;

    public SignInServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }
    

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        JsonObject jsonResponse = new JsonObject();

        String sessionId = session.getId();

        JsonObject requestData = JsonRequestParser.parse(request);

        JsonElement requestEmail = requestData.get("email");
        JsonElement requestPassword = requestData.get("password");

        String email = requestEmail == null ? "" : requestEmail.getAsString();
        String password = requestPassword == null ? "" : requestPassword.getAsString();

        System.out.append(email).append('\n').append(password);

        if (email.isEmpty()) {
            jsonResponse.addProperty("Status", "login is required");
        } else if (password.isEmpty()) {
            jsonResponse.addProperty("Status", "password is required");
        } else if (accountService.getSessions(sessionId) == null) {
            UserProfile profile = accountService.getUser(email);

            if (profile != null && profile.getPassword().equals(password)) {

                assert sessionId != null;
                accountService.addSessions(sessionId, profile);

                jsonResponse.addProperty("Status", "Login passed");
            } else {
                jsonResponse.addProperty("Status", "Wrong login/password");
            }
        } else {
            jsonResponse.addProperty("Status", "You are alredy logged in");
        }

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
