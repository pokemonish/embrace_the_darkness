package frontend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import main.AccountService;
import base.UserProfile;
import org.jetbrains.annotations.NotNull;
import utils.JsonRequestParser;
import main.ResponseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by v.chibrikov on 13.09.2014.
 */


public class SignUpServlet extends HttpServlet {
    @NotNull
    private final AccountService accountService;

    public SignUpServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        JsonObject jsonResponse = new JsonObject();

        JsonObject requestJsonData;

        try {
            requestJsonData = JsonRequestParser.parse(request);
        } catch (IOException e) {
            jsonResponse.addProperty("Status", "Request is invalid. Can't parse json.");
            ResponseHandler.respondWithJSONAndStatus(response, jsonResponse,
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        JsonElement requestEmail = requestJsonData.get("email");
        JsonElement requestPassword = requestJsonData.get("password");

        String name = requestEmail == null ? "" : requestEmail.getAsString();
        String password = requestPassword == null ? "" : requestPassword.getAsString();

        if (name.isEmpty()) {
            jsonResponse.addProperty("Status", "login is required");
        } else if (password.isEmpty()) {
            jsonResponse.addProperty("Status", "password is required");
        } else if (accountService.addUser(name, new UserProfile(name, password, ""))) {

            jsonResponse.addProperty("Status", "New user created");

        } else {
            StringBuilder duplicateParametersMessage = new StringBuilder();
            duplicateParametersMessage
            .append("User with name: ")
            .append(name)
            .append(" already exists");

            System.out.append(duplicateParametersMessage.toString()).append('\n');

            jsonResponse.addProperty("Status", duplicateParametersMessage.toString());

            System.out.append(jsonResponse.get("Status").toString());
        }

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
