<%@include file="/fragments/head.jspf"%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<title><fmt:message key="registrationTitle" /></title>
<link rel="stylesheet" href="./stylesheets/registration.css"
	type="text/css" />
</head>
<body>
	<div class="header">
		<h1 class="header">
			<c:choose>
				<c:when test="${not empty requestScope.userAlreadyRegistered}">
					<fmt:message key="registrationFailedtMSG" />
				</c:when>
				<c:when test="${not empty requestScope.incorrectData}">
					<fmt:message key="incorrectDataMSG" />
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
					<input type="hidden" name="CSRF" value="${sessionScope.CSRF}" /> 
					<input type="hidden" name="requestPath" value="/registration.jsp" /> 
					<input type="hidden" name="command" value="language" />
					<button class="button" name="locale" value="RU">RU</button>
					<button class="button" name="locale" value="EN">EN</button>
				</form>
			</div>
		</div>
	</div>
	<div class="content">
		<div>
			<form method="post" action="/Project4/controller">
				<input type="hidden" name="CSRF" value="${sessionScope.CSRF}" /> <input
					type="hidden" name="command" value="register" />
				<table>
					<tr>
						<td width="70" align="left" valign="middle"><fmt:message
								key="nameLabel" />:</td>
						<td><input class="form-field" type="text" name="name" /></td>
					</tr>
					<tr>
						<td align="left" valign="middle"><fmt:message
								key="surnameLabel" />:</td>
						<td><input class="form-field" type="text" name="surname" /></td>
					</tr>
					<tr>
						<td align="left" valign="middle"><fmt:message
								key="loginLabel" />:</td>
						<td><input class="form-field" type="text" name="login" /></td>
					</tr>
					<tr>
						<td align="left" valign="middle"><fmt:message
								key="passwordLabel" />:</td>
						<td><input class="form-field" type="password" name="password" /></td>
					</tr>
					<tr>
						<td align="left" valign="middle"><fmt:message
								key="confirmPasswordLabel" />:</td>
						<td valign="middle"><input class="form-field" type="password"
							name="confirmPassword" /></td>
					</tr>
					<tr>
						<td align="left" valign="middle"><fmt:message
								key="emailLabel" />:</td>
						<td><input class="form-field" type="text" name="email" /></td>
					</tr>
					<tr>
						<td align="left" valign="middle"><fmt:message
								key="confirmEmailLabel" />:</td>
						<td><input class="form-field" type="text" name="confirmEmail" /></td>
					</tr>
				</table>
				<input style="margin-left: 20%;" type="radio" name="male"
					value="true" checked>
				<fmt:message key="maleLabel" />
				<input style="margin-left: 1%;" type="radio" name="male"
					value="false">
				<fmt:message key="femaleLabel" />
				<input style="margin-left: 1%;" type="radio" name="male" value="">
				<fmt:message key="otherLabel" />
				<br />
				<button class="Cbutton">
					<fmt:message key="submitBTN" />
				</button>
			</form>
		</div>
	</div>
</body>
<%@include file="/fragments/footer.jspf"%>