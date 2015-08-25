<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.UserPermissionsBundle" var="userPermissionsBundle" />

<c:if test="${userPermissions.changeUserPermissions}">
    <jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
    <jsp:useBean id="changePasswordVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changePasswordVariable" property="varName" value="CHANGE_PASSWORD" />
    <jsp:useBean id="viewEmailAddressVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewEmailAddressVariable" property="varName" value="VIEW_EMAIL_ADDRESS" />
    <jsp:useBean id="changeEmailAddressVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeEmailAddressVariable" property="varName" value="CHANGE_EMAIL_ADDRESS" />
    <jsp:useBean id="viewTimeZoneVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewTimeZoneVariable" property="varName" value="VIEW_TIME_ZONE" />
    <jsp:useBean id="changeTimeZoneVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeTimeZoneVariable" property="varName" value="CHANGE_TIME_ZONE" />
    <jsp:useBean id="viewSessionTimeoutVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewSessionTimeoutVariable" property="varName" value="VIEW_SESSION_TIMEOUT" />
    <jsp:useBean id="changeSessionTimeoutVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeSessionTimeoutVariable" property="varName" value="CHANGE_SESSION_TIMEOUT" />
    <jsp:useBean id="viewMaxSessionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewMaxSessionsVariable" property="varName" value="VIEW_MAX_SESSIONS" />
    <jsp:useBean id="changeMaxSessionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeMaxSessionsVariable" property="varName" value="CHANGE_MAX_SESSIONS" />
    <jsp:useBean id="viewExclusiveSessionVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewExclusiveSessionVariable" property="varName" value="VIEW_EXCLUSIVE_SESSION" />
    <jsp:useBean id="changeExclusiveSessionVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeExclusiveSessionVariable" property="varName" value="CHANGE_EXCLUSIVE_SESSION" />
    <jsp:useBean id="viewMaxFailedAttemptsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewMaxFailedAttemptsVariable" property="varName" value="VIEW_MAX_FAILED_ATTEMPTS" />
    <jsp:useBean id="changeMaxFailedAttemptsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeMaxFailedAttemptsVariable" property="varName" value="CHANGE_MAX_FAILED_ATTEMPTS" />
    <jsp:useBean id="viewDisabledStatusVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewDisabledStatusVariable" property="varName" value="VIEW_DISABLED_STATUS" />
    <jsp:useBean id="viewActivatedStatusVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewActivatedStatusVariable" property="varName" value="VIEW_ACTIVATED_STATUS" />
    <jsp:useBean id="viewUserPermissionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewUserPermissionsVariable" property="varName" value="VIEW_USER_PERMISSIONS" />
    <jsp:useBean id="changeUserPermissionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeUserPermissionsVariable" property="varName" value="CHANGE_USER_PERMISSIONS" />
</c:if>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_user_permissions" bundle="${userPermissionsBundle}" /></title>
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
    <c:when test="${userPermissions.changeUserPermissions}">
function disableCheckboxes(element, id) {
    var checkbox;
    if(id == 'all')
        checkbox = allCheckboxes;
    else
        checkbox = $(id);
    if(element.checked) {
        checkbox.prop('disabled', false);
        checkbox.parent().next().removeClass('text-muted').removeClass('disabled');
    }
    else {
        checkbox.prop('disabled', true);
        checkbox.parent().next().addClass('text-muted').addClass('disabled');
    }
    $(element).prop('disabled', false);
    $(element).parent().next().removeClass('text-muted').removeClass('disabled');
    if(id == 'all' && element.checked) {
        checkbox.each(function(index) {
            if(this.id != 'view-user-permissions' && this.id != 'change-user-permissions')
                $(this).triggerHandler('change');
        });
    }
}

function inputChangeHandler(event) {
    dependentId = $(event.currentTarget).data('dependentId');
    disableCheckboxes(event.currentTarget, dependentId);
}

$(document).ready(function() {

    viewEmailAddressCheckbox = $('#view-email-address');
    viewEmailAddressCheckbox.data('dependentId', '#change-email-address');
    viewEmailAddressCheckbox.on('change', inputChangeHandler);

    viewTimeZoneCheckbox = $('#view-time-zone');
    viewTimeZoneCheckbox.data('dependentId', '#change-time-zone');
    viewTimeZoneCheckbox.on('change', inputChangeHandler);

    viewSessionTimeoutCheckbox = $('#view-session-timeout');
    viewSessionTimeoutCheckbox.data('dependentId', '#change-session-timeout');
    viewSessionTimeoutCheckbox.on('change', inputChangeHandler);

    viewMaxSessionsCheckbox = $('#view-max-sessions');
    viewMaxSessionsCheckbox.data('dependentId', '#change-max-sessions');
    viewMaxSessionsCheckbox.on('change', inputChangeHandler);

    viewExclusiveSessionCheckbox = $('#view-exclusive-session');
    viewExclusiveSessionCheckbox.data('dependentId', '#change-exclusive-session');
    viewExclusiveSessionCheckbox.on('change', inputChangeHandler);

    viewMaxFailedAttemptsCheckbox = $('#view-max-failed-attempts');
    viewMaxFailedAttemptsCheckbox.data('dependentId', '#change-max-failed-attempts');
    viewMaxFailedAttemptsCheckbox.on('change', inputChangeHandler);

    viewUserPermissionsCheckbox = $('#view-user-permissions');
    viewUserPermissionsCheckbox.data('dependentId', 'all');
    viewUserPermissionsCheckbox.on('change', inputChangeHandler);

    changeUserPermissionsCheckbox = $('#change-user-permissions');
    changeUserPermissionsCheckbox.data('dependentId', 'all');
    changeUserPermissionsCheckbox.on('change', inputChangeHandler);

    userPermissionsForm = $('#user-permissions-form');
    userPermissionsForm.on('submit', function(event) {
        allCheckboxes.each(function(index) {
            if(this.checked)
                this.disabled = false;
        });
    });

    allCheckboxes = userPermissionsForm.find(':checkbox');

    resetBtn = $('#reset-btn');
    resetBtn.on('click', function(event) {
        this.form.reset();
        allCheckboxes.each(function(index) {
            $(this).triggerHandler('change');
        });
    });
    resetBtn.trigger('click');

});
    </c:when>
    <c:otherwise>
$(document).ready(function() {
    $('#user-permissions-form label').each(function(index) {
        var spanGlyphicon = $(this).prev().children('span');
        var unchecked = spanGlyphicon.hasClass('glyphicon-unchecked');
        if(unchecked)
            $(this).parent().addClass('text-muted');
        var forProp = $(this).prop('for');
        if(forProp.lastIndexOf('view-', 0) === 0) {
            var nextLabel = $(this).parent().next().children('label');
            var nextProp = nextLabel.prop('for');
            if(nextProp.lastIndexOf('change-', 0) === 0 && unchecked)
                $(this).parent().next().addClass('text-muted');
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
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_user_permissions" bundle="${userPermissionsBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">
<c:choose>
    <c:when test="${userPermissions.changeUserPermissions}">
                    <form name="user-permissions-form" method="post" action="/auth/user-permissions" class="form-horizontal" id="user-permissions-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changePasswordVariable.name}" id="change-password" type="checkbox" value="1"${userPermissions.changePassword ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-password"><fmt:message key="change_password_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewEmailAddressVariable.name}" id="view-email-address" type="checkbox" value="1"${userPermissions.viewEmailAddress ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-email-address"><fmt:message key="view_email_address_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changeEmailAddressVariable.name}" id="change-email-address" type="checkbox" value="1"${userPermissions.changeEmailAddress ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-email-address"><fmt:message key="change_email_address_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewTimeZoneVariable.name}" id="view-time-zone" type="checkbox" value="1"${userPermissions.viewTimeZone ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-time-zone"><fmt:message key="view_time_zone_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changeTimeZoneVariable.name}" id="change-time-zone" type="checkbox" value="1"${userPermissions.changeTimeZone ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-time-zone"><fmt:message key="change_time_zone_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewSessionTimeoutVariable.name}" id="view-session-timeout" type="checkbox" value="1"${userPermissions.viewSessionTimeout ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-session-timeout"><fmt:message key="view_session_timeout_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changeSessionTimeoutVariable.name}" id="change-session-timeout" type="checkbox" value="1"${userPermissions.changeSessionTimeout ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-session-timeout"><fmt:message key="change_session_timeout_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewMaxSessionsVariable.name}" id="view-max-sessions" type="checkbox" value="1"${userPermissions.viewMaxSessions ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-max-sessions"><fmt:message key="view_max_sessions_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changeMaxSessionsVariable.name}" id="change-max-sessions" type="checkbox" value="1"${userPermissions.changeMaxSessions ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-max-sessions"><fmt:message key="change_max_sessions_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewExclusiveSessionVariable.name}" id="view-exclusive-session" type="checkbox" value="1"${userPermissions.viewExclusiveSession ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-exclusive-session"><fmt:message key="view_exclusive_session_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changeExclusiveSessionVariable.name}" id="change-exclusive-session" type="checkbox" value="1"${userPermissions.changeExclusiveSession ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-exclusive-session"><fmt:message key="change_exclusive_session_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewMaxFailedAttemptsVariable.name}" id="view-max-failed-attempts" type="checkbox" value="1"${userPermissions.viewMaxFailedAttempts ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-max-failed-attempts"><fmt:message key="view_max_failed_attempts_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changeMaxFailedAttemptsVariable.name}" id="change-max-failed-attempts" type="checkbox" value="1"${userPermissions.changeMaxFailedAttempts ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-max-failed-attempts"><fmt:message key="change_max_failed_attempts_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewDisabledStatusVariable.name}" id="view-disabled-status" type="checkbox" value="1"${userPermissions.viewDisabledStatus ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-disabled-status"><fmt:message key="view_disabled_status_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewActivatedStatusVariable.name}" id="view-activated-status" type="checkbox" value="1"${userPermissions.viewActivatedStatus ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-activated-status"><fmt:message key="view_activated_status_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewUserPermissionsVariable.name}" id="view-user-permissions" type="checkbox" value="1"${userPermissions.viewUserPermissions ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-user-permissions"><fmt:message key="view_user_permissions_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changeUserPermissionsVariable.name}" id="change-user-permissions" type="checkbox" value="1"${userPermissions.changeUserPermissions ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-user-permissions"><fmt:message key="change_user_permissions_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='change_user_permissions_button' bundle='${userPermissionsBundle}' />" />
                            <input id="reset-btn" type="button" class="btn btn-default" value="<fmt:message key='reset_button' bundle='${userPermissionsBundle}' />" />
                        </div>
                    </div>
                    </fieldset>
                    </form>
    </c:when>
    <c:otherwise>
                    <form name="user-permissions-form" class="form-horizontal" id="user-permissions-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changePassword ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-password"><fmt:message key="change_password_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewEmailAddress ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-email-address"><fmt:message key="view_email_address_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changeEmailAddress ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-email-address"><fmt:message key="change_email_address_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewTimeZone ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-time-zone"><fmt:message key="view_time_zone_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changeTimeZone ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-time-zone"><fmt:message key="change_time_zone_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewSessionTimeout ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-session-timeout"><fmt:message key="view_session_timeout_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changeSessionTimeout ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-session-timeout"><fmt:message key="change_session_timeout_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewMaxSessions ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-max-sessions"><fmt:message key="view_max_sessions_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changeMaxSessions ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-max-sessions"><fmt:message key="change_max_sessions_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewExclusiveSession ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-exclusive-session"><fmt:message key="view_exclusive_session_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changeExclusiveSession ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-exclusive-session"><fmt:message key="change_exclusive_session_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewMaxFailedAttempts ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-max-failed-attempts"><fmt:message key="view_max_failed_attempts_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changeMaxFailedAttempts ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-max-failed-attempts"><fmt:message key="change_max_failed_attempts_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewDisabledStatus ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-disabled-status"><fmt:message key="view_disabled_status_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewActivatedStatus ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-activated-status"><fmt:message key="view_activated_status_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewUserPermissions ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-user-permissions"><fmt:message key="view_user_permissions_label" bundle="${userPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changeUserPermissions ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-user-permissions"><fmt:message key="change_user_permissions_label" bundle="${userPermissionsBundle}" /></label>
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
