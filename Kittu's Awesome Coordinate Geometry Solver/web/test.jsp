<%--
  Created by IntelliJ IDEA.
  User: TheSynthax
  Date: 02-Apr-20
  Time: 9:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        out.print(session.getAttribute("message"));
    %>
</body>
</html>
