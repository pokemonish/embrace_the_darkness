package main;


import frontend.SignInServlet;
import frontend.SignUpServlet;
import frontend.LogOutServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author v.chibrikov
 */
public class Main {

    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {

        AccountService accountService = new AccountService();

        SignInServlet signIn = new SignInServlet(accountService);
        SignUpServlet signUp = new SignUpServlet(accountService);
        LogOutServlet logout = new LogOutServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(signIn), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(logout), "/api/v1/auth/logout");

        Server server = new Server(PORT);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
