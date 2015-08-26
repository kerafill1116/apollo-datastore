<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl-functions.tld" prefix="jf" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.SettingsBundle" var="settingsBundle" />
<fmt:setBundle basename="apollo.datastore.i18n.TimeZonesBundle" var="timeZonesBundle" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_settings" bundle="${settingsBundle}" /></title>
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
<c:if test="${userPermissions.viewMaxSessions and userPermissions.changeMaxSessions and userPermissions.viewExclusiveSession and userPermissions.changeExclusiveSession}">
    if(event.currentTarget.id == 'max-sessions') {
        var disabled = (event.currentTarget.value != 1);
        exclusiveSessionCheckbox.prop('disabled', disabled);
        if(disabled)
            exclusiveSessionCheckbox.closest('label').addClass('text-muted').closest('.checkbox').addClass('disabled');
        else
            exclusiveSessionCheckbox.closest('label').removeClass('text-muted').closest('.checkbox').removeClass('disabled');
    }
</c:if>
    settingsFormValidator.element(event.currentTarget);
}

function hideModal() {
    updateSettingModal.modal('hide');
    updateSettingModal.off('shown.bs.modal');
}

function updateSettingHandler(event) {
    updateSetting(event.currentTarget);
}

function updateSettingModalHandler(element, elementValue) {
    $.ajax({
        cache: false,
        dataType: 'json',
        url: '/auth/settings',
        data: element.name + '=' + elementValue
    }).done(function(data, textStatus, jqXHR) {
        if(!data['${errorVariable.name}']) {
            updateSettingModalContentDiv.html('<fmt:message key="updated" bundle="${settingsBundle}" />');
            updateSettingModalContentDiv.addClass('text-success');
            if(!(element.type == 'checkbox'))
                $(element).data('oldVal', elementValue);
        }
        else {
            updateSettingModalContentDiv.html('<fmt:message key="update_failed" bundle="${settingsBundle}" />');
            updateSettingModalContentDiv.addClass('text-danger');
        }
        setTimeout(hideModal, 100);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        updateSettingModalContentDiv.html('<fmt:message key="update_failed" bundle="${settingsBundle}" />');
        updateSettingModalContentDiv.addClass('text-danger');
        setTimeout(hideModal, 100);
    });
}

function updateSetting(element) {
    var inputElement = $(element);
    var isCheckbox = (element.type == 'checkbox');
    if(!isCheckbox && (inputElement.data('oldVal') == inputElement.val()))
        return;
    var elementValue = element.value;
    if(isCheckbox && !element.checked)
        elementValue = '0';

    updateSettingModal.on('shown.bs.modal', function(event) {
        updateSettingModalHandler(element, elementValue);
    });
    updateSettingModalContentDiv.html('<fmt:message key="updating" bundle="${settingsBundle}" />');
    updateSettingModalContentDiv.removeClass('text-danger');
    updateSettingModalContentDiv.removeClass('text-success');
    updateSettingModal.modal('show');
}

$(document).ready(function() {
    // globals
    settingsForm = $('#settings-form');
<c:if test="${userPermissions.viewTimeZone and userPermissions.changeTimeZone}">
    // hack for select placeholder
    document.getElementById('time-zone-id').options[0].disabled = true;
    $('#time-zone-id').on('change', updateSettingHandler);
</c:if>
    settingsFormValidator = settingsForm.validate({
        errorPlacement: function(error, element) { },
        highlight: function(element, errorClass, validClass) { },
        unhighlight: function(element, errorClass, validClass) {
            var popoverElement = $(element);
            if(element.id == 'session-timeout')
                popoverElement = popoverElement.parent();
            popoverElement.popover('hide');
        },
        showErrors: function(errorMap, errorList) {
            for(var i = 0; i < errorList.length; i++) {
                var errorListItem = $(errorList[i].element);
                if(errorList[i].element.id == 'session-timeout')
                    errorListItem = errorListItem.parent();
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
<c:if test="${userPermissions.viewSessionTimeout and userPermissions.changeSessionTimeout}">
    <jsp:useBean id="sessionTimeoutVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="sessionTimeoutVariable" property="varName" value="SESSION_TIMEOUT" />
            '${sessionTimeoutVariable.name}': {
                min: 0,
                number: true,
                required: true
            }
    <c:set var="addComma" value="," />
</c:if>
<c:if test="${userPermissions.viewMaxSessions and userPermissions.changeMaxSessions}">
    <jsp:useBean id="maxSessionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="maxSessionsVariable" property="varName" value="MAX_SESSIONS" />
            ${addComma}'${maxSessionsVariable.name}': {
                min: 0,
                number: true,
                required: true
            }
    <c:set var="addComma" value="," />
</c:if>
<c:if test="${userPermissions.viewMaxFailedAttempts and userPermissions.changeMaxFailedAttempts}">
    <jsp:useBean id="maxFailedAttemptsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
    <jsp:setProperty name="maxFailedAttemptsVariable" property="varName" value="MAX_FAILED_ATTEMPTS" />
            ${addComma}'${maxFailedAttemptsVariable.name}': {
                min: -1,
                number: true,
                required: true
            }
</c:if>
        },
        onkeyup: function(element, event) { },
        onfocusout: function(element, event) {
            if(element.id != 'time-zone-id' && element.id != 'exclusive-session' && settingsFormValidator.element(element))
                updateSetting(element);
        }
    });

<c:if test="${userPermissions.viewSessionTimeout and userPermissions.changeSessionTimeout}">
    sessionTimeoutInput = $('#session-timeout');
    $('#session-timeout-input-group').popover({placement: 'bottom', trigger: 'manual', content: '_'});
    sessionTimeoutInput.on('input', inputChangeHandler);
    sessionTimeoutInput.data('oldVal', sessionTimeoutInput.val());
</c:if>
<c:if test="${userPermissions.viewMaxSessions and userPermissions.changeMaxSessions}">
    maxSessionsInput = $('#max-sessions');
    maxSessionsInput.popover({placement: 'bottom', trigger: 'manual', content: '_'});
    maxSessionsInput.on('input', inputChangeHandler);
    maxSessionsInput.data('oldVal', maxSessionsInput.val());
</c:if>
<c:if test="${userPermissions.viewMaxFailedAttempts and userPermissions.changeMaxFailedAttempts}">
    maxFailedAttemptsInput = $('#max-failed-attempts');
    maxFailedAttemptsInput.popover({placement: 'bottom', trigger: 'manual', content: '_'});
    maxFailedAttemptsInput.on('input', inputChangeHandler);
    maxFailedAttemptsInput.data('oldVal', maxFailedAttemptsInput.val());
</c:if>
<c:if test="${userPermissions.viewExclusiveSession and userPermissions.changeExclusiveSession}">
    exclusiveSessionCheckbox = $('#exclusive-session');
    exclusiveSessionCheckbox.on('change', updateSettingHandler);
</c:if>

    updateSettingModal = $('#update-setting-modal');
    updateSettingModal.modal({
        backdrop: 'static',
        keyboard: false,
        show: false
    });
    updateSettingModalContentDiv = $('#update-setting-modal-content-div');
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
                        <div class="row"><h3 class="col-xs-12 col-sm-offset-3 col-sm-9"><fmt:message key="page_header_settings" bundle="${settingsBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="update-setting-modal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-sm">
                <div class="modal-content" id="update-setting-modal-content-div"></div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">

                    <form name="settings-form" class="form-horizontal" id="settings-form" role="form">
                    <fieldset>
<c:if test="${userPermissions.viewEmailAddress}">
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="email-address"><fmt:message key="email_address_label" bundle="${settingsBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><p id="email-address" class="form-control-static"><c:out value="${user.emailAddress}" /></p></div>
                    </div>
</c:if>
<c:if test="${userPermissions.viewTimeZone}">
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="time-zone-id"><fmt:message key="time_zone_label" bundle="${settingsBundle}" /></label>
                        <div class="col-xs-12 col-sm-8">
    <c:set var="timeZoneId" value="${(user.timeZoneKey ne null) ? user.timeZoneKey.name : null}" />
    <c:choose>
        <c:when test="${userPermissions.changeTimeZone}">
            <jsp:useBean id="timeZoneIdVariable" class="apollo.datastore.utils.HtmlVariableBean" />
            <jsp:setProperty name="timeZoneIdVariable" property="varName" value="TIME_ZONE_ID" />
                            <select name="${timeZoneIdVariable.name}" class="form-control" id="time-zone-id" >
                                <option value=""><fmt:message key="time_zone_choose_option" bundle="${settingsBundle}" /></option>
            <c:forEach var="timeZone" items="${jf:timeZonesArray()}" varStatus="loopCounter" >
                                <option value="${timeZone.timeZoneId}"${timeZone.timeZoneId eq timeZoneId ? " selected" : ""}><fmt:message key="${timeZone.timeZoneId}" bundle="${timeZonesBundle}" /></option>
            </c:forEach>
                            </select>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${timeZoneId ne null}">
                            <p id="time-zone-id" class="form-control-static"><fmt:message key="${timeZoneId}" bundle="${timeZonesBundle}" /></p>
                </c:when>
                <c:otherwise>
                            <p id="time-zone-id" class="form-control-static"><fmt:message key="undefined" bundle="${settingsBundle}" /></p>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
                        </div>
                    </div>
</c:if>

<fmt:message key="yes" bundle="${settingsBundle}" var="fmtYes"/>
<fmt:message key="no" bundle="${settingsBundle}" var="fmtNo"/>
<c:if test="${userPermissions.viewSessionTimeout or userPermissions.viewMaxSessions or (userPermissions.viewExclusiveSession and (user.maxSessions eq 1))}">
                    <div class="panel panel-default">
                        <div class="panel-heading"><fmt:message key="session" bundle="${settingsBundle}" /></div>
                        <div class="panel-body">
    <c:if test="${userPermissions.viewSessionTimeout}">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="session-timeout"><fmt:message key="session_timeout_label" bundle="${settingsBundle}" /></label>
                                <div class="col-xs-12 col-sm-8">
        <c:choose>
            <c:when test="${userPermissions.changeSessionTimeout}">
                                    <div class="input-group" id="session-timeout-input-group">
                                        <input name="${sessionTimeoutVariable.name}" type="number" min="0" class="form-control" id="session-timeout" required value="<c:out value='${user.sessionTimeout}' />" />
                                        <span class="input-group-addon"><fmt:message key="seconds" bundle="${settingsBundle}" /></span>
                                    </div>
            </c:when>
            <c:otherwise>
                                    <p id="session-timeout" class="form-control-static">${user.sessionTimeout} <fmt:message key="seconds" bundle="${settingsBundle}" /></p>
            </c:otherwise>
        </c:choose>
                                </div>
                            </div>
    </c:if>
    <c:if test="${userPermissions.viewMaxSessions}">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="max-sessions"><fmt:message key="max_sessions_label" bundle="${settingsBundle}" /></label>
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
                                <div class="col-xs-12 col-sm-offset-3 col-sm-8 checkbox${(user.maxSessions eq 1) ? '' : ' disabled'}">
                                    <label for="exclusive-session" class="${(user.maxSessions eq 1) ? '' : 'text-muted'}"><input${(user.maxSessions eq 1) ? '' : ' disabled'} name="${exclusiveSessionVariable.name}" id="exclusive-session" type="checkbox" value="1"${user.exclusiveSession ? " checked" : ""} />
                                    <strong><fmt:message key="exclusive_session_checkbox_label" bundle="${settingsBundle}" /></strong></label>
                                </div>
            </c:when>
            <c:otherwise>
                                <label class="col-xs-12 col-sm-3 control-label" for="exclusive-session"><fmt:message key="exclusive_session_label" bundle="${settingsBundle}" /></label>
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
                        <div class="panel-heading"><fmt:message key="security" bundle="${settingsBundle}" /></div>
                        <div class="panel-body">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="max-failed-attempts"><fmt:message key="max_failed_attempts_label" bundle="${settingsBundle}" /></label>
                                <div class="col-xs-12 col-sm-8">
    <c:choose>
        <c:when test="${userPermissions.changeMaxFailedAttempts}">
                                    <input name="${maxFailedAttemptsVariable.name}" type="number" min="-1" class="form-control" id="max-failed-attempts" required value="<c:out value='${user.maxFailedAttempts}' />" />
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
                        <div class="panel-heading"><fmt:message key="status" bundle="${settingsBundle}" /></div>
                        <div class="panel-body">
    <c:if test="${userPermissions.viewDisabledStatus}">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="disabled"><fmt:message key="disabled_label" bundle="${settingsBundle}" /></label>
                                <div class="col-xs-12 col-sm-8"><p id="disabled" class="form-control-static">${user.disabled ? fmtYes : fmtNo}</p></div>
                            </div>
    </c:if>
    <c:if test="${userPermissions.viewActivatedStatus}">
                            <div class="form-group">
                                <label class="col-xs-12 col-sm-3 control-label" for="activated"><fmt:message key="activated_label" bundle="${settingsBundle}" /></label>
                                <div class="col-xs-12 col-sm-8"><p id="activated" class="form-control-static">${user.activated ? fmtYes : fmtNo}</p></div>
                            </div>
    </c:if>
                        </div>
                    </div>
</c:if>
<fmt:setTimeZone value="${user.dateFormatId}" />
                    <div class="form-group">
                        <label class="col-xs-12 col-sm-3 control-label" for="date-created"><fmt:message key="date_created_label" bundle="${settingsBundle}" /></label>
                        <div class="col-xs-12 col-sm-8"><p id="date-created" class="form-control-static"><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${user.dateCreated}" /></p></div>
                    </div>

                    </fieldset>
                    </form>
                </div>
            </div>
        </div>
        </main>
    </body>
</html>
