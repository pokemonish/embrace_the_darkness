package frontend;

import main.AccountService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fatman on 20/09/15.
 */
public class SignOutServlet extends HttpServlet {
    private AccountService accountService;

    public SignOutServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, Object> pageVariables = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null && accountService.deleteSessions(userId.toString())) {
            session.removeAttribute("userId");
            pageVariables.put("signOutStatus", "Signed out successfully!\nSee you soon!");
        } else {
            pageVariables.put("signOutStatus", "You are alredy signed out");
        }

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(PageGenerator.getPage("signoutstatus.html", pageVariables));

    }
}
