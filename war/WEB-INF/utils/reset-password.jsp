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
<c:if test="${not empty requestScope[errorVariable.name]}">
    <jsp:setProperty name="errorVariable" property="value" value="${requestScope[errorVariable.name]}" />
    <jsp:setProperty name="userIdVariable" property="value" value="${requestScope[userIdVariable.name]}" />
</c:if>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_reset_password_request" bundle="${utilitiesBundle}" /></title>
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
    var validElement = false;
    if(event.currentTarget.value.length)
        validElement = resetPasswordFormValidator.element(event.currentTarget);
    else
        $(event.currentTarget).popover('hide');
}

$(document).ready(function() {
    // globals
    userIdInput = $('#user-id');
    resetPasswordForm = $('#reset-password-form');

    $('#clear-btn').click(function () {
        resetPasswordFormValidator.resetForm();
        this.form.reset();
        userIdInput.popover('hide');
    });

    resetPasswordFormValidator = resetPasswordForm.validate({
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
                    popoverDiv.find('.popover-content').addClass('text-danger');
                }
                popoverDiv.find('.popover-content').html(errorList[i].message);
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
                resetPasswordFormValidator.element(element);
        }
    });

    // initialize popovers
    userIdInput.popover({placement: 'bottom', trigger: 'manual', content: '_'});

    // for validator and autocomplete
    userIdInput.on('input', inputChangeHandler);

    // for when back button is pressed on browser
<c:if test="${errorVariable.value ne errorNone.code}">
    resetPasswordForm.valid();
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
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_reset_password_request" bundle="${utilitiesBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">

                    <form name="reset-password-form" method="post" action="/utils/reset-password-request" class="form-horizontal" id="reset-password-form" role="form">

                    <fieldset>
                    <div class="row"><p class="col-xs-12 col-sm-offset-3 col-sm-8"><fmt:message key="message_reset_password_request" bundle="${utilitiesBundle}" /></p></div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="user-id"><fmt:message key="user_id_label" bundle="${utilitiesBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${userIdVariable.name}" type="text" class="form-control" id="user-id" maxlength="32" required autofocus value="<c:out value='${userIdVariable.value}' />" /></div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='reset_password_button' bundle='${utilitiesBundle}' />" />
                            <input id="clear-btn" type="reset" class="btn btn-default" value="<fmt:message key='clear_button' bundle='${utilitiesBundle}' />" />
                        </div>
                    </div>
                    </fieldset>
                    </form>
<c:if test="${errorVariable.value ne errorNone.code}">
    <fmt:setBundle basename="apollo.datastore.i18n.ErrorMessagesBundle" var="errorMessagesBundle" />
    <jsp:useBean id="errorRequiredUserId" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorRequiredUserId" property="constant" value="REQUIRED_USER_ID" />
    <jsp:useBean id="errorAlreadyExistsRequest" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorAlreadyExistsRequest" property="constant" value="ALREADY_EXISTS_REQUEST" />
    <jsp:useBean id="errorNonExistentUser" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNonExistentUser" property="constant" value="NON_EXISTENT_USER" />
    <jsp:useBean id="errorNotActivatedUser" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNotActivatedUser" property="constant" value="NOT_ACTIVATED_USER" />
    <jsp:useBean id="errorDisabledUser" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorDisabledUser" property="constant" value="DISABLED_USER" />
    <jsp:useBean id="errorInResetPasswordRequest" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInResetPasswordRequest" property="constant" value="ERROR_IN_RESET_PASSWORD_REQUEST" />
    <c:choose>
        <c:when test="${errorVariable.value eq errorRequiredUserId.code}" >
            <fmt:message key="message_error_required_user_id" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorAlreadyExistsRequest.code}" >
            <fmt:message key="message_error_already_exists_request" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorNonExistentUser.code}" >
            <fmt:message key="message_error_non_existent_user" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorNotActivatedUser.code}" >
            <fmt:message key="message_error_not_activated_user" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorDisabledUser.code}" >
            <fmt:message key="message_error_disabled_user" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorInResetPasswordRequest.code}" >
            <fmt:message key="message_error_in_reset_password_request" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:when>
        <c:otherwise>
            <fmt:message key="message_error_invalid" bundle="${errorMessagesBundle}" var="errorMessage" />
        </c:otherwise>
    </c:choose>
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger">${errorMessage}</p></div>
</c:if>
                </div>
            </div>
        </div>
        </main>

    </body>
</html>