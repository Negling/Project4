<%@include file="/fragments/head.jspf"%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<title><fmt:message key="adminTitle" /></title>
<link rel="stylesheet" href="./stylesheets/admin.css" type="text/css" />
</head>
<body>
	<div class="header">
		<h1 class="header">
			<c:choose>
				<c:when test="${not empty requestScope.productMfMSG}">
					<fmt:message key="productManagmentFailedMSG" />
				</c:when>
				<c:otherwise>
					<fmt:message key="managmentMSG" />
				</c:otherwise>
			</c:choose>
		</h1>
		<%@include file="/fragments/logout.jspf"%>
		<div class="header-right">
			<div>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="requestPath" value="/WEB-INF/admin.jsp" />
					<input type="hidden" name="command" value="language" />
					<button class="button" name="locale" value="RU">RU</button>
					<button class="button" name="locale" value="EN">EN</button>
				</form>
			</div>
		</div>
	</div>
	<div class="content">
		<div>
			<details class="details">
				<summary>
					<fmt:message key="manageProducts" />
				</summary>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="command" value="deleteProduct" />
					<table border="1">
						<tr>
							<td align="center" width="20">ID
							<td />
							<td align="center" width="272"><fmt:message key="name" /></td>
							<td align="center" width="112"><fmt:message key="price" /></td>
							<td align="center" width="70"><fmt:message key="currency" /></td>
							<td align="center" width="76">
							<td />
						</tr>
						<c:forEach items="${sessionScope.products}" var="product">
							<tr>
								<td>${product.id}
								<td />
								<td>${product.name}</td>
								<td align="center">${product.price}</td>
								<td align="center">UAH</td>
								<td align="center">
									<button name="pUpdateId" value="${product.id}">
										<fmt:message key="deleteProductBTN" />
									</button>
								<td />
							</tr>
						</c:forEach>
					</table>
				</form>
				<details class="details">
					<summary>
						<fmt:message key="addProduct" />
					</summary>
					<form action="/Project4/controller" method="post">
						<input type="hidden" name="command" value="addProduct" />
						<table>
							<tr>
								<td><fmt:message key="name" /></td>
								<td><input type="text" name="pUdateName" /></td>
							</tr>
							<tr>
								<td><fmt:message key="price" /></td>
								<td><input type="text" name="pUpdatePrice" /></td>
							</tr>
							<tr>
								<td><button class="button">
										<fmt:message key="addBTN" />
									</button></td>
								<td></td>
							</tr>
						</table>
					</form>
				</details>
			</details>
			<details class="details">
				<summary>
					<fmt:message key="manageUsers" />
				</summary>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="command" value="blacklist" />
					<table border="1">
						<tr>
							<td align="center" width="20">ID
							<td align="center" width="100"><fmt:message key="nameLabel" /></td>
							<td align="center" width="100"><fmt:message
									key="surnameLabel" /></td>
							<td align="center" width="100"><fmt:message key="loginLabel" /></td>
							<td align="center" width="100"><fmt:message key="emailLabel" /></td>
							<td align="center" width="100"><fmt:message
									key="genderLabel" /></td>
							<td align="center" width="100"><fmt:message
									key="statusLabel" /></td>
							<td />
						</tr>
						<c:forEach items="${sessionScope.users}" var="user">
							<tr>
								<td>${user.userId}</td>
								<td>${user.name}</td>
								<td align="center">${user.surname}</td>
								<td align="center">${user.login}</td>
								<td align="center">${user.email}</td>
								<td align="center"><c:choose>
										<c:when test="${empty user.male}">
											<fmt:message key="otherLabel" />
										</c:when>
										<c:when test="${not user.male}">
											<fmt:message key="femaleLabel" />
										</c:when>
										<c:otherwise>
											<fmt:message key="maleLabel" />
										</c:otherwise>
									</c:choose></td>
								<c:choose>
									<c:when test="${user.blocked}">
										<td align="center"><fmt:message key="blockedStatus" /></td>
										<td align="center">
											<button name="uUnban" value="${user.login}">
												<fmt:message key="unblockBTN" />
											</button>
										</td>
									</c:when>
									<c:otherwise>
										<td align="center"><fmt:message key="okayStatus" /></td>
										<td align="center">
											<button name="uBan" value="${user.login}">
												<fmt:message key="blockBTN" />
											</button>
										</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</table>
				</form>
			</details>
		</div>
	</div>
<%@include file="/fragments/footer.jspf"%>