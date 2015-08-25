<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.UtilitiesBundle" var="utilitiesBundle" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<jsp:useBean id="errorNone" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorNone" property="constant" value="NONE" />
<jsp:setProperty name="errorVariable" property="value" value="${errorNone.code}" />

<jsp:useBean id="userIdVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="userIdVariable" property="varName" value="USER_ID" />
<jsp:useBean id="passwordVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="passwordVariable" property="varName" value="PASSWORD" />
<jsp:useBean id="rememberMeVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="rememberMeVariable" property="varName" value="REMEMBER_ME" />

<jsp:useBean id="rememberMeCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="rememberMeCookie" property="varName" value="REMEMBER_ME" />
<c:if test="${not empty cookie[rememberMeCookie.name]}">
    <jsp:setProperty name="rememberMeVariable" property="value" value="${cookie[rememberMeCookie.name].value}" />
    <jsp:useBean id="userIdCookie" class="apollo.datastore.CookiesBean" />
    <jsp:setProperty name="userIdCookie" property="varName" value="USER_ID" />
    <jsp:setProperty name="userIdVariable" property="value" value="${cookie[userIdCookie.name].value}" />
</c:if>

<jsp:useBean id="causeOfDisconnectVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="causeOfDisconnectVariable" property="varName" value="CAUSE_OF_DISCONNECT" />
<c:if test="${not empty requestScope[errorVariable.name]}">
    <jsp:setProperty name="errorVariable" property="value" value="${requestScope[errorVariable.name]}" />
    <jsp:setProperty name="causeOfDisconnectVariable" property="value" value="${requestScope[causeOfDisconnectVariable.name]}" />
</c:if>
<c:if test="${not empty requestScope[userIdVariable.name]}">
    <jsp:setProperty name="userIdVariable" property="value" value="${requestScope[userIdVariable.name]}" />
    <jsp:setProperty name="rememberMeVariable" property="value" value="${null}" />
</c:if>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_sign_in" bundle="${utilitiesBundle}" /></title>
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
    if(event.currentTarget.value.length)
        signInFormValidator.element(event.currentTarget);
    else
        $(event.currentTarget).popover('hide');
}

$(document).ready(function() {
    // globals
    userIdInput = $('#user-id');
    signInForm = $('#sign-in-form');

    $('#reset-password-link').attr('href', '/utils/reset-password-request');
    $('#register-user-link').attr('href', '/utils/register-user');

    $('#clear-btn').click(function () {
        signInFormValidator.resetForm();
        signInForm[0].reset();
        userIdInput.popover('hide');
    });

    signInFormValidator = signInForm.validate({
        errorPlacement: function(error, element) { },
        highlight: function(element, errorClass, validClass) { },
        unhighlight: function(element, errorClass, validClass) {
            $(element).popover('hide');
        },
        showErrors: function(errorMap, errorList) {
            for(var i = 0; i < errorList.length; i++) {
                var errorListItem = $(errorList[i].element);
                var popoverDiv = errorListItem.next();
                if(!popoverDiv.length) {
                    errorListItem.popover('show');
                    popoverDiv = errorListItem.next();
                    popoverDiv.css({'left': '0px', 'margin-left': '10px', 'margin-right': '10px'});
                    popoverDiv.children('.popover-content').addClass('text-danger');
                }
                popoverDiv.children('.popover-content').html(errorList[i].message);
            }
            this.defaultShowErrors();
        },
        rules: {
            '${userIdVariable.name}': {
                minlength: 5,
                maxlength: 32,
                required: true
            }
        },
        onkeyup: function(element, event) { },
        onfocusout: function(element, event) {
            if(element.value.length)
                signInFormValidator.element(element);
        }
    });

    // initialize popovers
    userIdInput.popover({placement: 'bottom', trigger: 'manual', content: '_'});

    // for validator and autocomplete
    userIdInput.on('input', inputChangeHandler);

    // for when back button is pressed on browser
<c:if test="${errorVariable.value ne errorNone.code}">
    signInForm.valid();
</c:if>
});
        </script>
    </head>
    <body>
<%@ include file="/WEB-INF/jspf/pub/top-navbar.jspf" %>
        <div class="page-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-12 col-sm-offset-2 col-sm-8">
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_sign_in" bundle="${utilitiesBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">

                    <form name="sign-in-form" method="post" action="/utils/sign-in" class="form-horizontal" id="sign-in-form" role="form">

                    <fieldset>
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="user-id"><fmt:message key="user_id_label" bundle="${utilitiesBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${userIdVariable.name}" type="text" class="form-control" id="user-id" maxlength="32" required autofocus value="<c:out value='${userIdVariable.value}' />" /></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="password"><fmt:message key="password_label" bundle="${utilitiesBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${passwordVariable.name}" type="password" class="form-control" id="password" maxlength="64" /></div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='sign_in_button' bundle='${utilitiesBundle}' />" />
                            <input id="clear-btn" type="reset" class="btn btn-default" value="<fmt:message key='clear_button' bundle='${utilitiesBundle}' />" />
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <div class="checkbox"><label><input name="${rememberMeVariable.name}" type="checkbox" id="remember-me" value="1"${not empty rememberMeVariable.value ? " checked" : ""} /> <fmt:message key="remember_me" bundle="${utilitiesBundle}" /></label></div>
                        </div>
                    </div>
                    </fieldset>
                    </form>
<c:if test="${errorVariable.value ne errorNone.code}">
    <fmt:setBundle basename="apollo.datastore.i18n.ErrorMessagesBundle" var="errorMessagesBundle" />
    <jsp:useBean id="errorRequiredUserId" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorRequiredUserId" property="constant" value="REQUIRED_USER_ID" />
    <jsp:useBean id="errorNonExistentUser" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNonExistentUser" property="constant" value="NON_EXISTENT_USER" />
    <jsp:useBean id="errorIncorrectPassword" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorIncorrectPassword" property="constant" value="INCORRECT_PASSWORD" />
    <jsp:useBean id="errorNotActivatedUser" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNotActivatedUser" property="constant" value="NOT_ACTIVATED_USER" />
    <jsp:useBean id="errorDisabledUser" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorDisabledUser" property="constant" value="DISABLED_USER" />
    <jsp:useBean id="errorReachedMaxSessions" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorReachedMaxSessions" property="constant" value="REACHED_MAX_SESSIONS" />
    <jsp:useBean id="errorExclusiveSession" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorExclusiveSession" property="constant" value="EXCLUSIVE_SESSION" />
    <jsp:useBean id="errorMaxedFailedAttempts" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorMaxedFailedAttempts" property="constant" value="MAXED_FAILED_ATTEMPTS" />
    <jsp:useBean id="errorInSignIn" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInSignIn" property="constant" value="ERROR_IN_SIGN_IN" />
    <jsp:useBean id="errorNonExistentSession" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNonExistentSession" property="constant" value="NON_EXISTENT_SESSION" />
    <jsp:useBean id="errorInSignOut" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInSignOut" property="constant" value="ERROR_IN_SIGN_OUT" />
    <jsp:useBean id="errorInSessionCheck" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInSessionCheck" property="constant" value="ERROR_IN_SESSION_CHECK" />
    <c:choose>
        <c:when test="${errorVariable.value eq errorRequiredUserId.code}" >
            <fmt:message key="message_error_required_user_id" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorNonExistentUser.code}" >
            <fmt:message key="message_error_non_existent_user" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorIncorrectPassword.code}" >
            <fmt:message key="message_error_incorrect_password" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorNotActivatedUser.code}" >
            <fmt:message key="message_error_not_activated_user" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorDisabledUser.code}" >
            <fmt:message key="message_error_disabled_user" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorReachedMaxSessions.code}" >
            <fmt:message key="message_error_reached_max_sessions" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorExclusiveSession.code}" >
            <fmt:message key="message_error_exclusive_session" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorMaxedFailedAttempts.code}" >
            <fmt:message key="message_error_maxed_failed_attempts" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorInSignIn.code}" >
            <fmt:message key="message_error_in_sign_in" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorNonExistentSession.code}" >
            <fmt:message key="message_error_non_existent_session" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorInSignOut.code}" >
            <fmt:message key="message_error_in_sign_out" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorInSessionCheck.code}" >
            <fmt:message key="message_error_in_session_check" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:otherwise>
            <fmt:message key="message_error_invalid" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:otherwise>
    </c:choose>
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger">${errorMessage}</p></div>
</c:if>
<c:if test="${not empty causeOfDisconnectVariable.value and errorVariable.value eq errorNone.code}">
    <fmt:setBundle basename="apollo.datastore.i18n.CauseOfDisconnectMessagesBundle" var="causeOfDisconnectMessagesBundle" />
    <jsp:useBean id="noneDisconnect" class="apollo.datastore.CauseOfDisconnectBean" />
    <jsp:setProperty name="noneDisconnect" property="constant" value="NONE" />
    <jsp:useBean id="exclusiveSessionDisconnect" class="apollo.datastore.CauseOfDisconnectBean" />
    <jsp:setProperty name="exclusiveSessionDisconnect" property="constant" value="EXCLUSIVE_SESSION" />
    <jsp:useBean id="timedOutSessionDisconnect" class="apollo.datastore.CauseOfDisconnectBean" />
    <jsp:setProperty name="timedOutSessionDisconnect" property="constant" value="TIMED_OUT_SESSION" />
    <jsp:useBean id="disconnectedSessionDisconnect" class="apollo.datastore.CauseOfDisconnectBean" />
    <jsp:setProperty name="disconnectedSessionDisconnect" property="constant" value="DISCONNECTED_SESSION" />
    <c:choose>
        <c:when test="${causeOfDisconnectVariable.value eq noneDisconnect.code}" >
            <fmt:message key="none" bundle="${causeOfDisconnectMessagesBundle}" var="causeOfDisconnectMessage" />
        </c:when>
        <c:when test="${causeOfDisconnectVariable.value eq exclusiveSessionDisconnect.code}" >
            <fmt:message key="exclusive_session" bundle="${causeOfDisconnectMessagesBundle}" var="causeOfDisconnectMessage" />
        </c:when>
        <c:when test="${causeOfDisconnectVariable.value eq timedOutSessionDisconnect.code}" >
            <fmt:message key="timed_out_session" bundle="${causeOfDisconnectMessagesBundle}" var="causeOfDisconnectMessage" />
        </c:when>
        <c:when test="${causeOfDisconnectVariable.value eq disconnectedSessionDisconnect.code}" >
            <fmt:message key="disconnected_session" bundle="${causeOfDisconnectMessagesBundle}" var="causeOfDisconnectMessage" />
        </c:when>
        <c:otherwise>
            <fmt:message key="invalid_cause_of_disconnect" bundle="${causeOfDisconnectMessagesBundle}" var="causeOfDisconnectMessage" />
        </c:otherwise>
    </c:choose>
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-warning">${causeOfDisconnectMessage}</p></div>
</c:if>
                    <div class="row">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8"><fmt:message key="forgot_password" bundle="${utilitiesBundle}" /></div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8"><fmt:message key="register_now" bundle="${utilitiesBundle}" /></div>
                    </div>
                </div>
            </div>
        </div>
        </main>

    </body>
</html>