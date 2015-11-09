package base;


import org.jetbrains.annotations.NotNull;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class UserProfile {

    @NotNull
    private final String login;
    @NotNull
    private final String password;
    @NotNull
    private final String email;

    public UserProfile(@NotNull String login,
                       @NotNull String password, @NotNull String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }


    @NotNull
    public String getLogin() {
        return login;
    }


    @NotNull
    public String getPassword() {
        return password;
    }


    @NotNull
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }

        UserProfile profile = (UserProfile)object;

        return (email.equals(profile.email)
             && login.equals(profile.login)
            && password.equals(profile.password));
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
