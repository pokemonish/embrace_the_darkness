package main;

import base.AuthService;
import base.GameMechanics;
import base.WebSocketService;
import frontend.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.jetbrains.annotations.NotNull;


import mechanics.GameMechanicsImpl;
import admin.AdminPageServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import resources.Config;


import javax.servlet.Servlet;

/**
 * @author v.chibrikov
 */
public class Main {

    public static void main(@NotNull String[] args) throws Exception {

        Config config = new Config();

        int port = Integer.valueOf(config.getParameter("port"));

        String startMessage = "Starting at port: " + port + '\n' +
                "Currently running on " + System.getProperty("os.name") +
                ' ' + System.getProperty("os.version") + ' ' +
                System.getProperty("os.arch") + '\n';

        System.out.append(startMessage);

        AccountService accountService = new AccountService();
        AuthService authService = new AuthServiceImpl();

        WebSocketService webSocketService = new WebSocketServiceImpl();
        GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService);

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet postName = new PostNameServlet(authService);
        Servlet admin = new AdminPageServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), config.getParameter("sign_in_url"));
        context.addServlet(new ServletHolder(signUp), config.getParameter("sign_up_url"));
        context.addServlet(new ServletHolder(signOut), config.getParameter("sign_out_url"));
        context.addServlet(new ServletHolder(admin), config.getParameter("admin_url"));
        context.addServlet(new ServletHolder(postName), config.getParameter("post_name_url"));
        context.addServlet(new ServletHolder(new WebSocketGameServlet(authService, gameMechanics, webSocketService)), "/gameplay");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase(config.getParameter("resource_base"));

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();

        gameMechanics.run();
    }
}
