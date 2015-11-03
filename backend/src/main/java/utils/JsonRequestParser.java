package utils;

import org.eclipse.jetty.client.HttpRequest;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by fatman on 03/11/15.
 */
public class JsonRequestParser {
    public static JSONObject parse(@NotNull HttpServletRequest request) throws IOException {
        StringBuilder jb = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            String line = null;
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) JSONValue.parse(jb.toString());

        } catch (RuntimeException e) {
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        }

        return jsonObject;
    }
}
