package webapp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class TestServlet extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        String msg = "IT WORKS BRO!";

       /* req.setAttribute("message", msg);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("test2");
        requestDispatcher.forward(req, res);*/

        res.sendRedirect("test2");
        HttpSession session = req.getSession();
        session.setAttribute("msg", msg);
    }
}
