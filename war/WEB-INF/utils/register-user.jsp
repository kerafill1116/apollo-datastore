<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.UtilitiesBundle" var="utilities" />
<fmt:setBundle basename="apollo.datastore.i18n.TimeZonesBundle" var="timeZones" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<jsp:useBean id="errorNone" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorNone" property="constant" value="NONE" />
<jsp:setProperty name="errorVariable" property="value" value="${errorNone.code}" />

<jsp:useBean id="userIdVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="userIdVariable" property="varName" value="USER_ID" />
<jsp:useBean id="passwordVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="passwordVariable" property="varName" value="PASSWORD" />
<jsp:useBean id="emailAddressVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="emailAddressVariable" property="varName" value="EMAIL_ADDRESS" />
<jsp:useBean id="timeZoneIdVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="timeZoneIdVariable" property="varName" value="TIME_ZONE_ID" />

<jsp:useBean id="checkUserIdErrorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="checkUserIdErrorVariable" property="varName" value="ERROR" />
<jsp:useBean id="checkUserIdAvailableVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="checkUserIdAvailableVariable" property="varName" value="AVAILABLE" />

<c:if test="${not empty param[errorVariable.name]}">
    <jsp:setProperty name="errorVariable" property="value" value="${param[errorVariable.name]}" />
    <jsp:setProperty name="userIdVariable" property="value" value="${param[userIdVariable.name]}" />
    <jsp:setProperty name="emailAddressVariable" property="value" value="${param[emailAddressVariable.name]}" />
    <jsp:setProperty name="timeZoneIdVariable" property="value" value="${param[timeZoneIdVariable.name]}" />
</c:if>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_register" bundle="${utilities}" /></title>
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
        <!-- misc script, adds userId validator and other things -->
        <script type="text/javascript" src="/js/scripts.js"></script>

<c:if test="${langCookie.value ne jf:defaultLanguage()}">
        <!-- Link messages file for localized validation. -->
        <script type="text/javascript" src="/js/messages_${langCookie.value}.min.js"></script>
        <!-- localization for misc script above -->
        <script type="text/javascript" src="/js/scripts-messages_${langCookie.value}.js"></script>
</c:if>

        <script type="text/javascript">
function inputChangeHandler(event) {
    var validElement = false;
    if(event.target.value.length)
        validElement = registerFormValidator.element(event.target);
    else
        $(event.target).popover('hide');
    if(event.target.id == 'user-id') {
        availableBtn.addClass('hidden');
        unavailableBtn.addClass('hidden');
        errorBtn.addClass('hidden');
        checkBtn.removeClass('hidden');
        if(validElement) {
            checkBtn.removeClass('disabled');
            checkBtn.prop('disabled', false);
        }
        else {
            checkBtn.addClass('disabled');
            checkBtn.prop('disabled', true);
        }
    }
}

function blinkUnavailableBtn() {
    userIdInput.trigger('focus');
    unavailableBtn.addClass('blink');
    setTimeout(function() {
        unavailableBtn.removeClass('blink');
    }, 1500);
}

$(document).ready(function() {
    // globals
    userIdInput = $('#user-id');
    emailAddressInput = $('#email-address');
    registerForm = $('#register-form');

    // hack for select placeholder
    document.getElementById('time-zone-id').options[0].disabled = true;

    checkBtn = $('#check-availability-btn');
    availableBtn = $('#available-btn');
    unavailableBtn = $('#unavailable-btn');
    errorBtn = $('#error-btn');

    checkBtn.click(function () {
        checkBtn.button('loading');
        $.ajax({
            cache: false,
            dataType: 'json',
            url: '/utils/check-user-id',
            data: '${userIdVariable.name}=' + userIdInput.val()
        }).done(function(data, textStatus, jqXHR) {
            checkBtn.button('reset');
            checkBtn.addClass('hidden');
            if(data['${checkUserIdErrorVariable.name}']) {
                availableBtn.addClass('hidden');
                unavailableBtn.addClass('hidden');
                errorBtn.removeClass('hidden');
            }
            else if(data['${checkUserIdAvailableVariable.name}']) {
                availableBtn.removeClass('hidden');
                unavailableBtn.addClass('hidden');
                errorBtn.addClass('hidden');
            }
            else {
                availableBtn.addClass('hidden');
                unavailableBtn.removeClass('hidden');
                errorBtn.addClass('hidden');
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            checkBtn.button('reset');
            checkBtn.addClass('hidden');
            availableBtn.addClass('hidden');
            unavailableBtn.addClass('hidden');
            errorBtn.removeClass('hidden');
        });
    });

    $('#clear-btn').click(function () {
        registerFormValidator.resetForm();
        registerForm[0].reset();
        userIdInput.popover('hide');
        emailAddressInput.popover('hide');
        availableBtn.addClass('hidden');
        unavailableBtn.addClass('hidden');
        errorBtn.addClass('hidden');
        checkBtn.removeClass('hidden');
        if(registerFormValidator.element(userIdInput)) {
            checkBtn.removeClass('disabled');
            checkBtn.prop('disabled', false);
        }
        else {
            checkBtn.addClass('disabled');
            checkBtn.prop('disabled', true);
        }
    });

    registerFormValidator = registerForm.validate({
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
                required: true,
                userid: true
            },
            '${emailAddressVariable.name}': {
                maxlength: 256,
                required: true,
                email: true
            }
        },
        onkeyup: function(element, event) { },
        onfocusout: function(element, event) {
            if(element.value.length) {
                var validElement = registerFormValidator.element(element);
                if(element.id == 'user-id' && !checkBtn.hasClass('hidden')) {
                    availableBtn.addClass('hidden');
                    unavailableBtn.addClass('hidden');
                    errorBtn.addClass('hidden');
                    if(validElement) {
                        checkBtn.removeClass('disabled');
                        checkBtn.prop('disabled', false);
                        checkBtn.trigger('click');
                    }
                    else {
                        checkBtn.addClass('disabled');
                        checkBtn.prop('disabled', true);
                    }
                }
            }
        },
        submitHandler: function(form) {
            if(!checkBtn.hasClass('hidden')) {
                checkBtn.button('loading');
                $.ajax({
                    cache: false,
                    dataType: 'json',
                    url: '/utils/check-user-id',
                    data: '${userIdVariable.name}=' + userIdInput.val()
                }).done(function(data, textStatus, jqXHR) {
                    checkBtn.button('reset');
                    checkBtn.addClass('hidden');
                    if(data['${checkUserIdErrorVariable.name}']) {
                        availableBtn.addClass('hidden');
                        unavailableBtn.addClass('hidden');
                        errorBtn.removeClass('hidden');
                    }
                    else if(data['${checkUserIdAvailableVariable.name}']) {
                        availableBtn.removeClass('hidden');
                        unavailableBtn.addClass('hidden');
                        errorBtn.addClass('hidden');
                        form.submit();
                    }
                    else {
                        availableBtn.addClass('hidden');
                        unavailableBtn.removeClass('hidden');
                        errorBtn.addClass('hidden');
                        blinkUnavailableBtn();
                    }
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    checkBtn.button('reset');
                    checkBtn.addClass('hidden');
                    availableBtn.addClass('hidden');
                    unavailableBtn.addClass('hidden');
                    errorBtn.removeClass('hidden');
                });
                return;
            }
            else if(!unavailableBtn.hasClass('hidden')) {
                blinkUnavailableBtn();
                return;
            }
            form.submit();
        }
    });

    // initialize popovers
    userIdInput.popover({placement: 'bottom', trigger: 'manual', content: '_'});
    emailAddressInput.popover({placement: 'bottom', trigger: 'manual', content: '_'});

    // for validator and autocomplete
    userIdInput.on('input', inputChangeHandler);
    emailAddressInput.on('input', inputChangeHandler);

    // for when back button is pressed on browser
    if(userIdInput.val().length)
        if(registerFormValidator.element(userIdInput)) {
            checkBtn.removeClass('disabled');
            checkBtn.prop('disabled', false);
        }
        else {
            checkBtn.addClass('disabled');
            checkBtn.prop('disabled', true);
        }
<c:if test="${errorVariable.value ne errorNone.code}">
    registerForm.valid();
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
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_register" bundle="${utilities}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">

                    <form name="register-form" method="post" action="/utils/register-user" class="form-horizontal" id="register-form" role="form">

                    <fieldset>
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="user-id"><fmt:message key="user_id_label" bundle="${utilities}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${userIdVariable.name}" type="text" class="form-control" id="user-id" pattern="^[a-zA-Z][a-zA-Z0-9_]{4,31}$" maxlength="32" required autofocus value="<c:out value='${userIdVariable.value}' />" /></div>
                    </div>

                    <div id="check-availability-div" class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <button class="btn btn-default btn-xs btn-success disabled hidden" type="button" id="available-btn" tabindex="-1" disabled>
                                <span class="glyphicon glyphicon-thumbs-up"></span> <fmt:message key="available" bundle="${utilities}" /></button>
                            <button class="btn btn-default btn-xs btn-danger disabled hidden" type="button" id="unavailable-btn" tabindex="-1" disabled>
                                <span class="glyphicon glyphicon-thumbs-down"></span> <fmt:message key="unavailable" bundle="${utilities}" /></button>
                            <button class="btn btn-default btn-xs btn-danger disabled hidden" type="button" id="error-btn" tabindex="-1" disabled>
                                <span class="glyphicon glyphicon-ban-circle"></span> <fmt:message key="error" bundle="${utilities}" /></button>

                            <button class="btn btn-default btn-xs disabled" type="button" id="check-availability-btn" tabindex="-1" data-loading-text="<fmt:message key='checking' bundle='${utilities}' />" disabled>
                                <span class="glyphicon glyphicon-search"></span> <fmt:message key="check_availability" bundle="${utilities}" /></button>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="password"><fmt:message key="password_label" bundle="${utilities}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${passwordVariable.name}" type="password" class="form-control" id="password" maxlength="64" /></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="email-address"><fmt:message key="email_address_label" bundle="${utilities}" /></label>
                        <div class="col-xs-12 col-sm-8"><input name="${emailAddressVariable.name}" type="email" class="form-control" id="email-address" maxlength="256" required value="<c:out value='${emailAddressVariable.value}' />" /></div>
                    </div>

                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="time-zone-id"><fmt:message key="time_zone_label" bundle="${utilities}" /></label>
                        <div class="col-xs-12 col-sm-8">
                            <select name="${timeZoneIdVariable.name}" class="form-control" id="time-zone-id">
                                <option value=""><fmt:message key="time_zone_choose_option" bundle="${utilities}" /></option>
<c:forEach var="timeZone" items="${jf:timeZonesArray()}" varStatus="loopCounter" >
                                <option value="${timeZone.timeZoneId}"${timeZone.timeZoneId eq timeZoneIdVariable.value ? " selected" : ""}><fmt:message key="${timeZone.timeZoneId}" bundle="${timeZones}" /></option>
</c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-12 col-sm-offset-3 col-sm-8">
                            <input id="submit-btn" type="submit" class="btn btn-default" value="<fmt:message key='register_button' bundle='${utilities}' />" />
                            <input id="clear-btn" type="reset" class="btn btn-default" value="<fmt:message key='clear_button' bundle='${utilities}' />" />
                        </div>
                    </div>

<c:if test="${errorVariable.value ne errorNone.code}">
    <fmt:setBundle basename="apollo.datastore.i18n.ErrorMessagesBundle" var="errorMessages" />
    <jsp:useBean id="errorRequiredUserId" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorRequiredUserId" property="constant" value="REQUIRED_USER_ID" />
    <jsp:useBean id="errorInvalidUserId" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInvalidUserId" property="constant" value="INVALID_USER_ID" />
    <jsp:useBean id="errorRequiredEmailAddress" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorRequiredEmailAddress" property="constant" value="REQUIRED_EMAIL_ADDRESS" />
    <jsp:useBean id="errorInvalidEmailAddress" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInvalidEmailAddress" property="constant" value="INVALID_EMAIL_ADDRESS" />
    <jsp:useBean id="errorNotAvailableUserId" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorNotAvailableUserId" property="constant" value="NOT_AVAILABLE_USER_ID" />
    <jsp:useBean id="errorInRegisterNewUser" class="apollo.datastore.utils.ErrorBean" />
    <jsp:setProperty name="errorInRegisterNewUser" property="constant" value="ERROR_IN_REGISTER_NEW_USER" />
    <c:choose>
        <c:when test="${errorVariable.value eq errorRequiredUserId.code}" >
            <fmt:message key="message_error_required_user_id" bundle="${errorMessages}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorInvalidUserId.code}" >
            <fmt:message key="message_error_invalid_user_id" bundle="${errorMessages}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorRequiredEmailAddress.code}" >
            <fmt:message key="message_error_required_email_address" bundle="${errorMessages}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorInvalidEmailAddress.code}" >
            <fmt:message key="message_error_invalid_email_address" bundle="${errorMessages}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorNotAvailableUserId.code}" >
            <fmt:message key="message_error_not_available_user_id" bundle="${errorMessages}" var="errorMessage" />
        </c:when>
        <c:when test="${errorVariable.value eq errorInRegisterNewUser.code}" >
            <fmt:message key="message_error_in_register_new_user" bundle="${errorMessages}" var="errorMessage" />
        </c:when>
        <c:otherwise>
            <fmt:message key="message_error_invalid" bundle="${errorMessages}" var="errorMessage" />
        </c:otherwise>
    </c:choose>
                    <div class="row alert-row"><p class="col-xs-12 col-sm-offset-3 col-sm-8 alert alert-danger">${errorMessage}</p></div>
</c:if>
                    </fieldset>

                    </form>
                </div>
            </div>
        </div>
        </main>
    </body>
</html>