package admin;
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
    private AccountService accountService;

    public AdminPageServlet(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public void doGet(@NotNull HttpServletRequest request,
                      @NotNull HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            Integer timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: "+ timeMS + " ms");
            TimeHelper.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }
        pageVariables.put("usersTotal", accountService.getUsersQuantity());
        pageVariables.put("usersSignedIn", accountService.getSessionsQuantity());
        pageVariables.put("status", "run");

        ResponseHandler.drawPage(response, "admin.html", pageVariables);
    }
}
