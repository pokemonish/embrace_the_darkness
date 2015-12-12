package admin;
import base.GameMechanics;
import main.AccountServiceException;
import utils.TimeHelper;
import main.AccountService;
import main.ResponseHandler;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminPageServlet extends HttpServlet {

    @NotNull
    private final AccountService accountService;
    @NotNull
    private final GameMechanics gameMechanics;

    public AdminPageServlet(@NotNull AccountService accountService,
                            @NotNull GameMechanics gameMechanics) {
        this.accountService = accountService;
        this.gameMechanics = gameMechanics;
    }


    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();


        String timeGameString = request.getParameter("stopgame");
        if (timeGameString != null) {
            Integer timeMS = Integer.valueOf(timeGameString);
            System.out.print("Game mechanics will be stopped after: " + timeMS + " ms");
            TimeHelper.sleep(timeMS);
            gameMechanics.setIsActive(false);
        }

        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            Integer timeMS = Integer.valueOf(timeString);
            System.out.println("Server will be down after: "+ timeMS + " ms");
            TimeHelper.sleep(timeMS);
            System.out.println("Shutdown");
            System.exit(0);
        }

        try {
            pageVariables.put("usersTotal", accountService.getUsersQuantity());
        } catch (AccountServiceException e) {
            pageVariables.put("usersTotal", "Unavailable");
        }


        pageVariables.put("usersSignedIn", accountService.getSessionsQuantity());
        pageVariables.put("status", "run");

        ResponseHandler.drawPage(response, "admin.html", pageVariables);
    }
}
