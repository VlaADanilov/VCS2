<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<jsp:include page="/WEB-INF/jsp/header.jsp" />

<div id="wrapper">
    <div id="sidebar">
        <h3>На нашем сайте</h3>
        <p><img class="marcer" src="${pageContext.servletContext.contextPath}/resources/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/list">Автомобили</a></p>
        <p><img class="marcer" src="${pageContext.servletContext.contextPath}/resources/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/all_users">Список пользователей</a></p>
        <p><img class="marcer" src="${pageContext.servletContext.contextPath}/resources/pages/icon_green_checkbox.png" width="10" height="10"><a href="${pageContext.servletContext.contextPath}/list_of_emp">Сотрудники</a></p>
        <hr width="50" color="#037FFC" size="5">

        <c:if test="${not empty user}">
            Здравствуйте, ${user.getUsername()} !
        </c:if>

        <c:if test="${empty user}">
            Ой, кажется вы не зарегистрированы😟
            Надо это исправить!
        </c:if>
    </div>

    <!--Основной контент (статья)-->
    <div id="content">
        <!--Картинка слева-->
        <img class="left" src="${pageContext.request.contextPath}/resources/pages/left_page.jpg">
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
        <img class="right" src="${pageContext.request.contextPath}/resources/pages/right_page.jpg">
        <p>
            В Автомаркете VCS мы верим, что автомобиль — это не просто средство передвижения, а часть вашей жизни. Давайте вместе сделаем этот путь увлекательным и безопасным!
        </p>
        <p>
            ㅤ
        </p>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />