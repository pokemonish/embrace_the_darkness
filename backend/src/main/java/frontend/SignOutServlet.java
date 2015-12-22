package frontend;

import com.google.gson.JsonObject;
import frontendservice.FrontEnd;
import main.ResponseHandler;
import messagesystem.MyTimeOutException;
import messagesystem.TimeOutHelper;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by fatman on 20/09/15.
 */


public class SignOutServlet extends HttpServlet {
    @NotNull
    private final FrontEnd frontEnd;

    public SignOutServlet(@NotNull FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
    }



    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession();

        String userId = String.valueOf(session.getAttribute("userId"));
        JsonObject jsonResponse = new JsonObject();

        if (frontEnd.isAuthenticated(userId) == null) {
            jsonResponse.addProperty("Status", "You are alredy signed out");
        } else {
            try {
                new TimeOutHelper().doInTime(() -> {
                    frontEnd.exit(userId);
                    while (frontEnd.getAuthStatus(userId) != null) ;
                    jsonResponse.addProperty("Status", "Signed out successfully!\nSee you soon!");
                });
            } catch (MyTimeOutException e) {
                jsonResponse.addProperty("Status", "Request took too long");
            }
        }

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
