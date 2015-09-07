<%@ page session="false"%><%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="langCookie" class="apollo.datastore.utils.CookiesBean" />
<jsp:setProperty name="langCookie" property="varName" value="LANG" />
<jsp:setProperty name="langCookie" property="value" value="${requestScope[langCookie.name]}" />
<fmt:setLocale value="${langCookie.value}" />
<fmt:setBundle basename="apollo.datastore.utils.i18n.UsersBundle" var="usersBundle" />

<jsp:useBean id="errorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="errorVariable" property="varName" value="ERROR" />
<jsp:useBean id="prevCursorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="prevCursorVariable" property="varName" value="PREV_CURSOR" />
<jsp:useBean id="nextCursorVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="nextCursorVariable" property="varName" value="NEXT_CURSOR" />
<jsp:useBean id="usersVariable" class="apollo.datastore.utils.HtmlVariableBean" />
<jsp:setProperty name="usersVariable" property="varName" value="USERS" />

<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title><fmt:message key="title_users" bundle="${usersBundle}" /></title>
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

function populateTbody(users) {
    var length = users.length;
    for(var i = 0; i < length; ++i) {
        var user = users[i];
        var tr = document.createElement('tr');
        var td0 = document.createElement('td');
        $(td0).append(user[0]);
        var td1 = document.createElement('td');
        $(td1).append(user[1]);
        var tr = document.createElement('tr');
        $(tr).append(td0).append(td1);
        usersTbody.append(tr);
    }
}

function updateCounter() {
    var x = cursorList.length - 3;
    var counterText1 = (x * pageSize) - (pageSize - 1);
    var counterText2 = (counterText1 - 1) + usersTbody.children().length;
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
        url: '/auth/admin-users',
        data: '${prevCursorVariable.name}=' + cursorList[cursorList.length - 3]
    }).done(function(data, textStatus, jqXHR) {
        usersTbody.children().remove();
        populateTbody(data['${usersVariable.name}']);
        cursorList.pop();
        cursorList.pop();
        cursorList.push(data['${nextCursorVariable.name}']);
        updateCounter();
        initBtns();
        setTimeout(hideModal, 100);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        loadingModalContentDiv.html('<fmt:message key="loading_failed" bundle="${usersBundle}" />');
        loadingModalContentDiv.addClass('text-danger');
        setTimeout(hideModal, 100);
    });
}

function fetchPrevHandler(event) {
    loadingModal.on('shown.bs.modal', fetchPrevModalHandler);
    loadingModalContentDiv.html('<fmt:message key="loading" bundle="${usersBundle}" />');
    loadingModalContentDiv.removeClass('text-danger');
    loadingModal.modal('show');
}

function fetchNextModalHandler(event) {
    $.ajax({
        cache: false,
        dataType: 'json',
        url: '/auth/admin-users',
        data: '${nextCursorVariable.name}=' + cursorList[cursorList.length - 1]
    }).done(function(data, textStatus, jqXHR) {
        usersTbody.children().remove();
        populateTbody(data['${usersVariable.name}']);
        cursorList.push(data['${nextCursorVariable.name}']);
        updateCounter();
        initBtns();
        setTimeout(hideModal, 500);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        loadingModalContentDiv.html('<fmt:message key="loading_failed" bundle="${usersBundle}" />');
        loadingModalContentDiv.addClass('text-danger');
        setTimeout(hideModal, 500);
    });
}

function fetchNextHandler(event) {
    loadingModal.on('shown.bs.modal', fetchNextModalHandler);
    loadingModalContentDiv.html('<fmt:message key="loading" bundle="${usersBundle}" />');
    loadingModalContentDiv.removeClass('text-danger');
    loadingModal.modal('show');
}

$(document).ready(function() {
    pageSize = ${pageSize};
    cursorList = ${cursorList};
    prevBtn = $('#prev-btn');
    nextBtn = $('#next-btn');
    usersTbody = $('#users-tbody');
    counterSpan = $('#counter');
    prevBtn.on('click', fetchPrevHandler);
    nextBtn.on('click', fetchNextHandler);
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
                        <div class="row"><h3 class="col-xs-12 col-sm-12"><fmt:message key="page_header_users" bundle="${usersBundle}" /></h3></div>
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
                <div class="col-xs-6 col-sm-offset-2 col-sm-4"></div>
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
                            <th><fmt:message key="table_header_user_id" bundle="${usersBundle}" /></th>
                            <th><fmt:message key="table_header_date_created" bundle="${usersBundle}" /></th>
                        </tr>
                        </thead>
                        <tbody id="users-tbody">
<fmt:setTimeZone value="${user.timeZoneLocaleId}" />
<c:forEach var="user1" items="${users}" varStatus="loopCounter" >
                        <tr>
                            <td><c:out value='${user1.userId}' /></td>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss z" value="${user1.dateCreated}" /></td>
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
