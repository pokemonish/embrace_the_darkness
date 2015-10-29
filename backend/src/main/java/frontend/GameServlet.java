package frontend;

import base.AuthService;
import base.GameMechanics;
import main.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class GameServlet extends HttpServlet {

    private GameMechanics gameMechanics;
    private AuthService authService;

    public GameServlet(GameMechanics gameMechanics, AuthService authService) {
        this.gameMechanics = gameMechanics;
        this.authService = authService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
