<%@include file="/fragments/head.jspf"%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<title><fmt:message key="loginTitle" /></title>
<link rel="stylesheet" href="./stylesheets/login.css" type="text/css" />
</head>
<body>
	<div class="header">
		<h1 class="header">
			<c:choose>
				<c:when test="${not empty requestScope.blocked}">
					<fmt:message key="blacklistMSG" />
				</c:when>
				<c:when test="${not empty requestScope.incorrectLogin}">
					<fmt:message key="loginFailedMSG" />
				</c:when>
				<c:otherwise>
					<fmt:message key="dataMSG" />:
				</c:otherwise>
			</c:choose>
		</h1>
		<%@include file="/fragments/logout.jspf"%>
		<div class="header-right">
			<div>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="requestPath" value="/login.jsp" />
					<input type="hidden" name="command" value="language" />
					<input type="hidden" name="CSRF" value="${sessionScope.CSRF}" />
					<button class="button" name="locale" value="RU">RU</button>
					<button class="button" name="locale" value="EN">EN</button>
				</form>
			</div>
		</div>
	</div>
	<div class="content">
		<div>
			<form class="form-container" method="post"
				action="/Project4/controller">
				<input type="hidden" name="command" value="login" />
				<input type="hidden" name="CSRF" value="${sessionScope.CSRF}" />
				<div class="form-title">
					<fmt:message key="loginLabel" />:
				</div>
				<input class="form-field" type="text" name="login" /><br />
				<div class="form-title">
					<fmt:message key="passwordLabel" />:
				</div>
				<input class="form-field" type="password" name="password" /><br />
				<button class="button">
					<fmt:message key="submitBTN" />
				</button>
			</form>
		</div>
	</div>
<%@include file="/fragments/footer.jspf"%>