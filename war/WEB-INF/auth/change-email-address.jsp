<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.ChangeEmailAddressBundle" var="changeEmailAddressBundle" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<jsp:useBean id="currentPasswordVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="currentPasswordVariable" property="varName" value="CURRENT_PASSWORD" />
<jsp:useBean id="newEmailAddressVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="newEmailAddressVariable" property="varName" value="NEW_EMAIL_ADDRESS" />
<jsp:useBean id="requestIdVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="requestIdVariable" property="varName" value="REQUEST_ID" />
<jsp:useBean id="cancelVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="cancelVariable" property="varName" value="CANCEL" />
<jsp:useBean id="resendVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="resendVariable" property="varName" value="RESEND" />

<c:if test="${not empty requestScope[errorVariable.name]}">
    <jsp:setProperty name="errorVariable" property="value" value="${requestScope[errorVariable.name]}" />
    <jsp:setProperty name="newEmailAddressVariable" property="value" value="${requestScope[newEmailAddressVariable.name]}" />
</c:if>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_change_email_address" bundle="${changeEmailAddressBundle}" /></title>
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
<c:if test="${empty changeEmailAddressRequest}">
        <script type="text/javascript">
function inputChangeHandler(event) {
    changeEmailAddressFormValidator.element(event.target);
}

$(document).ready(function() {
    // globals
    currentPasswordInput = $('#current-password');
    newEmailAddressInput = $('#new-email-address');
    changeEmailAddressForm = $('#change-email-address-form');

    changeEmailAddressFormValidator = changeEmailAddressForm.validate({
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
            '${newEmailAddressVariable.name}': {
                maxlength: 256,
                required: true,
                email: true
            }
        },
        onkeyup: function(element, event) { },
        onfocusout: function(element, event) {
            if(element.value.length)
                changeEmailAddressFormValidator.element(element);
        },
        submitHandler: function(form) {
            form.submit();
        }
    });

    newEmailAddressInput.popover({placement: 'bottom', trigger: 'manual', content: '_'});
    newEmailAddressInput.on('input', inputChangeHandler);

});
        </script>
</c:if>
    </head>
    <body>
<%@ include file="/WEB-INF/jspf/auth/top-navbar.jspf" %>
<%@ include file="/WEB-INF/jspf/auth/permissions-navbar.jspf" %>

        <div class="page-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-12 col-sm-offset-2 col-sm-8">
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_change_email_address" bundle="${changeEmailAddressBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">

<c:choose>
    <c:when test="${not empty changeEmailAddressRequest}" >
                    <form name="change-email-address-form" class="form-horizontal" id="change-email-address-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="current-email-address"><fmt:message key="current_email_address_label" bundle="${changeEmailAddressBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><p id="current-email-address" class="form-control-static"><c:out value="${user.emailAddress}" /></p></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="new-email-address"><fmt:message key="new_email_address_label" bundle="${changeEmailAddressBundle}" /></label>
                        <div class="col-xs-12 col-sm-8">
                            <p id="new-email-address" class="form-control-static"><c:out value='${changeEmailAddressRequest.emailAddress}' />
                            <span class="label label-danger"><fmt:message key='unverified_label' bundle='${changeEmailAddressBundle}' /></span></p></div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <a href="/auth/change-email-address?${cancelVariable.name}" class="btn btn-default"><fmt:message key='cancel_button' bundle='${changeEmailAddressBundle}' /></a>
                            <a href="/auth/change-email-address?${resendVariable.name}" class="btn btn-default"><fmt:message key='resend_button' bundle='${changeEmailAddressBundle}' /></a>
                        </div>
                    </div>
                    </fieldset>
                    </form>
    </c:when>
    <c:otherwise>
                    <form name="change-email-address-form" method="post" action="/auth/change-email-address" class="form-horizontal" id="change-email-address-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="current-email-address"><fmt:message key="current_email_address_label" bundle="${changeEmailAddressBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><p id="current-email-address" class="form-control-static"><c:out value="${user.emailAddress}" /></p></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="new-email-address"><fmt:message key="new_email_address_label" bundle="${changeEmailAddressBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${newEmailAddressVariable.name}" type="email" class="form-control" id="new-email-address" maxlength="256" required value="<c:out value='${newEmailAddressVariable.value}' />" /></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="current-password"><fmt:message key="current_password_label" bundle="${changeEmailAddressBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${currentPasswordVariable.name}" type="password" class="form-control" id="current-password" maxlength="64" /></div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='change_email_address_button' bundle='${changeEmailAddressBundle}' />" />
                            <input id="clear-btn" type="reset" class="btn btn-default" value="<fmt:message key='clear_button' bundle='${changeEmailAddressBundle}' />" />
                        </div>
                    </div>
                    </fieldset>
                    </form>
    </c:otherwise>
</c:choose>
<c:if test="${not empty errorVariable.value}">
    <fmt:setBundle basename="apollo.datastore.i18n.ErrorMessagesBundle" var="errorMessagesBundle" />
    <jsp:useBean id="errorNone" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNone" property="constant" value="NONE" />
    <jsp:useBean id="errorIncorrectPassword" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorIncorrectPassword" property="constant" value="INCORRECT_PASSWORD" />
    <jsp:useBean id="errorRequiredEmailAddress" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorRequiredEmailAddress" property="constant" value="REQUIRED_EMAIL_ADDRESS" />
    <jsp:useBean id="errorInvalidEmailAddress" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInvalidEmailAddress" property="constant" value="INVALID_EMAIL_ADDRESS" />
    <jsp:useBean id="errorAlreadyExistsRequest" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorAlreadyExistsRequest" property="constant" value="ALREADY_EXISTS_REQUEST" />
    <jsp:useBean id="errorNonExistentRequest" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNonExistentRequest" property="constant" value="NON_EXISTENT_REQUEST" />
    <jsp:useBean id="errorRequiredRequestId" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorRequiredRequestId" property="constant" value="REQUIRED_REQUEST_ID" />
    <jsp:useBean id="errorInChangeEmailAddress" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInChangeEmailAddress" property="constant" value="ERROR_IN_CHANGE_EMAIL_ADDRESS" />
    <c:choose>
        <c:when test="${errorVariable.value eq errorNone.code}" >
            <c:choose>
                <c:when test="${not empty requestScope[requestIdVariable.name]}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-info"><fmt:message key="message_change_email_address_verified" bundle="${changeEmailAddressBundle}" /></p></div>
                </c:when>
                <c:when test="${not empty requestScope[cancelVariable.name]}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-info"><fmt:message key="message_cancel_change_email_address" bundle="${changeEmailAddressBundle}" /></p></div>
                </c:when>
                <c:when test="${not empty requestScope[resendVariable.name]}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-info"><fmt:message key="message_resent_verification_email" bundle="${changeEmailAddressBundle}" /></p></div>
                </c:when>
                <c:when test="${not empty changeEmailAddressRequest}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-info"><fmt:message key="message_verify_email_address" bundle="${changeEmailAddressBundle}" /></p></div>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:when test="${errorVariable.value eq errorIncorrectPassword.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_incorrect_password" bundle="${errorMessagesBundle}" /></p></div>
        </c:when>
        <c:when test="${errorVariable.value eq errorRequiredEmailAddress.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_required_email_address" bundle="${errorMessagesBundle}" /></p></div>
        </c:when>
        <c:when test="${errorVariable.value eq errorInvalidEmailAddress.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_invalid_email_address" bundle="${errorMessagesBundle}" /></p></div>
        </c:when>
        <c:when test="${errorVariable.value eq errorAlreadyExistsRequest.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_already_exists_request" bundle="${errorMessagesBundle}" /></p></div>
        </c:when>
        <c:when test="${errorVariable.value eq errorNonExistentRequest.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_non_existent_request" bundle="${errorMessagesBundle}" /></p></div>
        </c:when>
        <c:when test="${errorVariable.value eq errorRequiredRequestId.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_required_request_id" bundle="${errorMessagesBundle}" /></p></div>
        </c:when>
        <c:when test="${errorVariable.value eq errorInChangeEmailAddress.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_in_change_email_address" bundle="${errorMessagesBundle}" /></p></div>
        </c:when>
        <c:otherwise>
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_invalid" bundle="${errorMessagesBundle}" /></p></div>
        </c:otherwise>
    </c:choose>
</c:if>
                </div>
            </div>
        </div>
        </main>
    </body>
</html>
