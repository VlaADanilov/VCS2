<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 25.09.2024
  Time: 13:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/menu.css">
<!DOCTYPE html>
<html lang="ru">
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/pages/ico.png" type="image/png">
    <meta charset="utf-8" />
    <title>VladCarService</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/forMenu.js"></script>
</head>
<body>
<div id="wrapper"> <!--Оболочка страницы-->
    <!--Шапка сайта-->
    <div id="header">
        <!--Заголовок сайта-->
        <h1>Автомаркет</h1>
        <!--Описание (телефон)-->
        <p class="nomer"> +7 953 015 62 18</p>
        <!--Фоновая картинка в шапке сайта-->
        <img class = "logo" src="${pageContext.request.contextPath}/icons/логотип-без-фона.png">
        <img class = "fone" src="${pageContext.request.contextPath}/pages/XXL.jpg">
    </div>
    <!--Сайдбар-->
    <div id="sidebar">
        <!--меню-->
        <h3>На нашем сайте</h3>
        <!--Картинки маркеров меню (галочки)-->
        <p><img class="marcer" src="${pageContext.request.contextPath}/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/list">Автомобили</a></p>
        <p><img class="marcer" src="${pageContext.request.contextPath}/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/all_users">Список пользователей</a></p>
        <p><img class="marcer" src="${pageContext.request.contextPath}/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/list_of_emp">Сотрудники</a></p>
        <!--Прямая синяя линия-->
        <hr width="50" color="#037FFC" size="5">
        <!--Общая информация в сайдбаре-->
        <c:if test="${sessionScope.get('username') == null}">
            <c:if test="${error != null}">
                <p style="color: red">
                        ${error}
                </p>
            </c:if>
            <h3>Аутентификация</h3>
            <form method="post" action="${pageContext.servletContext.contextPath}/profile">
                <label for="name">Имя пользователя:
                    <input type="text" id="name" name="name" placeholder="Your name.." required>
                </label>
                <label for="password">Пароль:
                    <input type="password" id="password" name="password" placeholder="Your password.." required>
                </label>
                <input type="submit" value="Войти">
            </form>
            <button onclick='openForm()'>Регистрация</button>
            <div id='req'>
                <form role="form" action="${pageContext.servletContext.contextPath}/registr" autocomplete="off" method="POST">
                    <label>Имя пользователя:</label>
                    <input type="text" name="nameReq">
                    <label>Пароль:</label>
                    <input type="password" name="passwordReq">
                    <label>Номер телефона(Вводить без +7):</label>
                    <input type="text" name="phoneReq" pattern="[9]{1}[0-9]{9}">
                    <button type="submit" class="btn btn-success">Зарегистрироваться</button>
                </form>
                <button onclick='closeForm()'>Закрыть</button>
            </div>
        </c:if>
        <c:if test="${sessionScope.get('username') != null}">
            <h3>Здравствуйте! ${sessionScope.get('username')}<img src = "${pageContext.request.contextPath}/icons/delete-1487-svgrepo-com.svg" id = 'delete_polz' onclick="location.href='${pageContext.servletContext.contextPath}/delete_my_account'"></h3>
            <p><img class="marcer" src="${pageContext.request.contextPath}/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/my_cars">Мои объявления</a></p>
            <p><img class="marcer" src="${pageContext.request.contextPath}/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/my_likes">Избранные</a></p>
            <p><img class="marcer" src="${pageContext.request.contextPath}/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/add">Добавить авто</a></p>
            <c:if test="${!sessionScope.get('status').equals('default')}">
                <p><img class="marcer" src="${pageContext.request.contextPath}/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/list_of_reports">Жалобы</a></p>
            </c:if>
            <form method="post" action="${pageContext.servletContext.contextPath}/exit">
                <button type = submit>Выход</button>
            </form>

        </c:if>

    </div>
    <!--Основной контент (статья)-->
    <div id="content">
        <!--Картинка слева-->
        <img class="left" src="${pageContext.request.contextPath}/pages/left_page.jpg">
        <!--Заголовок статьи-->
        <h3>О нашей работе</h3>
        <!--Текст статьи-->
        <p>
            Добро пожаловать в Автомаркет VCS — ваш надежный партнер в мире автомобилей! Наша компания была основана с одной целью: сделать процесс покупки и продажи автомобилей максимально удобным для наших клиентов. Мы понимаем, что выбор автомобиля — это важное решение, и стремимся предоставить вам все необходимые инструменты для его принятия.
        </p>
        <p>
            В Автомаркете VCS мы предлагаем широкий ассортимент автомобилей: от новых моделей до подержанных авто, прошедших тщательную проверку. Наша команда опытных специалистов всегда готова помочь вам в выборе идеального автомобиля, учитывая ваши предпочтения и бюджет. Мы гордимся тем, что можем предложить конкурентоспособные цены и гибкие условия финансирования, чтобы сделать вашу покупку максимально комфортной.
        </p>

        <p>
            Кроме того, мы предоставляем полный спектр услуг, включая trade-in, кредитование и страхование. Наша задача — не просто продать автомобиль, а создать долгосрочные отношения с клиентами, основанные на доверии и взаимопонимании. Мы ценим каждого клиента и стремимся превзойти ваши ожидания на каждом этапе сотрудничества.

        </p>
        <!--Картинка справа-->
        <img class="right" src="${pageContext.request.contextPath}/pages/right_page.jpg">
        <p>
            В Автомаркете VCS мы верим, что автомобиль — это не просто средство передвижения, а часть вашей жизни. Давайте вместе сделаем этот путь увлекательным и безопасным!
        </p>
        <p>
            ㅤ
        </p>
    </div>
    <!--Запрет наплывания-->
    <div class="clear"></div>
    <!--Подвал-->
    <div id="footer">
        <p class="fon"><strong>Телефон:<br> +7 953 015 62 18</strong> </p>
        <p class="mail"><strong>E-mail<br>helloampro@gmail.com</strong></p>
        <img class = "logo_bottom" src="/icons/логотип-без-фона.png">
    </div>
</div>
</body>
</html>
