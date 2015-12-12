package main;

import base.AuthService;
import base.DBService;
import base.GameMechanics;
import base.WebSocketService;
import db.DBServiceImpl;
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

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(@NotNull String[] args) throws Exception {

        int port = Config.getInstance().getPort();

        String startMessage = "Starting at port: " + port + '\n' +
                "Currently running on " + System.getProperty("os.name") +
                ' ' + System.getProperty("os.version") + ' ' +
                System.getProperty("os.arch") + '\n';

        Logger.getAnonymousLogger().log(new LogRecord(Level.INFO, startMessage));

        DBService dBservice = new DBServiceImpl();

        AccountService accountService = new AccountService(dBservice);
        AuthService authService = new AuthServiceImpl();

        WebSocketService webSocketService = new WebSocketServiceImpl();

        MechanicsParameters mechanicsParameters = (MechanicsParameters)ReadXMLFileSAX.readXML
                ("data/MechanicsParameters.xml");
        GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService, mechanicsParameters);

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);
        Servlet postName = new PostNameServlet(authService);
        Servlet admin = new AdminPageServlet(accountService, gameMechanics);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), Config.getInstance().getSignInUrl());
        context.addServlet(new ServletHolder(signUp), Config.getInstance().getSignUpUrl());
        context.addServlet(new ServletHolder(signOut), Config.getInstance().getSignOutUrl());
        context.addServlet(new ServletHolder(admin), Config.getInstance().getAdminUrl());
        context.addServlet(new ServletHolder(postName), Config.getInstance().getPostNameUrl());
        context.addServlet(new ServletHolder(new WebSocketGameServlet(authService,
                gameMechanics, webSocketService)), Config.getInstance().getGameplayUrl());

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase(Config.getInstance().getResourceBase());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();

        gameMechanics.run();
    }
}
