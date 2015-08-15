<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.UtilitiesBundle" var="utilities" />
<fmt:setBundle basename="apollo.datastore.i18n.ErrorMessagesBundle" var="errorMessages" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<c:if test="${not empty param[errorVariable.name]}">
    <jsp:setProperty name="errorVariable" property="value" value="${param[errorVariable.name]}" />
</c:if>

<jsp:useBean id="errorNone" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorNone" property="constant" value="NONE" />
<jsp:useBean id="errorRequiredActivationKey" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorRequiredActivationKey" property="constant" value="REQUIRED_ACTIVATION_KEY" />
<jsp:useBean id="errorInvalidActivationKey" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorInvalidActivationKey" property="constant" value="INVALID_ACTIVATION_KEY" />
<jsp:useBean id="errorNonExistentUser" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorNonExistentUser" property="constant" value="NON_EXISTENT_USER" />
<jsp:useBean id="errorAlreadyActivatedUser" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorAlreadyActivatedUser" property="constant" value="ALREADY_ACTIVATED_USER" />
<jsp:useBean id="errorInActivation" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorInActivation" property="constant" value="ERROR_IN_ACTIVATION" />

<c:choose>
    <c:when test="${errorVariable.value eq errorNone.code}" >
        <fmt:message key="page_header_activate_user_report" bundle="${utilities}" var="pageHeader" />
        <fmt:message key="message_activate_user_report" bundle="${utilities}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorRequiredActivationKey.code}" >
        <fmt:message key="page_header_error" bundle="${utilities}" var="pageHeader" />
        <fmt:message key="message_error_required_activation_key" bundle="${errorMessages}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorInvalidActivationKey.code}" >
        <fmt:message key="page_header_error" bundle="${utilities}" var="pageHeader" />
        <fmt:message key="message_error_invalid_activation_key" bundle="${errorMessages}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorNonExistentUser.code}" >
        <fmt:message key="page_header_error" bundle="${utilities}" var="pageHeader" />
        <fmt:message key="message_error_non_existent_user" bundle="${errorMessages}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorAlreadyActivatedUser.code}" >
        <fmt:message key="page_header_error" bundle="${utilities}" var="pageHeader" />
        <fmt:message key="message_error_already_activated_user" bundle="${errorMessages}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorInActivation.code}" >
        <fmt:message key="page_header_error" bundle="${utilities}" var="pageHeader" />
        <fmt:message key="message_error_in_activation" bundle="${errorMessages}" var="errorMessage" />
    </c:when>
    <c:otherwise>
        <fmt:message key="page_header_error" bundle="${utilities}" var="pageHeader" />
        <fmt:message key="message_error_invalid" bundle="${errorMessages}" var="errorMessage" />
    </c:otherwise>
</c:choose>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_activate_user_report" bundle="${utilities}" /></title>
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
    </head>
    <body>
<%@ include file="/WEB-INF/jspf/pub/top-navbar.jspf" %>
        <div class="page-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-12 col-sm-offset-2 col-sm-8">
                        <div class="row"><h3 class="col-xs-12 col-sm-12">${pageHeader}</h3></div>
                    </div>
                </div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8">

                    <div class="row">
                        <div class="col-xs-12 col-sm-12">${errorMessage}</div>
                    </div>

<c:if test="${errorVariable.value eq errorNone.code}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12"><br /><fmt:message key="redirecting" bundle="${utilities}" /> <span id="seconds">3</span></div>
                    </div>

                    <script type="text/javascript">
$(document).ready(function() {
    signInURL = '/utils/sign-in';
    $('#sign-in-link').attr('href', signInURL);

    // redirect to sign in page after 5 seconds
    delay = 3000;
    setTimeout(function() {
        location.href = signInURL;
    }, 3000);

    seconds = 2;
    redirectInterval = setInterval(function() {
        $('#seconds').html(seconds);
        if(--seconds == 0)
            clearInterval(redirectInterval);
    }, 1000);
});
                    </script>
</c:if>

                </div>
            </div>
        </div>
        </main>

    </body>
</html>

