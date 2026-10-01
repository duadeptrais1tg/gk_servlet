<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><sitemesh:write property="title" /> | BookStore</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx}/css/style.css" rel="stylesheet">
<sitemesh:write property="head" />
</head>
<body class="d-flex flex-column min-vh-100">
	<!-- ===== HEADER (User) ===== -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="container">
			<a class="navbar-brand fw-bold" href="${ctx}/">BookStore</a>
			<ul class="navbar-nav me-auto">
				<li class="nav-item"><a class="nav-link" href="${ctx}/">Trang Chủ</a></li>
				<li class="nav-item"><a class="nav-link" href="${ctx}/books">Sản phẩm</a></li>
				<c:if test="${sessionScope.account != null && sessionScope.account.adminRole}">
					<li class="nav-item"><a class="nav-link" href="${ctx}/admin">Trang quản trị</a></li>
				</c:if>
			</ul>
			<ul class="navbar-nav">
				<c:choose>
					<c:when test="${sessionScope.account != null}">
						<li class="nav-item"><span class="nav-link text-white">Xin chào, ${sessionScope.account.fullname}</span></li>
						<li class="nav-item"><a class="nav-link" href="${ctx}/logout">Đăng xuất</a></li>
					</c:when>
					<c:otherwise>
						<li class="nav-item"><a class="nav-link" href="${ctx}/login">Đăng nhập</a></li>
						<li class="nav-item"><a class="nav-link" href="${ctx}/register">Đăng ký</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</nav>

	<!-- ===== CONTENT ===== -->
	<main class="container my-4 flex-grow-1">
		<sitemesh:write property="body" />
	</main>

	<!-- ===== FOOTER ===== -->
	<footer class="bg-dark text-white text-center py-3">
		Họ tên: Phạm Viết Thư &nbsp;|&nbsp; MSSV: 24162126 &nbsp;|&nbsp; Mã đề: 01
	</footer>
</body>
</html>
