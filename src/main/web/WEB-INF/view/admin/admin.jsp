<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-gb" lang="en-gb" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Работа с БД</title>

    <!-- css -->
    <spring:url value="/resources/css/bootstrap.min.css" var="css"/>
    <link rel="stylesheet" href="${css}">
    <spring:url value="/resources/css/style.css" var="css"/>
    <link rel="stylesheet" href="${css}">
    <link href="https://fonts.googleapis.com/css?family=Roboto+Slab" rel="stylesheet">

    <!-- js -->
    <spring:url value="/resources/js/jquery.js" var="js"/>
    <script src="${js}"></script>
    <spring:url value="/resources/js/bootstrap.min.js" var="js"/>
    <script src="${js}"></script>

    <!--custom functions-->
    <spring:url value="/resources/js/web.account.functions.js" var="js"/>
    <script src="${js}"></script>
</head>
<body>

<!-- навигационная панель и модальное окно -->
<jsp:include page="/WEB-INF/view/tags/nav-panel.jsp"></jsp:include>

<script language="javascript" type="text/javascript">
    $(document).ready(function () {

    })
</script>

<div class="content container-fluid wam-radius wam-min-height-0">
    <input id="_csrf_token" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <textarea id="response" name="response" style="display: none;">${response}</textarea>
    <div class='row'>
        <div class="container-fluid wam-not-padding-xs">
            <div class="panel panel-default wam-margin-left-1 wam-margin-right-1 wam-margin-top-1 ">
                <div class="panel-heading wam-page-title">
                    <h3 class="wam-margin-bottom-0 wam-margin-top-0">Редактирование данных в БД</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-12 col-md-12">
                            <p class='text-justify'>Здесь вы можете очистить БД и заполнить ее заново. Будьте предусмотрительны - операции очень длительные по времени.</p>
                            <p class='text-justify'>Повторное нажатие кнопки "Заполнить" до завершения предыдущей итерации приведет к нарушению валидации на уровне БД - нарушение уникальности генерируемого поля account.acct. Но импорт должен корректно остановиться.</p>
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <button class="btn-lg btn-primary btn-block wam-btn-2" type="submit"
                                    onclick="location.href='delete'">
                                <span class='wam-font-size-2'>Очистить</span>
                            </button>
                        </div>
                        <div class="col-xs-12 col-md-6 ">
                            <button class="btn-lg btn-primary btn-block wam-btn-2" type="submit"
                                    onclick="location.href='mock'">
                                <span class='wam-font-size-2'>Заполнить</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>