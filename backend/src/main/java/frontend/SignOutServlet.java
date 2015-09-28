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
        Map<String, Object> pageVariables = new HashMap<>();
        String sessionId = session.getId();

        UserProfile profile = accountService.getSessions(sessionId);

        if (profile != null) {
            accountService.deleteSessions(sessionId);
            pageVariables.put("signOutStatus", "Signed out successfully!\nSee you soon!");
        } else {
            pageVariables.put("signOutStatus", "You are alredy signed out");
        }

        ResponseHandler.drawPage(response, "signoutstatus.html", pageVariables);
    }
}
