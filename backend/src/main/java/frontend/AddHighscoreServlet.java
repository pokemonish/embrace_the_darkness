package frontend;

import base.DBService;
import base.UserProfile;
import com.google.gson.JsonObject;
import db.DBException;
import main.AccountService;
import main.ResponseHandler;
import utils.JsonRequestParser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fatman on 17/12/15.
 */
public class AddHighscoreServlet extends HttpServlet {
    private AccountService accountService;
    private DBService dbService;

    public AddHighscoreServlet(AccountService accountService, DBService dbService) {
        this.accountService = accountService;
        this.dbService = dbService;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        String id = String.valueOf(request.getSession().getAttribute("userId"));
        JsonObject jsonResponse = new JsonObject();
        UserProfile userProfile = accountService.getSessions(id);
        if (id == null || userProfile == null) {
            jsonResponse.addProperty("Status", "You're not logged in.");
            ResponseHandler.respondWithJSONAndStatus(response, jsonResponse,
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        JsonObject requestData;

        try {
            requestData = JsonRequestParser.parse(request);
        } catch (IOException e) {
            jsonResponse.addProperty("Status", "Request is invalid. Can't parse json.");
            ResponseHandler.respondWithJSONAndStatus(response, jsonResponse,
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!requestData.has("score")) {
            jsonResponse.addProperty("Status", "Request data doesn't have 'score' member in it.");
            ResponseHandler.respondWithJSONAndStatus(response, jsonResponse,
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            dbService.getHighscoreDAO().addHighScore(userProfile.getLogin(), requestData.get("score").getAsInt());
        } catch (DBException e) {
            e.printStackTrace();
            jsonResponse.addProperty("Status", "Error processing request. Please, try again later.");
            ResponseHandler.respondWithJSON(response, jsonResponse);
            return;
        }
        jsonResponse.addProperty("Status", "Success");

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
