<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:useBean id="langCookie" class="apollo.datastore.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.i18n.SessionLogsBundle" var="sessionLogsBundle" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<jsp:useBean id="prevCursorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="prevCursorVariable" property="varName" value="PREV_CURSOR" />
<jsp:useBean id="nextCursorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="nextCursorVariable" property="varName" value="NEXT_CURSOR" />
<jsp:useBean id="sessionLogsVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="sessionLogsVariable" property="varName" value="SESSION_LOGS" />
<jsp:useBean id="sessionIdVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="sessionIdVariable" property="varName" value="SESSION_ID" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_session_logs" bundle="${sessionLogsBundle}" /></title>
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

function populateTbody(sessionLogs) {
    var length = sessionLogs.length;
    for(var i = 0; i < length; ++i) {
        var sessionLog = sessionLogs[i];
        var tr = document.createElement('tr');
        var td0 = document.createElement('td');
        $(td0).append(sessionLog[0]);
        var td1 = document.createElement('td');
        $(td1).append(sessionLog[1]);
        var td2 = document.createElement('td');
        $(td2).append(sessionLog[2]);
        $(td2).addClass('text-right');
        var td3 = document.createElement('td');
        $(td3).append(sessionLog[3]);
        var td4 = document.createElement('td');
        $(td4).append(sessionLog[4]);
        var tr = document.createElement('tr');
<c:if test="${userPermissions.removeSessionLogs}">
        var tda = document.createElement('td');
        var checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.name = '${sessionIdVariable.name}';
        checkbox.value = sessionLog[5];
        $(tda).append(checkbox);
        $(tr).append(tda);
</c:if>
        $(tr).append(td0).append(td1).append(td2).append(td3).append(td4);
        sessionLogsTbody.append(tr);
    }
}

function updateCounter() {
    var x = cursorList.length - 3;
    var counterText1 = (x * pageSize) - (pageSize - 1);
    var counterText2 = (counterText1 - 1) + sessionLogsTbody.children().length;
    counterSpan.html(counterText1 + ' - ' + counterText2);
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
        url: '/auth/session-logs',
        data: '${prevCursorVariable.name}=' + cursorList[cursorList.length - 3]
    }).done(function(data, textStatus, jqXHR) {
        sessionLogsTbody.children().remove();
        populateTbody(data['${sessionLogsVariable.name}']);
        cursorList.pop();
        cursorList.pop();
        cursorList.push(data['${nextCursorVariable.name}']);
        updateCounter();
        initBtns();
        setTimeout(hideModal, 100);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        loadingModalContentDiv.html('<fmt:message key="loading_failed" bundle="${sessionLogsBundle}" />');
        loadingModalContentDiv.addClass('text-danger');
        setTimeout(hideModal, 100);
    });
}

function fetchPrevHandler(event) {
    loadingModal.on('shown.bs.modal', fetchPrevModalHandler);
    loadingModalContentDiv.html('<fmt:message key="loading" bundle="${sessionLogsBundle}" />');
    loadingModalContentDiv.removeClass('text-danger');
    loadingModal.modal('show');
}

function fetchNextModalHandler(event) {
    $.ajax({
        cache: false,
        dataType: 'json',
        url: '/auth/session-logs',
        data: '${nextCursorVariable.name}=' + cursorList[cursorList.length - 1]
    }).done(function(data, textStatus, jqXHR) {
        sessionLogsTbody.children().remove();
        populateTbody(data['${sessionLogsVariable.name}']);
        cursorList.push(data['${nextCursorVariable.name}']);
        updateCounter();
        initBtns();
        setTimeout(hideModal, 500);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        loadingModalContentDiv.html('<fmt:message key="loading_failed" bundle="${sessionLogsBundle}" />');
        loadingModalContentDiv.addClass('text-danger');
        setTimeout(hideModal, 500);
    });
}

function fetchNextHandler(event) {
    loadingModal.on('shown.bs.modal', fetchNextModalHandler);
    loadingModalContentDiv.html('<fmt:message key="loading" bundle="${sessionLogsBundle}" />');
    loadingModalContentDiv.removeClass('text-danger');
    loadingModal.modal('show');
}

$(document).ready(function() {
    pageSize = ${pageSize};
    cursorList = ${cursorList};
    prevBtn = $('#prev-btn');
    nextBtn = $('#next-btn');
    sessionLogsTbody = $('#session-logs-tbody');
    counterSpan = $('#counter');
    prevBtn.on('click', fetchPrevHandler);
    nextBtn.on('click', fetchNextHandler);
<c:if test="${userPermissions.removeSessionLogs}">
    removeBtn = $('#remove-btn');
    removeBtn.on('click', function(event) {
        var checkedBoxes = sessionLogsTbody.find(':checked');
        var sessionIds = [];
        for(var i = 0; i < checkedBoxes.length; ++i)
            sessionIds[i] = checkedBoxes[i].value;
        loadingModal.on('shown.bs.modal', function(event) {
            $.ajax({
                cache: false,
                dataType: 'json',
                type : 'POST',
                url: '/auth/session-logs',
                data: { '${sessionIdVariable.name}' : sessionIds }
            }).done(function(data, textStatus, jqXHR) {
                for(var sessionId in data) if(data.hasOwnProperty(sessionId)) {
                    var removeCheckbox = sessionLogsTbody.find(':checkbox[value=' + sessionId + ']');
                    if(removeCheckbox.length == 1)
                        removeCheckbox.closest('tr').remove();
                }
                updateCounter();
                setTimeout(hideModal, 500);
            }).fail(function(jqXHR, textStatus, errorThrown) {
                loadingModalContentDiv.html('<fmt:message key="remove_failed" bundle="${sessionLogsBundle}" />');
                loadingModalContentDiv.addClass('text-danger');
                setTimeout(hideModal, 500);
            });
        });
        loadingModalContentDiv.html('<fmt:message key="removing" bundle="${sessionLogsBundle}" />');
        loadingModalContentDiv.removeClass('text-danger');
        loadingModal.modal('show');
    });
    checkAllCheckbox = $('#check-all');
    checkAllCheckbox.on('change', function(event) {
        sessionLogsTbody.find(':checkbox').prop('checked', event.currentTarget.checked).trigger('change');
    });
    updateCounter2 = updateCounter;
    updateCounter = function() {
        updateCounter2();
        sessionLogsTbody.find('tr').on('click', function(event) {
            if(event.target.type != 'checkbox') {
                var checkbox = $(event.currentTarget).find(':checkbox');
                var checked = checkbox.prop('checked');
                checkbox.prop('checked', !checked).trigger('change');
            }
        });
        sessionLogsTbody.find(':checkbox').on('change', function(event) {
            if(sessionLogsTbody.find(':checked').length > 0) {
                removeBtn.removeClass('disabled');
                removeBtn.prop('disabled', false);
            }
            else {
                removeBtn.addClass('disabled');
                removeBtn.prop('disabled', true);
            }
        });
        checkAllCheckbox.prop('checked', false).trigger('change');
    };
</c:if>
    updateCounter();
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
                        <div class="row"><h3 class="col-xs-12 col-sm-12"><fmt:message key="page_header_session_logs" bundle="${sessionLogsBundle}" /></h3></div>
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
                <div class="col-xs-6 col-sm-offset-2 col-sm-4"><p>
<c:if test="${userPermissions.removeSessionLogs}">
                    <button id="remove-btn" type="button" class="btn btn-default btn-sm disabled" disabled><span class="glyphicon glyphicon-remove"></span><span class="hidden-xs"> <fmt:message key="remove_session_logs_button" bundle="${sessionLogsBundle}" /></span></button>
</c:if>
                </p></div>
                <div class="col-xs-6 col-sm-4 text-right"><p><span id="counter"></span>
                    <button id="prev-btn" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-left"></span></button>
                    <button id="next-btn" type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-chevron-right"></span></button>
                </p></div>
            </div>

            <div class="row">
                <div class="col-xs-12 col-sm-offset-2 col-sm-8 table-responsive">
                    <table class="table table-condensed table-hover">
                        <thead>
                        <tr>
<c:if test="${userPermissions.removeSessionLogs}">
                            <th><input id="check-all" type="checkbox" /></th>
</c:if>
                            <th><fmt:message key="table_header_session_id" bundle="${sessionLogsBundle}" /></th>
                            <th><fmt:message key="table_header_date_signed_in" bundle="${sessionLogsBundle}" /></th>
                            <th class="text-right"><fmt:message key="table_header_session_timeout" bundle="${sessionLogsBundle}" /></th>
                            <th><fmt:message key="table_header_date_signed_out" bundle="${sessionLogsBundle}" /></th>
                            <th><fmt:message key="table_header_cause_of_disconnect" bundle="${sessionLogsBundle}" /></th>
                        </tr>
                        </thead>
                        <tbody id="session-logs-tbody">
<fmt:setTimeZone value="${user.dateFormatId}" />
<c:choose>
    <c:when test="${userPermissions.removeSessionLogs}">
        <c:forEach var="sessionLog" items="${sessionLogs}" varStatus="loopCounter" >
                        <tr>
                            <td><input name="${sessionIdVariable.name}" type="checkbox" value="<c:out value='${sessionLog.sessionId}' />" /></td>
                            <td><c:out value='${fn:substring(sessionLog.sessionId, 0, 32)}' />...</td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${sessionLog.dateSignedIn}" /></td>
                            <td class="text-right"><c:out value='${sessionLog.sessionTimeout}' /></td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${sessionLog.dateSignedOut}" /></td>
                            <td><c:out value='${sessionLog.causeOfDisconnect}' /></td>
                        </tr>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <c:forEach var="sessionLog" items="${sessionLogs}" varStatus="loopCounter" >
                        <tr>
                            <td><c:out value='${fn:substring(sessionLog.sessionId, 0, 32)}' />...</td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${sessionLog.dateSignedIn}" /></td>
                            <td class="text-right"><c:out value='${sessionLog.sessionTimeout}' /></td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${sessionLog.dateSignedOut}" /></td>
                            <td><c:out value='${sessionLog.causeOfDisconnect}' /></td>
                        </tr>
        </c:forEach>
    </c:otherwise>
</c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        </main>
    </body>
</html>
