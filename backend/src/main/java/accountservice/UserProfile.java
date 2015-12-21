package accountservice;


import org.jetbrains.annotations.NotNull;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class UserProfile {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfile profile = (UserProfile) o;

        return login.equals(profile.login) &&
                password.equals(profile.password) &&
                email.equals(profile.email);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    @NotNull
    private String login = "";
    @NotNull
    private String password = "";
    @NotNull
    private String email = "";

    public UserProfile() {}

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

    @SuppressWarnings("unused")
    @NotNull
    public String getEmail() {
        return email;
    }

}
