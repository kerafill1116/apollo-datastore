<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.SessionLogPermissionsBundle" var="sessionLogPermissionsBundle" />

<c:if test="${userPermissions.changeSessionLogPermissions}">
    <jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
    <jsp:useBean id="viewSessionLogsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewSessionLogsVariable" property="varName" value="VIEW_SESSION_LOGS" />
    <jsp:useBean id="removeSessionLogsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="removeSessionLogsVariable" property="varName" value="REMOVE_SESSION_LOGS" />
    <jsp:useBean id="viewSessionLogPermissionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewSessionLogPermissionsVariable" property="varName" value="VIEW_SESSION_LOG_PERMISSIONS" />
    <jsp:useBean id="changeSessionLogPermissionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeSessionLogPermissionsVariable" property="varName" value="CHANGE_SESSION_LOG_PERMISSIONS" />
</c:if>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_session_log_permissions" bundle="${sessionLogPermissionsBundle}" /></title>
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
<c:choose>
    <c:when test="${userPermissions.changeSessionLogPermissions}">
function disableCheckboxes(element, id) {
    var checkbox;
    if(id == 'all')
        checkbox = allCheckboxes;
    else
        checkbox = $(id);
    if(element.checked) {
        checkbox.prop('disabled', false);
        checkbox.closest('label').removeClass('text-muted').closest('.checkbox').removeClass('disabled');
    }
    else {
        checkbox.prop('disabled', true);
        checkbox.closest('label').addClass('text-muted').closest('.checkbox').addClass('disabled');
    }
    $(element).prop('disabled', false);
    $(element).closest('label').removeClass('text-muted').closest('.checkbox').removeClass('disabled');
    if(id == 'all' && element.checked) {
        checkbox.each(function(index, element) {
            if(element.id != 'view-session-log-permissions' && element.id != 'change-session-log-permissions')
                $(element).triggerHandler('change');
        });
    }
}

function inputChangeHandler(event) {
    dependentId = $(event.currentTarget).data('dependentId');
    disableCheckboxes(event.currentTarget, dependentId);
}

$(document).ready(function() {

    viewSessionLogsCheckbox = $('#view-session-logs');
    viewSessionLogsCheckbox.data('dependentId', '#remove-session-logs');
    viewSessionLogsCheckbox.on('change', inputChangeHandler);

    viewSessionLogPermissionsCheckbox = $('#view-session-log-permissions');
    viewSessionLogPermissionsCheckbox.data('dependentId', 'all');
    viewSessionLogPermissionsCheckbox.on('change', inputChangeHandler);

    changeSessionLogPermissionsCheckbox = $('#change-session-log-permissions');
    changeSessionLogPermissionsCheckbox.data('dependentId', 'all');
    changeSessionLogPermissionsCheckbox.on('change', inputChangeHandler);

    sessionLogPermissionsForm = $('#session-log-permissions-form');
    sessionLogPermissionsForm.on('submit', function(event) {
        allCheckboxes.each(function(index, element) {
            element.disabled = !element.checked;
        });
    });

    allCheckboxes = sessionLogPermissionsForm.find(':checkbox');

    resetBtn = $('#reset-btn');
    resetBtn.on('click', function(event) {
        this.form.reset();
        allCheckboxes.each(function(index, element) {
            $(element).triggerHandler('change');
        });
    });
    resetBtn.trigger('click');

});
    </c:when>
    <c:otherwise>
$(document).ready(function() {
    $('#session-log-permissions-form label').each(function(index, element) {
        var spanGlyphicon = $(element).find('span');
        var unchecked = spanGlyphicon.hasClass('glyphicon-unchecked');
        if(unchecked)
            $(element).addClass('text-muted');
        var forProp = $(element).prop('for');
        if(forProp.lastIndexOf('view-', 0) === 0) {
            var nextLabel = $(element).closest('.form-group').next().find('label');
            var nextProp = nextLabel.prop('for');
            if(nextProp.lastIndexOf('change-', 0) === 0 && unchecked)
                nextLabel.addClass('text-muted');
            else if(nextProp.lastIndexOf('remove-', 0) === 0 && unchecked)
                nextLabel.addClass('text-muted');
        }
    });
});
    </c:otherwise>
</c:choose>
        </script>
    </head>
    <body>
<%@ include file="/WEB-INF/jspf/auth/top-navbar.jspf" %>
<%@ include file="/WEB-INF/jspf/auth/permissions-navbar.jspf" %>

        <div class="page-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-12 col-sm-offset-2 col-sm-8">
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_session_log_permissions" bundle="${sessionLogPermissionsBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">
<c:choose>
    <c:when test="${userPermissions.changeSessionLogPermissions}">
                    <form name="session-log-permissions-form" method="post" action="/auth/session-log-permissions" class="form-horizontal" id="session-log-permissions-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8 checkbox">
                            <label for="view-session-logs"><input name="${viewSessionLogsVariable.name}" id="view-session-logs" type="checkbox" value="1"${userPermissions.viewSessionLogs ? " checked" : ""} />
                            <strong><fmt:message key="view_session_logs_label" bundle="${sessionLogPermissionsBundle}" /></strong></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8 checkbox">
                            <label for="remove-session-logs"><input name="${removeSessionLogsVariable.name}" id="remove-session-logs" type="checkbox" value="1"${userPermissions.removeSessionLogs ? " checked" : ""} />
                            <strong><fmt:message key="remove_session_logs_label" bundle="${sessionLogPermissionsBundle}" /></strong></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8 checkbox">
                            <label for="view-session-log-permissions"><input name="${viewSessionLogPermissionsVariable.name}" id="view-session-log-permissions" type="checkbox" value="1"${userPermissions.viewSessionLogPermissions ? " checked" : ""} />
                            <strong><fmt:message key="view_session_log_permissions_label" bundle="${sessionLogPermissionsBundle}" /></strong></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8 checkbox">
                            <label for="change-session-log-permissions"><input name="${changeSessionLogPermissionsVariable.name}" id="change-session-log-permissions" type="checkbox" value="1"${userPermissions.changeSessionLogPermissions ? " checked" : ""} />
                            <strong><fmt:message key="change_session_log_permissions_label" bundle="${sessionLogPermissionsBundle}" /></strong></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='change_session_log_permissions_button' bundle='${sessionLogPermissionsBundle}' />" />
                            <input id="reset-btn" type="button" class="btn btn-default" value="<fmt:message key='reset_button' bundle='${sessionLogPermissionsBundle}' />" />
                        </div>
                    </div>
                    </fieldset>
                    </form>
    </c:when>
    <c:otherwise>
                    <form name="session-log-permissions-form" class="form-horizontal" id="session-log-permissions-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <label for="view-session-logs"><span class="glyphicon ${userPermissions.viewSessionLogs ? 'glyphicon-check' : 'glyphicon-unchecked'}"></span>
                            <strong><fmt:message key="view_session_logs_label" bundle="${sessionLogPermissionsBundle}" /></strong></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <label for="remove-session-logs"><span class="glyphicon ${userPermissions.removeSessionLogs ? 'glyphicon-check' : 'glyphicon-unchecked'}"></span>
                            <strong><fmt:message key="remove_session_logs_label" bundle="${sessionLogPermissionsBundle}" /></strong></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <label for="view-session-log-permissions"><span class="glyphicon ${userPermissions.viewSessionLogPermissions ? 'glyphicon-check' : 'glyphicon-unchecked'}"></span>
                            <strong><fmt:message key="view_session_log_permissions_label" bundle="${sessionLogPermissionsBundle}" /></strong></label>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <label for="change-session-log-permissions"><span class="glyphicon ${userPermissions.changeSessionLogPermissions ? 'glyphicon-check' : 'glyphicon-unchecked'}"></span>
                            <strong><fmt:message key="change_session_log_permissions_label" bundle="${sessionLogPermissionsBundle}" /></strong></label>
                        </div>
                    </div>

                    </fieldset>
                    </form>
    </c:otherwise>
</c:choose>
                </div>
            </div>
        </div>
        </main>
    </body>
</html>
