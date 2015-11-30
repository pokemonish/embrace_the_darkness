package annotations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fatman on 05/11/15.
 */


public class MethodContainer {
    @Nullable
    public String getNotAnnotatedNullableString() {
        if (System.currentTimeMillis() % 10 == 0) {
            return null;
        }
        return "returns null, but annotated as @NotNull";
    }

    @Nullable
    public String getFakeNotNullString() {
        if (System.currentTimeMillis() % 10 == 0) {
            return null;
        }
        return "returns null, but annotated as @NotNull";
    }

    @Nullable
    public String getNullableString() {
        if (System.currentTimeMillis() % 10 == 0) {
            return null;
        }
        return "not null, but can be null";
    }


    @NotNull
    public String getNotNullString() {
        if (System.currentTimeMillis() % 10 == 0) {
            return "not null, even";
        }
        return "not null, odd";
    }
}

