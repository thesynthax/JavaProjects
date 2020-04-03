package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test2")
public class Test2Servlet extends HttpServlet
{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        HttpSession session = req.getSession();
        String msg = (String) session.getAttribute("msg");
        PrintWriter out = res.getWriter();
        out.println(msg);

    }
}
