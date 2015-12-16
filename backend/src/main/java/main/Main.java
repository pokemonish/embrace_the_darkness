package main;

import game_socket_mechanics.WebSocketGameServlet;
import game_socket_mechanics.WebSocketServiceImpl;
import base.DBService;
import base.GameMechanics;
import base.WebSocketService;
import db.DBServiceImpl;
import frontend.*;
import gamepad.GamepadServlet;
import gamepad.GamepadSocketServlet;
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

        WebSocketService webSocketService = new WebSocketServiceImpl();

        MechanicsParameters mechanicsParameters = (MechanicsParameters)ReadXMLFileSAX.readXML
                ("data/MechanicsParameters.xml");
        GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService, mechanicsParameters);

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet signOut = new SignOutServlet(accountService);

        Servlet admin = new AdminPageServlet(accountService, gameMechanics);
        Servlet gamepad = new GamepadServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), Config.getInstance().getSignInUrl());
        context.addServlet(new ServletHolder(signUp), Config.getInstance().getSignUpUrl());
        context.addServlet(new ServletHolder(signOut), Config.getInstance().getSignOutUrl());
        context.addServlet(new ServletHolder(admin), Config.getInstance().getAdminUrl());
        context.addServlet(new ServletHolder(new WebSocketGameServlet(accountService,
                gameMechanics, webSocketService)), Config.getInstance().getGameplayUrl());
        context.addServlet(new ServletHolder(gamepad), Config.getInstance().getGamepadUrl());
        context.addServlet(new ServletHolder(new GamepadSocketServlet(gameMechanics)),
                            Config.getInstance().getGamepadInputsUrl());

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
