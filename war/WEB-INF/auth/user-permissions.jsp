<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.UserPermissionsBundle" var="userPermissions" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_user_permissions" bundle="${userPermissions}" /></title>
        <!-- Bootstrap -->
        <link rel="stylesheet" href="/css/bootstrap.min.css" />
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script type="text/javascript" src="/js/jquery.min.js"></script>
        <script type="text/javascript" src="/js/jquery.validate.min.js"></script>
        <script type="text/javascript" src="/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="/css/styles.css" />

<c:if test="${langCookie.value ne jf:defaultLanguage()}">
        <!-- Link messages file for localized validation. -->
        <script type="text/javascript" src="/js/messages_${langCookie.value}.min.js"></script>
</c:if>

        <script type="text/javascript">
function inputChangeHandler(event) {
    // settingsFormValidator.element(event.target);
}

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
                    <div class="col-xs-12 col-sm-offset-1 col-sm-10">
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_user_permissions" bundle="${userPermissions}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-1 col-sm-10">

                    <form name="user-permissions-form" class="form-horizontal" id="user-permissions-form" role="form">
                    <fieldset>
<c:if test="${userPermissions.changePassword}">
                    <div class="form-group">
        <c:choose>
            <c:when test="${userPermissions.changeExclusiveSession and ((userPermissions.viewMaxSessions and userPermissions.changeMaxSessions) or (user.maxSessions eq 1))}">
                <jsp:useBean id="exclusiveSessionVariable" class="apollo.datastore.utils.HtmlVariableBean" />
                <jsp:setProperty name="exclusiveSessionVariable" property="varName" value="EXCLUSIVE_SESSION" />
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <div class="checkbox${(user.maxSessions eq 1) ? '' : ' disabled'}"><label id="exclusive-session-input-group" class="${(user.maxSessions eq 1) ? '' : 'text-muted'}"> <strong><fmt:message key="exclusive_session_checkbox_label" bundle="${settings}" /></strong></label></div>
                        </div>
            </c:when>
            <c:otherwise>
                        <label class="col-xs-12 col-sm-3 control-label" for="exclusive-session"><fmt:message key="exclusive_session_label" bundle="${settings}" /></label>
                        <div class="col-xs-12 col-sm-8"><p id="exclusive-session" class="form-control-static">${user.exclusiveSession ? fmtYes : fmtNo}</p></div>
            </c:otherwise>
        </c:choose>
                    </div>
</c:if>
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-8 control-label" for="change-password"><fmt:message key="date_created_label" bundle="${settings}" /></label>
                        <div class="col-xs-12 col-sm-3"><input name="${exclusiveSessionVariable.name}" id="change-password" type="checkbox" value="1"${userPermissions.changePassword ? " checked" : ""} /></div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-8 control-label" for="change-password"><fmt:message key="date_created_label" bundle="${settings}" /></label>
                        <div class="col-xs-12 col-sm-3"><p id="change-password" class="form-control-static"><c:out value="${user.dateCreated}" /></p></div>
                    </div>

                    </fieldset>
                    </form>
                </div>
            </div>
        </div>
        </main>
    </body>
</html>
