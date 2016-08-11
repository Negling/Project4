<%@include file="/fragments/head.jspf"%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<title><fmt:message key="welcomeTittle" /></title>
<link rel="stylesheet" href="./stylesheets/index.css" type="text/css" />
</head>
<body>
	<div class="header">
		<h1 class="header">
			<fmt:message key="indexMSG" />:
		</h1>
		<%@include file="/fragments/logout.jspf"%>
		<div class="header-right">
			<div>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="requestPath" value="/index.jsp" />
					<input type="hidden" name="command" value="language" />
					<input type="hidden" name="CSRF" value="${sessionScope.CSRF}" />
					<button class="button" name="locale" value="RU">RU</button>
					<button class="button" name="locale" value="EN">EN</button>
				</form>
			</div>
		</div>
	</div>
	<div class="content">
		<div class="reg-login">
			<div>
				<form action="login.jsp" method="post">
					<button class="button">
						<fmt:message key="loginBTN" />
					</button>
				</form>
			</div>
			<div>
				<p class="text">
					<fmt:message key="or" />
				</p>
			</div>
			<div>
				<form action="registration.jsp" method="post">
					<button class="button">
						<fmt:message key="registerBTN" />
					</button>
				</form>
			</div>
		</div>
	</div>
	<div class="footer"></div>
</body>
</html>