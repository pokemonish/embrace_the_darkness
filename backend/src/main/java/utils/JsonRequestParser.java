package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by fatman on 03/11/15.
 */
public class JsonRequestParser {
    public static JsonObject parse(@NotNull HttpServletRequest request) throws IOException {
        StringBuilder jb = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject parsedRequest;

        try {
            parsedRequest = new Gson().fromJson(jb.toString(), JsonObject.class);

        } catch (JsonSyntaxException ignore) {
            throw new IOException("Error parsing JSON request string");
        }

        return parsedRequest;
    }
}
