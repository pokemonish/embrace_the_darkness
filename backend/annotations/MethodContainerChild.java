package annotations;

import org.jetbrains.annotations.Nullable;

/**
 * Created by fatman on 05/11/15.
 */
public class MethodContainerChild extends MethodContainer {
    @Override
    @Nullable
    public String getNotNullString() {
        if (System.currentTimeMillis() % 10 == 0) {
            return null;
        }
        return "overriden can return null";
    }
}
