<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.UtilitiesBundle" var="utilitiesBundle" />
<fmt:setBundle basename="apollo.datastore.i18n.ErrorMessagesBundle" var="errorMessagesBundle" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<c:if test="${not empty requestScope[errorVariable.name]}">
    <jsp:setProperty name="errorVariable" property="value" value="${requestScope[errorVariable.name]}" />
</c:if>

<jsp:useBean id="errorNone" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorNone" property="constant" value="NONE" />
<jsp:useBean id="errorRequiredUserId" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorRequiredUserId" property="constant" value="REQUIRED_USER_ID" />
<jsp:useBean id="errorRequiredRequestId" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorRequiredRequestId" property="constant" value="REQUIRED_REQUEST_ID" />
<jsp:useBean id="errorNonExistentRequest" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorNonExistentRequest" property="constant" value="NON_EXISTENT_REQUEST" />
<jsp:useBean id="errorExpiredRequest" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorExpiredRequest" property="constant" value="EXPIRED_REQUEST" />
<jsp:useBean id="errorNonExistentUser" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorNonExistentUser" property="constant" value="NON_EXISTENT_USER" />
<jsp:useBean id="errorNotActivatedUser" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorNotActivatedUser" property="constant" value="NOT_ACTIVATED_USER" />
<jsp:useBean id="errorDisabledUser" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorDisabledUser" property="constant" value="DISABLED_USER" />
<jsp:useBean id="errorInResetPassword" class="apollo.datastore.utils.ErrorBean" />
<jsp:setProperty name="errorInResetPassword" property="constant" value="ERROR_IN_RESET_PASSWORD" />

<c:choose>
    <c:when test="${errorVariable.value eq errorNone.code}" >
        <fmt:message key="page_header_reset_password_report" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_reset_password_report" bundle="${utilitiesBundle}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorRequiredUserId.code}" >
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_required_user_id" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorRequiredRequestId.code}" >
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_required_request_id" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorNonExistentRequest.code}" >
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_non_existent_request" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorExpiredRequest.code}" >
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_expired_request" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorNonExistentUser.code}" >
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_non_existent_user" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorNotActivatedUser.code}" >
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_not_activated_user" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorDisabledUser.code}" >
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_disabled_user" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:when>
    <c:when test="${errorVariable.value eq errorInResetPassword.code}" >
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_in_reset_password" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:when>
    <c:otherwise>
        <fmt:message key="page_header_error" bundle="${utilitiesBundle}" var="pageHeader" />
        <fmt:message key="message_error_invalid" bundle="${errorMessagesBundle}" var="errorMessage" />
    </c:otherwise>
</c:choose>

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_reset_password_report" bundle="${utilitiesBundle}" /></title>
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

                </div>
            </div>
        </div>
        </main>

    </body>
</html>
