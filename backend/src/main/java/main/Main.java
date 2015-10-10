package main;

import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.jetbrains.annotations.NotNull;


import admin.AdminPageServlet;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import frontend.SignOutServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jetbrains.annotations.NotNull;


import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 */
public class Main {

    public static void main(@NotNull String[] args) throws Exception {

        if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);
        }

        String portString = args[0];

        assert portString != null;
        Integer port = Integer.valueOf(portString);

        String startMessage = "Starting at port: " + String.valueOf(port) + '\n';
        System.out.append(startMessage);

        AccountService accountService = new AccountService();

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet admin = new AdminPageServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(signOut), "/api/v1/auth/signout");
        context.addServlet(new ServletHolder(admin), "/admin");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        assert port != null;
        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
