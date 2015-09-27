package frontend;

import main.AccountService;
import main.UserProfile;
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
 * @author v.chibrikov
 */
public class LogOutServlet extends HttpServlet {

    private AccountService accountService;

    public LogOutServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String sessionId = session.getId();

        UserProfile profile = accountService.getSessions(sessionId);

        Map<String, Object> pageVariables = new HashMap<>();
        if (profile != null) {
//            accountService.deleteSession(sessionId);
            pageVariables.put("status", "Logout done!");
        } else {
            pageVariables.put("status", "Oops, you aren`t login :c");
        }

        response.setStatus(HttpServletResponse.SC_OK);


        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
    }

}
