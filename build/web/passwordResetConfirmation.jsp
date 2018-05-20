<%-- 
    Document   : index
    Created on : Jan 5, 2014, 4:43:34 PM
    Author     : simone
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Counsel To Success | Confirm Password</title>
        <%@include file="include/head.jsp" %>
    </head>
    <body>
        <div id="login-page" data-role="page">
            <div data-role="header">
                <a href="/" data-icon="home">Home</a>
                <h2>Healthcare System | Confirm Password</h2>
            </div>
            <div data-role="content">
                <c:if test="${isCorrect}">
                    <div class="ui-body ui-body-a ui-corner-all">
                        <p>
                            Please insert your new password
                        </p>
                    </div>
                    <form method="post" data-ajax="false" onsubmit="var c = $('#reset-password').val() == $('#reset-password-confirm').val();
                            if (!c)
                                alert('Passwords don\'t match');
                            return c">
                        <ul data-role="listview" data-inset="true">
                            <li data-role="fieldcontain">
                                <label for="reset-password">New password</label>
                                <input type="password" id="reset-password" name="password">
                            </li>
                            <li data-role="fieldcontain">
                                <label for="reset-password-confirm">Confirm new password</label>
                                <input type="password" id="reset-password-confirm" name="password-confirm">
                            </li>
                        </ul>
                        <button data-inline="true" type="submit" data-theme="b">Confirm</button>
                    </form>
                </c:if>
                <c:if test="${!isCorrect}">
                    <div class="ui-body ui-body-a ui-corner-all">
                        <p>
                            Your link has expired. Please retry  
                        </p>
                    </div>
                      <form method="post" data-ajax="false" >
                          <a href="/password-reset-request"><button data-inline="true" type="submit" data-theme="b">Retry</button></a>
                    </form>
                </c:if>
            </div>
        </div>
    </body>
</html>
