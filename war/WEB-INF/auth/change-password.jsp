<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.ChangePasswordBundle" var="changePassword" />
<fmt:setBundle basename="apollo.datastore.i18n.TimeZonesBundle" var="timeZones" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<jsp:useBean id="currentPasswordVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="currentPasswordVariable" property="varName" value="CURRENT_PASSWORD" />
<jsp:useBean id="newPasswordVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="newPasswordVariable" property="varName" value="NEW_PASSWORD" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_change_password" bundle="${changePassword}" /></title>
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
    changePasswordFormValidator.element(event.target);
}

$(document).ready(function() {
    // globals
    currentPasswordInput = $('#current-password');
    newPasswordInput = $('#new-password');
    confirmNewPasswordInput = $('#confirm-new-password');
    changePasswordForm = $('#change-password-form');

    changePasswordFormValidator = changePasswordForm.validate({
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
            'confirm-new-password': {
                equalTo: '#new-password'
            }
        },
        onkeyup: function(element, event) { },
        onfocusout: function(element, event) {
            changePasswordFormValidator.element(element);
        },
        submitHandler: function(form) {
            if(currentPasswordInput.val() != newPasswordInput.val())
                form.submit();
        }
    });

    confirmNewPasswordInput.popover({placement: 'bottom', trigger: 'manual', content: '_'});

    confirmNewPasswordInput.on('input', inputChangeHandler);
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
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_change_password" bundle="${changePassword}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-1 col-sm-10">

                    <form name="change-password-form" method="post" action="/auth/change-password" class="form-horizontal" id="change-password-form" role="form">
                    <fieldset>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="current-password"><fmt:message key="current_password_label" bundle="${changePassword}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${currentPasswordVariable.name}" type="password" class="form-control" id="current-password" maxlength="64" /></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="new-password"><fmt:message key="new_password_label" bundle="${changePassword}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${newPasswordVariable.name}" type="password" class="form-control" id="new-password" maxlength="64" /></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="confirm-new-password"><fmt:message key="confirm_new_password_label" bundle="${changePassword}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="confirm-new-password" type="password" class="form-control" id="confirm-new-password" maxlength="64" /></div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='change_password_button' bundle='${changePassword}' />" />
                            <input id="clear-btn" type="reset" class="btn btn-default" value="<fmt:message key='clear_button' bundle='${changePassword}' />" />
                        </div>
                    </div>

                    </fieldset>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
