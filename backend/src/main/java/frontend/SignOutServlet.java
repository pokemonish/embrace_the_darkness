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

/**
 * Created by fatman on 20/09/15.
 */
public class SignOutServlet extends HttpServlet {
    @NotNull
    private AccountService accountService;

    public SignOutServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }



    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        JSONObject jsonResponse = new JSONObject();
        String sessionId = session.getId();

        UserProfile profile = accountService.getSessions(sessionId);

        if (profile != null) {
            accountService.deleteSessions(sessionId);
            jsonResponse.put("Status", "Signed out successfully!\nSee you soon!");
        } else {
            jsonResponse.put("Status", "You are alredy signed out");
        }

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
