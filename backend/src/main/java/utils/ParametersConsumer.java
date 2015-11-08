package utils;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by fatman on 08/11/15.
 */
public class ParametersConsumer implements BiConsumer{

    private Map<String, String> parameters = new HashMap<>();

    public Map getParameters() {
        return parameters;
    }

    @Override
    public void accept(Object o, Object o2) {
        if (o.getClass() != String.class ||
                o2.getClass() != String.class) return;

        String s = (String) o;
        String s2 = (String) o2;
        parameters.put(s, s2);
    }

    @Override
    @Nullable
    public BiConsumer andThen(BiConsumer after) {
        return null;
    }
}
