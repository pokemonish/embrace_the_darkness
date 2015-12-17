package frontend;

import base.DBService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.DBException;
import main.ResponseHandler;
import utils.JsonRequestParser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fatman on 17/12/15.
 */
public class GetHighscoreServlet extends HttpServlet {
    private DBService dbService;

    public GetHighscoreServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        JsonObject requestData;
        JsonObject jsonResponse = new JsonObject();

        try {
            requestData = JsonRequestParser.parse(request);
        } catch (IOException e) {
            jsonResponse.addProperty("Status", "Request is invalid. Can't parse json.");
            ResponseHandler.respondWithJSONAndStatus(response, jsonResponse,
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!requestData.has("top")) {
            jsonResponse.addProperty("Status", "Request data doesn't have 'top' member in it.");
            ResponseHandler.respondWithJSONAndStatus(response, jsonResponse,
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int topScoresNumber;
        try {
            topScoresNumber = requestData.get("top").getAsInt();
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("Status", "'top' parameter should be int. It's " +
                    requestData.get("top") + " instead. " +
                    "Shame on you, people.");
            ResponseHandler.respondWithJSONAndStatus(response, jsonResponse,
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            JsonArray highscores = dbService.getHighscoreDAO().getHighscoresLimit(topScoresNumber);
            jsonResponse.add("highscores", highscores);
        } catch (DBException e) {
            e.printStackTrace();
            jsonResponse.addProperty("Status", "Error processing request. Please, try again later.");
            ResponseHandler.respondWithJSON(response, jsonResponse);
            return;
        }

        ResponseHandler.respondWithJSON(response, jsonResponse);
    }
}
