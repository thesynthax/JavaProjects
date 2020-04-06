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
        <script src="static/js/header2bgGlow.js"></script>
    </head>
    <body>

        <div id="header2">
            <div id="header2bg">
                <div class="lines">
                    <x:forEach var="i" begin="1" end="11">
                        <div class="${"horzLine"} ${"a"}${i}"></div>
                    </x:forEach>
                    <x:forEach var="i" begin="1" end="43">
                        <%--<div class="${"horzLine"} ${"vertLine"} ${"b"}${i}" style="top: calc(calc(-${i}*400px) + 25px); left: calc(${i-1}*35px)"></div>--%>
                        <div class="${"horzLine"} ${"vertLine"} ${"b"}${i}"></div>
                    </x:forEach>
                </div>
            </div>
        </div>
        <div id="footer">
        </div>
        <script>
            function correctGeometry()
            {
                var header2 = document.getElementById("header2");
                var height = header2.clientHeight;
                var width = header2.clientWidth;
                var margin = 30;

                var horzLines = Math.round(height/margin) - 1;
                var vertLines = Math.round(width/35) - 1;

                var l = header2.children[0].children[0].children;
                for (var i = horzLines; i < horzLines + vertLines; i++) {
                    var j = l[i];
                    j.style.top = (-(i - horzLines + 1) * 400 + 35).toString();
                    j.style.left = ((i - horzLines) * 35).toString();
                    j.style.width = "3px";
                    j.style.opacity = "20%";
                }
                var hMid = l[Math.round(horzLines/2)-1];
                hMid.style.height = "5px";
                hMid.style.opacity = "100%";

                var vMid = l[horzLines + Math.round(vertLines/2)-1];
                vMid.style.width = "5px";
                vMid.style.opacity = "100%";
                for (var i = 4; i <= Math.round(vertLines/2); i += 4)
                {
                    var j = l[horzLines + Math.round(vertLines/2) + i - 1];
                    j.style.width = "4px";
                    j.style.opacity = "50%";
                }
                for (var i = 4; i <= Math.round(vertLines/2); i += 4)
                {
                    var j = l[horzLines + Math.round(vertLines/2) - i - 1];
                    j.style.width = "4px";
                    j.style.opacity = "50%";
                }
            }
            window.addEventListener("resize", correctGeometry);
            correctGeometry();
        </script>
    </body>
</html>
