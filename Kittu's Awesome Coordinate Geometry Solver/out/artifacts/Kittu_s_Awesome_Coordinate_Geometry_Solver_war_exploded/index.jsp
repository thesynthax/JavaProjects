<%--
  Created by IntelliJ IDEA.
  User: TheSynthax
  Date: 15-Nov-19
  Time: 10:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="j" %>
<html>
  <head>
    <title>Home Page</title>
  </head>
  <body>
  Kittu's Awesome Coordinate Geometry Solver

  <form action="test.jsp">
      <button>
        GO
      </button>
  </form>

  <%
      String message = "IT WORKS YEAHHHHHHH!";
      session.setAttribute("message", message);
  %>

  </body>
</html>
