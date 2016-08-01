<%@include file="/fragments/head.jspf"%>
<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<title><fmt:message key="contentPageTitle" /></title>
<link rel="stylesheet" href="./stylesheets/content.css" type="text/css" />
</head>
<body>
	<div class="header">
		<h1 class="header">
			<c:choose>
				<c:when test="${not empty requestScope.tnxMSG}">
					<fmt:message key="tnxForPurchaseMSG" />
				</c:when>
				<c:otherwise>
					<fmt:message key="contentPageMSG" />
				</c:otherwise>
			</c:choose>
		</h1>
		<%@include file="/fragments/logout.jspf"%>
		<div class="header-right">
			<div>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="command" value="userOrders" />
					<button class="button">
						<fmt:message key="myOrdersMSG" />
					</button>
				</form>
			</div>
			<div>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="requestPath"
						value="/WEB-INF/content.jsp" /> <input type="hidden"
						name="command" value="language" />
					<button class="button" name="locale" value="RU">RU</button>
					<button class="button" name="locale" value="EN">EN</button>
				</form>
			</div>
		</div>
	</div>
	<div class="content">
		<table class="products">
			<tr>
				<td width="550">
					<div class="outer-table">
						<form action="/Project4/controller" method="post">
							<input type="hidden" name="command" value="addProductBucket" />
							<table border="1">
								<tr>
									<td align="center" width="20">ID</td>
									<td align="center" width="272"><fmt:message key="name" /></td>
									<td align="center" width="112"><fmt:message key="price" /></td>
									<td align="center" width="70"><fmt:message key="currency" /></td>
									<td align="center" width="76"></td>
								</tr>
								<c:forEach items="${sessionScope.products}" var="product">
									<tr>
										<td>${product.id}</td>
										<td>${product.name}</td>
										<td align="center">${product.price}</td>
										<td align="center">UAH</td>
										<td align="center">
											<button name="bProductId" value="${product.id}">
												<fmt:message key="toBucketBTN" />
											</button>
										</td>
									</tr>
								</c:forEach>
							</table>
						</form>
					</div>

				</td>
			</tr>
		</table>
		<div class="bucket">
			<details>
				<c:set var="bucket" value="${sessionScope.bucket}" />
				<summary>
					<fmt:message key="bucket" />
					:
					<fmt:message key="cost" />
					: ${bucket.totalCost}
					<fmt:message key="products" />
					: ${bucket.size()}
				</summary>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="command" value="removeProductBucket" />
					<c:set var="bucket" value="${sessionScope.bucket }" />
					<table>
						<c:forEach var="pd" items="${bucket.bucket}">
							<tr>
								<td align="right" width="100"><fmt:message key="productId" />:
									${pd.key}</td>
								<td align="right" width="100"><fmt:message key="amount" />:
									${pd.value}</td>
								<td align="right" width="100">
									<button name="bProductId" value="${pd.key}"
										class="bucket-button">
										<fmt:message key="removeBTN" />
									</button>
								</td>
							</tr>
						</c:forEach>
					</table>
				</form>
				<form action="/Project4/controller" method="post">
					<input type="hidden" name="command" value="purchase" />
					<button class="bucket-button">
						<fmt:message key="confirmBTN" />
					</button>
				</form>
			</details>
		</div>
	</div>
	<%@include file="/fragments/footer.jspf"%>