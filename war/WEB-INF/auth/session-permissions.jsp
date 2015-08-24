<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.SessionPermissionsBundle" var="sessionPermissionsBundle" />

<c:if test="${userPermissions.changeSessionPermissions}">
    <jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
    <jsp:useBean id="viewSessionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewSessionsVariable" property="varName" value="VIEW_SESSIONS" />
    <jsp:useBean id="disconnectSessionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="disconnectSessionsVariable" property="varName" value="DISCONNECT_SESSIONS" />
    <jsp:useBean id="viewSessionPermissionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="viewSessionPermissionsVariable" property="varName" value="VIEW_SESSION_PERMISSIONS" />
    <jsp:useBean id="changeSessionPermissionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="changeSessionPermissionsVariable" property="varName" value="CHANGE_SESSION_PERMISSIONS" />
</c:if>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_session_permissions" bundle="${sessionPermissionsBundle}" /></title>
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
    <c:when test="${userPermissions.changeSessionPermissions}">
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
            if(this.id != 'view-session-permissions' && this.id != 'change-session-permissions')
                $(this).triggerHandler('change');
        });
    }
}

function inputChangeHandler(event) {
    dependentId = $(event.target).data('dependentId');
    disableCheckboxes(event.target, dependentId);
}

$(document).ready(function() {

    viewSessionsCheckbox = $('#view-sessions');
    viewSessionsCheckbox.data('dependentId', '#disconnect-sessions');
    viewSessionsCheckbox.on('change', inputChangeHandler);

    viewSessionPermissionsCheckbox = $('#view-session-permissions');
    viewSessionPermissionsCheckbox.data('dependentId', 'all');
    viewSessionPermissionsCheckbox.on('change', inputChangeHandler);

    changeSessionPermissionsCheckbox = $('#change-session-permissions');
    changeSessionPermissionsCheckbox.data('dependentId', 'all');
    changeSessionPermissionsCheckbox.on('change', inputChangeHandler);

    allCheckboxes = $('#session-permissions-form input[type=checkbox]');

    resetBtn = $('#reset-btn');
    resetBtn.on('click', function(event) {
        this.form.reset();
        allCheckboxes.each(function(index) {
            $(this).triggerHandler('change');
        });
    });
    resetBtn.trigger('click');

    sessionPermissionsForm = $('#session-permissions-form');
    sessionPermissionsForm.on('submit', function(event) {
        allCheckboxes.each(function(index) {
            if(this.checked)
                this.disabled = false;
        });
    });

});
    </c:when>
    <c:otherwise>
$(document).ready(function() {
    $('#session-permissions-form label').each(function(index) {
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
            else if(nextProp.lastIndexOf('disconnect-', 0) === 0 && unchecked)
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
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_session_permissions" bundle="${sessionPermissionsBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">
<c:choose>
    <c:when test="${userPermissions.changeSessionPermissions}">
                    <form name="session-permissions-form" method="post" action="/auth/session-permissions" class="form-horizontal" id="session-permissions-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewSessionsVariable.name}" id="view-sessions" type="checkbox" value="1"${userPermissions.viewSessions ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-sessions"><fmt:message key="view_sessions_label" bundle="${sessionPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${disconnectSessionsVariable.name}" id="disconnect-sessions" type="checkbox" value="1"${userPermissions.disconnectSessions ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="disconnect-sessions"><fmt:message key="disconnect_sessions_label" bundle="${sessionPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${viewSessionPermissionsVariable.name}" id="view-session-permissions" type="checkbox" value="1"${userPermissions.viewSessionPermissions ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="view-session-permissions"><fmt:message key="view_session_permissions_label" bundle="${sessionPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right"><input name="${changeSessionPermissionsVariable.name}" id="change-session-permissions" type="checkbox" value="1"${userPermissions.changeSessionPermissions ? " checked" : ""} /></div>
                        <label class="col-xs-8 col-sm-8" for="change-session-permissions"><fmt:message key="change_session_permissions_label" bundle="${sessionPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='change_session_permissions_button' bundle='${sessionPermissionsBundle}' />" />
                            <input id="reset-btn" type="button" class="btn btn-default" value="<fmt:message key='reset_button' bundle='${sessionPermissionsBundle}' />" />
                        </div>
                    </div>
                    </fieldset>
                    </form>
    </c:when>
    <c:otherwise>
                    <form name="session-permissions-form" class="form-horizontal" id="session-permissions-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewSessions ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-sessions"><fmt:message key="view_sessions_label" bundle="${sessionPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.disconnectSessions ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="disconnect-sessions"><fmt:message key="disconnect_sessions_label" bundle="${sessionPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.viewSessionPermissions ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="view-session-permissions"><fmt:message key="view_session_permissions_label" bundle="${sessionPermissionsBundle}" /></label>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-1 col-sm-3 text-right">${userPermissions.changeSessionPermissions ? "<span class='glyphicon glyphicon-check'></span>" : "<span class='glyphicon glyphicon-unchecked'></span>"}</div>
                        <label class="col-xs-8 col-sm-8" for="change-session-permissions"><fmt:message key="change_session_permissions_label" bundle="${sessionPermissionsBundle}" /></label>
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
