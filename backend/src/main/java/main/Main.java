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
        AuthService authService = new AuthServiceImpl();

        WebSocketService webSocketService = new WebSocketServiceImpl();
        GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService);

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet postName = new PostNameServlet(authService);
        Servlet admin = new AdminPageServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(signOut), "/api/v1/auth/signout");
        context.addServlet(new ServletHolder(admin), "/admin");
        context.addServlet(new ServletHolder(postName), "/postName");
        context.addServlet(new ServletHolder(new WebSocketGameServlet(authService, gameMechanics, webSocketService)), "/gameplay");
        //context.addServlet(new ServletHolder(new GameServlet(gameMechanics, authService)), "/game");


        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();

        gameMechanics.run();
    }
}
