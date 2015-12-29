package frontend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import frontendservice.FrontEnd;
import main.ResponseHandler;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import static accountservice.Statuses.*;

import utils.JsonRequestParser;

/**
 * @author v.chibrikov
 */


public class SignInServlet extends HttpServlet {

    @NotNull
    private final FrontEnd frontEnd;

    public SignInServlet(@NotNull FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
    }


    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        JsonObject jsonResponse = new JsonObject();

        String userId = String.valueOf(session.getAttribute("userId"));

        JsonObject requestData;

        try {
            requestData = JsonRequestParser.parse(request);
        } catch (IOException e) {
            jsonResponse.addProperty("Status", "Request is invalid. Can't parse json.");
            ResponseHandler.respondWithJSONAndStatus(response, jsonResponse,
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        JsonElement requestEmail = requestData.get("email");
        JsonElement requestPassword = requestData.get("password");

        String email = requestEmail == null ? "" : requestEmail.getAsString();
        String password = requestPassword == null ? "" : requestPassword.getAsString();

        if (email.isEmpty()) {
            jsonResponse.addProperty("Status", "login is required");
        } else if (password.isEmpty()) {
            jsonResponse.addProperty("Status", "password is required");
        } else if (frontEnd.getAuthStatus(String.valueOf(userId)) == AuthorizationStates.WAITING_FOR_AUTHORIZATION ||
                frontEnd.getAuthStatus(String.valueOf(userId)) == AuthorizationStates.WAITING_FOR_REGISTRATION) {
            jsonResponse.addProperty("Status", "Your request for authorization is processing, please, wait.");
        } else if (frontEnd.getAuthStatus(String.valueOf(userId)) != AuthorizationStates.AUTHORIZED) {
            final String[] id = new String[1];
                id[0] = frontEnd.authenticate(email, password);
            switch (frontEnd.getAuthStatus(id[0])) {
                case AUTHORIZED:
                    session.setAttribute("userId", id[0]);
                    jsonResponse.addProperty("Status", "Login passed");
                    break;
                case ERROR:
                    jsonResponse.addProperty("Status", "Error occured, please, try again later.");
                    break;
                case WRONG_AUTHORIZATION_DATA:
                    jsonResponse.addProperty("Status", "Wrong login/password");
                    break;
                case WAITING_FOR_AUTHORIZATION:
                    jsonResponse.addProperty("Status", "Your request for authorization is processing, please, wait.");
                    break;
                case WAITING_FOR_REGISTRATION:
                    jsonResponse.addProperty("Status", "Your request for authorization is processing, please, wait.");
                    break;
            }
        } else {
            jsonResponse.addProperty("Status", "You are already logged in");
        }

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
