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
    public void doPost(@NotNull HttpServletRequest request,
                       @NotNull HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("email");

        String password = request.getParameter("password");

        JSONObject jsonResponse = new JSONObject();

        if (name == null) {
            jsonResponse.put("Status", "login is required");
        } else if (password == null) {
            jsonResponse.put("Status", "password is required");
        } else if (accountService.addUser(name, new UserProfile(name, password, ""))) {

            jsonResponse.put("Status", "New user created\n");

        } else {
            jsonResponse.put("Status", "User with name: " + name + " already exists");
        }

        ResponseHandler.drawPage(response, jsonResponse);
    }
}
