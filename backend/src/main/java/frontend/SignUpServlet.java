package frontend;

import main.AccountService;
import base.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
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
    private AccountService accountService;

    public SignUpServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {


        JSONObject jsonObject = JsonRequestParser.parse(request);

        Object requestEmail = jsonObject.get("email");
        Object requestPassword = jsonObject.get("password");

        String  name = requestEmail == null ? "" : requestEmail.toString();
        String password = requestPassword == null ? "" : requestPassword.toString();

        JSONObject jsonResponse = new JSONObject();

        if (name.isEmpty()) {
            jsonResponse.put("Status", "login is required");
        } else if (password.isEmpty()) {
            jsonResponse.put("Status", "password is required");
        } else if (accountService.addUser(name, new UserProfile(name, password, ""))) {

            jsonResponse.put("Status", "New user created\n");

        } else {
            jsonResponse.put("Status", "User with name: " + name + " already exists");
        }

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
