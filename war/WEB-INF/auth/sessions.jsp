<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.SessionsBundle" var="sessionsBundle" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_sessions" bundle="${sessionsBundle}" /></title>
        <!-- Bootstrap -->
        <link rel="stylesheet" href="/css/bootstrap.min.css" />
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script type="text/javascript" src="/js/jquery.min.js"></script>
        <script type="text/javascript" src="/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="/css/styles.css" />

        <script type="text/javascript">
$(document).ready(function() {
});
        </script>
    </head>
    <body>
<%@ include file="/WEB-INF/jspf/auth/top-navbar.jspf" %>
<%@ include file="/WEB-INF/jspf/auth/permissions-navbar.jspf" %>

        <div class="page-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-12 col-sm-offset-2 col-sm-8">
                        <div class="row"><h3 class="col-xs-12 col-sm-12"><fmt:message key="page_header_sessions" bundle="${sessionsBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th><fmt:message key="table_header_session_id" bundle="${sessionsBundle}" /></th>
                            <th><fmt:message key="table_header_date_signed_in" bundle="${sessionsBundle}" /></th>
                            <th><fmt:message key="table_header_last_session_check" bundle="${sessionsBundle}" /></th>
                            <th><fmt:message key="table_header_session_timeout" bundle="${sessionsBundle}" /></th>
                        </tr>
                        </thead>
                        <tbody>
<fmt:setTimeZone value="${user.dateFormatId}" />
<c:forEach var="session" items="${sessions}" varStatus="loopCounter" >
                        <tr>
                            <td><c:out value='${fn:substring(session.sessionId, 0, 32)}' />...</td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${session.dateSignedIn}" /></td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${session.lastSessionCheck}" /></td>
                            <td><c:out value='${session.sessionTimeout}' /></td>
                        </tr>
</c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        </main>
    </body>
</html>
