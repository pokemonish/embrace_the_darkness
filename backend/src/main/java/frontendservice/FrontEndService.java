package frontendservice;

import accountservice.Statuses;
import accountservice.UserProfile;

/**
 * @author e.shubin
 */
public interface FrontEndService {
    void register(UserProfile profile);

    Statuses.SignUpStatuses getRegistrationResult(String name);

    String authenticate(String name, String password);

    UserProfile isAuthenticated(String sessionId);

    void exit(String userId);

    void exited(String userId);
}
