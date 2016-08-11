<%@include file="/fragments/head.jspf"%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<title><fmt:message key="errorTitle" /></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/stylesheets/error.css" type="text/css" />
</head>
<body>
	<div class="header">
		<h1 class="header">
			<fmt:message key="errorMSG" />
		</h1>
		<div class="header-right">
			<div>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="requestPath" value="/error.jsp" />
					<input type="hidden" name="CSRF" value="${sessionScope.CSRF}" />
					<input type="hidden" name="command" value="language" />
					<button class="button" name="locale" value="RU">RU</button>
					<button class="button" name="locale" value="EN">EN</button>
				</form>
			</div>
		</div>
		<%@include file="/fragments/home.jspf"%>
	</div>

	<div id="div">
		<img src="${pageContext.request.contextPath}/images/error.jpg" />
	</div>
</body>
</html>