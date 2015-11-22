package main;

import base.AuthService;
import base.GameMechanics;
import base.WebSocketService;
import frontend.*;
import mechanics.MechanicsParameters;
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
import resources.ReadXMLFileSAX;


import javax.servlet.Servlet;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author v.chibrikov
 */
@SuppressWarnings("OverlyBroadThrowsClause")
public class Main {

    public static void main(@NotNull String[] args) throws Exception {

        Config config = new Config();

        int port = Integer.valueOf(config.getParameter(Config.PORT));

        String startMessage = "Starting at port: " + port + '\n' +
                "Currently running on " + System.getProperty("os.name") +
                ' ' + System.getProperty("os.version") + ' ' +
                System.getProperty("os.arch") + '\n';

        Logger.getAnonymousLogger().log(new LogRecord(Level.INFO, startMessage));

        AccountService accountService = new AccountService();
        AuthService authService = new AuthServiceImpl();

        WebSocketService webSocketService = new WebSocketServiceImpl();

        MechanicsParameters mechanicsParameters = (MechanicsParameters)ReadXMLFileSAX.readXML("data/MechanicsParameters.xml");
        GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService, mechanicsParameters);

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet postName = new PostNameServlet(authService);
        Servlet admin = new AdminPageServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), config.getParameter(Config.SIGN_IN_URL));
        context.addServlet(new ServletHolder(signUp), config.getParameter(Config.SIGN_UP_URL));
        context.addServlet(new ServletHolder(signOut), config.getParameter(Config.SIGN_OUT_URL));
        context.addServlet(new ServletHolder(admin), config.getParameter(Config.ADMIN_URL));
        context.addServlet(new ServletHolder(postName), config.getParameter(Config.POST_NAME_URL));
        context.addServlet(new ServletHolder(new WebSocketGameServlet(authService,
                            gameMechanics, webSocketService)), config.getParameter(Config.GAMEPLAY_URL));

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase(config.getParameter(Config.RESOURCE_BASE));

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();

        gameMechanics.run();
    }
}
