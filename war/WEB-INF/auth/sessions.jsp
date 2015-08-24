<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.SessionsBundle" var="sessionsBundle" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<jsp:useBean id="prevCursorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="prevCursorVariable" property="varName" value="PREV_CURSOR" />
<jsp:useBean id="nextCursorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="nextCursorVariable" property="varName" value="NEXT_CURSOR" />
<jsp:useBean id="sessionsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="sessionsVariable" property="varName" value="SESSIONS" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_sessions" bundle="${sessionsBundle}" /></title>
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
function hideModal() {
    loadingModal.modal('hide');
    loadingModal.off('shown.bs.modal');
}

function populateTbody(sessions) {
    var length = sessions.length;
    for(var i = 0; i < length; ++i) {
        var session = sessions[i];
        var tr = document.createElement('tr');
        var td0 = document.createElement('td');
        $(td0).append(session[0]);
        var td1 = document.createElement('td');
        $(td1).append(session[1]);
        var td2 = document.createElement('td');
        $(td2).append(session[2]);
        var td3 = document.createElement('td');
        $(td3).append(session[3]);
        $(td3).addClass('text-right');
        var tr = document.createElement('tr');
        $(tr).append(td0).append(td1).append(td2).append(td3);
        sessionsTbody.append(tr);
    }
}

function initBtns() {
    var length = cursorList.length;
    prevBtn.prop('disabled', cursorList[length - 3] == null);
    nextBtn.prop('disabled', cursorList[length - 1] == null);
}

function fetchPrevModalHandler(event) {
    $.ajax({
        cache: false,
        dataType: 'json',
        url: '/auth/sessions',
        data: '${prevCursorVariable.name}=' + cursorList[cursorList.length - 3]
    }).done(function(data, textStatus, jqXHR) {
        sessionsTbody.children().remove();
        populateTbody(data['${sessionsVariable.name}']);
        cursorList.pop();
        cursorList.pop();
        cursorList.push(data['${nextCursorVariable.name}']);
        initBtns();
        setTimeout(hideModal, 100);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        loadingModalContentDiv.html('<fmt:message key="loading_failed" bundle="${sessionsBundle}" />');
        loadingModalContentDiv.addClass('text-danger');
        setTimeout(hideModal, 100);
    });
}

function fetchPrevHandler(event) {
    loadingModal.on('shown.bs.modal', fetchPrevModalHandler);
    loadingModalContentDiv.html('<fmt:message key="loading" bundle="${sessionsBundle}" />');
    loadingModalContentDiv.removeClass('text-danger');
    loadingModal.modal('show');
}

function fetchNextModalHandler(event) {
    $.ajax({
        cache: false,
        dataType: 'json',
        url: '/auth/sessions',
        data: '${nextCursorVariable.name}=' + cursorList[cursorList.length - 1]
    }).done(function(data, textStatus, jqXHR) {
        sessionsTbody.children().remove();
        populateTbody(data['${sessionsVariable.name}']);
        cursorList.push(data['${nextCursorVariable.name}']);
        initBtns();
        setTimeout(hideModal, 500);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        loadingModalContentDiv.html('<fmt:message key="loading_failed" bundle="${sessionsBundle}" />');
        loadingModalContentDiv.addClass('text-danger');
        setTimeout(hideModal, 500);
    });
}

function fetchNextHandler(event) {
    loadingModal.on('shown.bs.modal', fetchNextModalHandler);
    loadingModalContentDiv.html('<fmt:message key="loading" bundle="${sessionsBundle}" />');
    loadingModalContentDiv.removeClass('text-danger');
    loadingModal.modal('show');
}

$(document).ready(function() {
    cursorList = ${cursorList};
    prevBtn = $('#prev-btn');
    nextBtn = $('#next-btn');
    sessionsTbody = $('#sessions-tbody');
    prevBtn.on('click', fetchPrevHandler);
    nextBtn.on('click', fetchNextHandler);
    initBtns();
    loadingModal = $('#loading-modal');
    loadingModal.modal({
        backdrop: 'static',
        keyboard: false,
        show: false
    });
    loadingModalContentDiv = $('#loading-modal-content-div');
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
                        <div class="row"><h3 class="col-xs-12 col-sm-12"><fmt:message key="page_header_sessions" bundle="${sessionsBundle}" /></h3></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="loading-modal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-sm">
                <div class="modal-content" id="loading-modal-content-div"></div>
            </div>
        </div>

        <main role="main">
        <div class="container-fluid">

            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 text-right"><p>
                    <button id="prev-btn" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-left"></span></button>
                    <button id="next-btn" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-right"></span></button>
                </p></div>
            </div>

            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 table-responsive">
                    <table class="table table-condensed">
                        <thead>
                        <tr>
                            <th><fmt:message key="table_header_session_id" bundle="${sessionsBundle}" /></th>
                            <th><fmt:message key="table_header_date_signed_in" bundle="${sessionsBundle}" /></th>
                            <th><fmt:message key="table_header_last_session_check" bundle="${sessionsBundle}" /></th>
                            <th class="text-right"><fmt:message key="table_header_session_timeout" bundle="${sessionsBundle}" /></th>
                        </tr>
                        </thead>
                        <tbody id="sessions-tbody">
<fmt:setTimeZone value="${user.dateFormatId}" />
<c:forEach var="session" items="${sessions}" varStatus="loopCounter" >
                        <tr>
                            <td><c:out value='${fn:substring(session.sessionId, 0, 32)}' />...</td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${session.dateSignedIn}" /></td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${session.lastSessionCheck}" /></td>
                            <td class="text-right"><c:out value='${session.sessionTimeout}' /></td>
                        </tr>
</c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        </main>
    </body>
</html>
