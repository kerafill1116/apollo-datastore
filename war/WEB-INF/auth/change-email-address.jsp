<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.ChangeEmailAddressBundle" var="changeEmailAddress" />

<jsp:useBean id="currentPasswordVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="currentPasswordVariable" property="varName" value="CURRENT_PASSWORD" />
<jsp:useBean id="newEmailAddressVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="newEmailAddressVariable" property="varName" value="NEW_EMAIL_ADDRESS" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_change_email_address" bundle="${changeEmailAddress}" /></title>
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
	changeEmailAddressFormValidator.element(event.target);
}

$(document).ready(function() {
    // globals
    currentPasswordInput = $('#current-password');
    newEmailAddressInput = $('#new-email-address');
    changeEmailAddressForm = $('#change-email-address');

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
    </head>
    <body>
<%@ include file="/WEB-INF/jspf/auth/top-navbar.jspf" %>
<%@ include file="/WEB-INF/jspf/auth/permissions-navbar.jspf" %>

        <div class="page-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-12 col-sm-offset-2 col-sm-8">
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_change_email_address" bundle="${changeEmailAddress}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">

                    <form name="change-email-address-form" method="post" action="/auth/change-email-address" class="form-horizontal" id="change-email-address-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="current-password"><fmt:message key="current_password_label" bundle="${changeEmailAddress}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${currentPasswordVariable.name}" type="password" class="form-control" id="current-password" maxlength="64" /></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="new-email-address"><fmt:message key="new_email_address_label" bundle="${changeEmailAddress}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${newEmailAddressVariable.name}" type="email" class="form-control" id="new-email-address" maxlength="256" required /></div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='change_email_address_button' bundle='${changeEmailAddress}' />" />
                            <input id="clear-btn" type="reset" class="btn btn-default" value="<fmt:message key='clear_button' bundle='${changeEmailAddress}' />" />
                        </div>
                    </div>
<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<c:if test="${not empty param[errorVariable.name]}">
    <fmt:setBundle basename="apollo.datastore.i18n.ErrorMessagesBundle" var="errorMessages" />
    <jsp:setProperty name="errorVariable" property="value" value="${param[errorVariable.name]}" />
    <jsp:useBean id="errorNone" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNone" property="constant" value="NONE" />
    <jsp:useBean id="errorIncorrectPassword" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorIncorrectPassword" property="constant" value="INCORRECT_PASSWORD" />
    <c:choose>
        <c:when test="${errorVariable.value eq errorNone.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-warning"><fmt:message key="message_password_changed" bundle="${changePassword}" /></p></div>
        </c:when>
        <c:when test="${errorVariable.value eq errorIncorrectPassword.code}" >
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_incorrect_password" bundle="${errorMessages}" /></p></div>
        </c:when>
        <c:otherwise>
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger"><fmt:message key="message_error_invalid" bundle="${errorMessages}" /></p></div>
        </c:otherwise>
    </c:choose>
</c:if>
                    </fieldset>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
