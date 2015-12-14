package main;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import templater.PageGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by fatman on 26/09/15.
 */
public class ResponseHandler {
    public static void respondWithJSON(@NotNull HttpServletResponse response,
                                       @NotNull JsonObject jsonResponse) {

        respondWithJSONAndStatus(response, jsonResponse, HttpServletResponse.SC_OK);
    }

    public static void respondWithJSONAndStatus(@NotNull HttpServletResponse response,
                                         @NotNull JsonObject jsonResponse, int status) {
        try (PrintWriter writer = response.getWriter()) {
            if (writer != null) {
                writer.println(jsonResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setStatus(status);
    }

    public static void drawPage(@NotNull HttpServletResponse response,
                                @NotNull String pageName,
                                @NotNull Map<String, Object> pageVariables) throws IOException {

        String pageToShow = PageGenerator.getPage(pageName, pageVariables);
        if (!pageToShow.isEmpty()) {
            try (PrintWriter writer = response.getWriter()) {
                if (writer != null) {
                    writer.println(pageToShow);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
