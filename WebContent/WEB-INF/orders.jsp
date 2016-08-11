<%@include file="/fragments/head.jspf"%>
<%@ taglib uri="/WEB-INF/jstl/details.tld" prefix="t"%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<title><fmt:message key="ordersTittle" /></title>
<link rel="stylesheet" href="./stylesheets/orders.css" type="text/css" />
</head>
<body>
	<div class="header">
		<h1 class="header">
			<c:choose>
				<c:when test="${not empty requestScope.noOrders }">
					<fmt:message key="ordersMSG" />
				</c:when>
				<c:otherwise>
					<fmt:message key="myOrdersMSG" />
				</c:otherwise>
			</c:choose>
		</h1>
		<%@include file="/fragments/logout.jspf"%>
		<div class="header-right">
			<div>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="CSRF" value="${sessionScope.CSRF}" /> 
					<input type="hidden" name="command" value="content" />
					<button class="button">
						<fmt:message key="contentPageTitle" />
					</button>
				</form>
			</div>
			<div>
				<form action="/Project4/controller" method="post">
				    <input type="hidden" name="CSRF" value="${sessionScope.CSRF}" />
					<input type="hidden" name="requestPath" value="/WEB-INF/orders.jsp" />
					<input type="hidden" name="command" value="language" />
					<button class="button" name="locale" value="RU">RU</button>
					<button class="button" name="locale" value="EN">EN</button>
				</form>
			</div>
		</div>
	</div>
	<div class="content">
		<div>
			<c:set var="details" value="${sessionScope.details}" />
			<c:set var="productDetails" value="${sessionScope.pDetails}" />
			<c:forEach items="${sessionScope.orders}" var="order">
				<details class="details">
					<summary>
						<fmt:message key="orderID" />
						: ${order.orderId} |
						<fmt:message key="orderDate" />
						: ${order.orderDate} |
						<fmt:message key="orderStatus" />
						: ${order.status} |
						<fmt:message key="totalCost" />
						: ${order.totalCost } UAH
					</summary>
					<table border="1" class="table">
						<tr>
							<td width="250"><fmt:message key="product" /></td>
							<td width="60"><fmt:message key="amount" /></td>
							<td width="200"><fmt:message key="totalCost" /></td>
						</tr>
						<t:details asTable="true" details="${details[order]}"
							products="${productDetails[order]}"></t:details>

					</table>
				</details>
			</c:forEach>
		</div>
	</div>
<%@include file="/fragments/footer.jspf"%>