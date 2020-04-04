<%--
  Created by IntelliJ IDEA.
  User: TheSynthax
  Date: 15-Nov-19
  Time: 10:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="layout.jsp" %>
<html>
    <head>
        <title></title>
    </head>
    <body>
    <script>
        var x = window.innerWidth;
        
    </script>
        <div id="header2">
            <div id="header2bg">
                <x:forEach var="i" begin="1" end="11">
                    <div class="${"horzLine"} ${"a"}${i}"></div>
                </x:forEach>
                <x:forEach var="i" begin="1" end="43">
                    <div class="${"horzLine"} ${"vertLine"} ${"b"}${i}" style="top: calc(calc(-${i}*400px) + 25px); left: calc(${i-1}*35px)"></div>

                </x:forEach>
            </div>
        </div>
        <div id="footer">
        </div>
    </body>
</html>
