package db.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.DBException;
import db.executor.TExecutor;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fatman on 17/12/15.
 */
public class HighscoresDAO {
    TExecutor executor;

    public HighscoresDAO(TExecutor executor) {
        this.executor = executor;
    }

    private static final String GET_HIGHSCORES_LIMIT =
            "SELECT user_name, highscore FROM highscores ORDER BY highscore DESC LIMIT ";
    private static final String ADD_HIGH_SCORE =
            "INSERT INTO highscores (user_name, highscore) VALUES (\"%s\", \"%s\")";

    @Nullable
    public JsonArray getHighscoresLimit(int limit) throws DBException {
        String query = GET_HIGHSCORES_LIMIT + limit;
        return executor.execQuery(query,
            result -> {
                JsonArray array = new JsonArray();
                while (result.next()) {
                    JsonObject object = new JsonObject();
                    object.addProperty("name", result.getString(1));
                    object.addProperty("score", String.valueOf(result.getInt(2)));
                    array.add(object);
                }
                return array;
            }
        );
    }

    public void addHighScore(String userName, int highscore) throws DBException {
        String query = String.format(ADD_HIGH_SCORE, userName, highscore);
        executor.execUpdate(query);
    }
}
