package main;

import accountservice.AccountServiceTh;
import frontendservice.FrontEnd;
import gamesocketmech.WebSocketGameServlet;
import gamesocketmech.WebSocketServiceImpl;
import base.DBService;
import base.GameMechanics;
import base.WebSocketService;
import db.DBServiceImpl;
import frontend.*;
import gamepad.GamepadServlet;
import gamepad.GamepadSocketServlet;
import mechanics.MechanicsParameters;
import messagesystem.MessageSystem;
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

        DBService dbService = new DBServiceImpl();

        final MessageSystem messageSystem = new MessageSystem();
        AccountServiceTh accountServiceTh = new AccountServiceTh(messageSystem, dbService);
        final Thread accountServiceThread = new Thread(accountServiceTh);
        accountServiceThread.setDaemon(true);
        accountServiceThread.setName("Account Service");
        final FrontEnd frontEnd = new FrontEnd(messageSystem);
        final Thread frontEndThread = new Thread(frontEnd);
        frontEndThread.setDaemon(true);
        frontEndThread.setName("FrontEnd");
        frontEndThread.start();
        accountServiceThread.start();

        WebSocketService webSocketService = new WebSocketServiceImpl();

        MechanicsParameters mechanicsParameters = (MechanicsParameters)ReadXMLFileSAX.readXML
                ("data/MechanicsParameters.xml");
        GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService, mechanicsParameters);

        final Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setDaemon(true);
        gameMechanicsThread.setName("GameMechanics");
        gameMechanicsThread.start();

        Servlet signin = new SignInServlet(frontEnd);
        Servlet signUp = new SignUpServlet(frontEnd);
        Servlet signOut = new SignOutServlet(frontEnd);
        Servlet getHighscore = new GetHighscoreServlet(dbService);
        Servlet addHighscore = new AddHighscoreServlet(frontEnd, dbService);

        Servlet admin = new AdminPageServlet(accountServiceTh, gameMechanics);
        Servlet gamepad = new GamepadServlet(frontEnd);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), Config.getInstance().getSignInUrl());
        context.addServlet(new ServletHolder(signUp), Config.getInstance().getSignUpUrl());
        context.addServlet(new ServletHolder(signOut), Config.getInstance().getSignOutUrl());
        context.addServlet(new ServletHolder(admin), Config.getInstance().getAdminUrl());
        context.addServlet(new ServletHolder(new WebSocketGameServlet(frontEnd,
                gameMechanics, webSocketService)), Config.getInstance().getGameplayUrl());
        context.addServlet(new ServletHolder(gamepad), Config.getInstance().getGamepadUrl());
        context.addServlet(new ServletHolder(new GamepadSocketServlet(gameMechanics)),
                Config.getInstance().getGamepadInputsUrl());
        context.addServlet(new ServletHolder(addHighscore), "/score");
        context.addServlet(new ServletHolder(getHighscore), "/top");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase(Config.getInstance().getResourceBase());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();

        server.join();
        gameMechanicsThread.join();
        frontEndThread.join();
        accountServiceThread.join();
    }
}
