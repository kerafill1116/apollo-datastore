<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.SettingsBundle" var="settings" />
<fmt:setBundle basename="apollo.datastore.i18n.TimeZonesBundle" var="timeZones" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_settings" bundle="${settings}" /></title>
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
        <script type="text/javascript" src="/js/messages_${langCookie.value}.js"></script>
</c:if>

        <script type="text/javascript">
$(document).ready(function() {
    // globals


<c:if test="${userPermissions.viewSessionTimeout and userPermissions.changeSessionTimeout}">
    <jsp:useBean id="sessionTimeoutVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="sessionTimeoutVariable" property="varName" value="SESSION_TIMEOUT" />
</c:if>

<c:if test="${userPermissions.viewMaxSessions and userPermissions.changeMaxSessions}">
    <jsp:useBean id="maxSessionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="maxSessionsVariable" property="varName" value="MAX_SESSIONS" />
</c:if>

<c:if test="${userPermissions.viewMaxFailedAttempts and userPermissions.changeMaxFailedAttempts}">
    <jsp:useBean id="maxFailedAttemptsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="maxFailedAttemptsVariable" property="varName" value="MAX_FAILED_ATTEMPTS" />
</c:if>

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
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_settings" bundle="${settings}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-1 col-sm-10">

                    <form name="settings-form" class="form-horizontal" id="settings-form" role="form">
                    <fieldset>
<c:if test="${userPermissions.viewEmailAddress}">
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="email-address"><fmt:message key="email_address_label" bundle="${settings}" /></label>
                        <div class="col-xs-12 col-sm-8"><p id="email-address" class="form-control-static"><c:out value="${user.emailAddress}" /></p></div>
                    </div>
</c:if>
<c:if test="${userPermissions.viewTimeZone}">
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="time-zone-id"><fmt:message key="time_zone_label" bundle="${settings}" /></label>
                        <div class="col-xs-12 col-sm-8">
    <c:set var="timeZoneId" value="${(user.timeZoneKey ne null) ? user.timeZoneKey.name : null}" />
    <c:choose>
        <c:when test="${userPermissions.changeTimeZone}">
            <jsp:useBean id="timeZoneIdVariable" class="apollo.datastore.utils.HtmlVariableBean" />
            <jsp:setProperty name="timeZoneIdVariable" property="varName" value="TIME_ZONE_ID" />
                            <select name="${timeZoneIdVariable.name}" class="form-control" id="time-zone-id">
                                <option value=""><fmt:message key="time_zone_choose_option" bundle="${settings}" /></option>
            <c:forEach var="timeZone" items="${jf:timeZonesArray()}" varStatus="loopCounter" >
                                <option value="${timeZone.timeZoneId}"${timeZone.timeZoneId eq timeZoneId ? " selected" : ""}><fmt:message key="${timeZone.timeZoneId}" bundle="${timeZones}" /></option>
            </c:forEach>
                            </select>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${timeZoneId ne null}">
                            <p id="time-zone-id" class="form-control-static"><fmt:message key="${timeZoneId}" bundle="${timeZones}" /></p>
                </c:when>
                <c:otherwise>
                            <p id="time-zone-id" class="form-control-static"><fmt:message key="undefined" bundle="${settings}" /></p>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
                        </div>
                    </div>
</c:if>

<fmt:message key="yes" bundle="${settings}" var="fmtYes"/>
<fmt:message key="no" bundle="${settings}" var="fmtNo"/>
<c:if test="${userPermissions.viewSessionTimeout or userPermissions.viewMaxSessions or (userPermissions.viewExclusiveSession and (user.maxSessions eq 1))}">
                    <div class="panel panel-default">
                        <div class="panel-heading"><fmt:message key="session" bundle="${settings}" /></div>
                        <div class="panel-body">
    <c:if test="${userPermissions.viewSessionTimeout}">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="session-timeout"><fmt:message key="session_timeout_label" bundle="${settings}" /></label>
                                <div class="col-xs-12 col-sm-8">
        <c:choose>
            <c:when test="${userPermissions.changeSessionTimeout}">
                                    <div class="input-group" id="session-timeout-input-group">
                                        <input name="${sessionTimeoutVariable.name}" type="number" min="0" class="form-control" id="session-timeout" required value="<c:out value='${user.sessionTimeout}' />" />
                                        <span class="input-group-addon"><fmt:message key="seconds" bundle="${settings}" /></span>
                                    </div>
            </c:when>
            <c:otherwise>
                                    <p id="session-timeout" class="form-control-static">${user.sessionTimeout} <fmt:message key="seconds" bundle="${settings}" /></p>
            </c:otherwise>
        </c:choose>
                                </div>
                            </div>
    </c:if>
    <c:if test="${userPermissions.viewMaxSessions}">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="max-sessions"><fmt:message key="max_sessions_label" bundle="${settings}" /></label>
                                <div class="col-xs-12 col-sm-8">
        <c:choose>
            <c:when test="${userPermissions.changeMaxSessions}">
                                    <input name="${maxSessionsVariable.name}" type="number" min="0" class="form-control" id="max-sessions" required value="<c:out value='${user.maxSessions}' />" />
            </c:when>
            <c:otherwise>
                                    <p id="max-sessions" class="form-control-static">${user.maxSessions}</p>
            </c:otherwise>
        </c:choose>
                                </div>
                            </div>
    </c:if>
    <c:if test="${userPermissions.viewExclusiveSession and ((userPermissions.viewMaxSessions and userPermissions.changeMaxSessions) or (user.maxSessions eq 1))}">
                            <div class="form-group">
        <c:choose>
            <c:when test="${userPermissions.changeExclusiveSession and ((userPermissions.viewMaxSessions and userPermissions.changeMaxSessions) or (user.maxSessions eq 1))}">
                <jsp:useBean id="exclusiveSessionVariable" class="apollo.datastore.utils.HtmlVariableBean" />
                <jsp:setProperty name="exclusiveSessionVariable" property="varName" value="EXCLUSIVE_SESSION" />
                                <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                                    <div class="checkbox${(user.maxSessions eq 1) ? '' : ' disabled'}"><label id="exclusive-session-input-group" class="${(user.maxSessions eq 1) ? '' : 'text-muted'}"><input${(user.maxSessions eq 1) ? '' : ' disabled'} name="${exclusiveSessionVariable.name}" id="exclusive-session" type="checkbox" value="1"${user.exclusiveSession ? " checked" : ""} /> <strong><fmt:message key="exclusive_session_checkbox_label" bundle="${settings}" /></strong></label></div>
                                </div>
            </c:when>
            <c:otherwise>
                                <label class="col-xs-12 col-sm-3 control-label" for="exclusive-session"><fmt:message key="exclusive_session_label" bundle="${settings}" /></label>
                                <div class="col-xs-12 col-sm-8"><p id="exclusive-session" class="form-control-static">${user.exclusiveSession ? fmtYes : fmtNo}</p></div>
            </c:otherwise>
        </c:choose>
                            </div>
    </c:if>
                        </div>
                    </div>
</c:if>
<c:if test="${userPermissions.viewMaxFailedAttempts}">
                    <div class="panel panel-default">
                        <div class="panel-heading"><fmt:message key="security" bundle="${settings}" /></div>
                        <div class="panel-body">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="max-failed-attempts"><fmt:message key="max_failed_attempts_label" bundle="${settings}" /></label>
                                <div class="col-xs-12 col-sm-8">
    <c:choose>
        <c:when test="${userPermissions.changeMaxFailedAttempts}">
                                    <input name="${maxFailedAttemptsVariable.name}" type="number" min="-1" class="form-control" id="maxFailedAttempts" required value="<c:out value='${user.maxFailedAttempts}' />" />
        </c:when>
        <c:otherwise>
                                    <p id="max-failed-attempts" class="form-control-static">${user.maxFailedAttempts}</p>
        </c:otherwise>
    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
</c:if>
<c:if test="${userPermissions.viewDisabledStatus or userPermissions.viewActivatedStatus}">
                    <div class="panel panel-default">
                        <div class="panel-heading"><fmt:message key="status" bundle="${settings}" /></div>
                        <div class="panel-body">
    <c:if test="${userPermissions.viewDisabledStatus}">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="disabled"><fmt:message key="disabled_label" bundle="${settings}" /></label>
                                <div class="col-xs-12 col-sm-8"><p id="disabled" class="form-control-static">${user.disabled ? fmtYes : fmtNo}</p></div>
                            </div>
    </c:if>
    <c:if test="${userPermissions.viewActivatedStatus}">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="activated"><fmt:message key="activated_label" bundle="${settings}" /></label>
                                <div class="col-xs-12 col-sm-8"><p id="activated" class="form-control-static">${user.activated ? fmtYes : fmtNo}</p></div>
                            </div>
    </c:if>
                        </div>
                    </div>
</c:if>
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="date-created"><fmt:message key="date_created_label" bundle="${settings}" /></label>
                        <div class="col-xs-12 col-sm-8"><p id="date-created" class="form-control-static"><c:out value="${user.dateCreated}" /></p></div>
                    </div>

                    </fieldset>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
