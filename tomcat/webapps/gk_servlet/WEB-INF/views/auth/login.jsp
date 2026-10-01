<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:if test="${empty message and not empty sessionScope.message}"><c:set var="message" value="${sessionScope.message}" /><c:remove var="message" scope="session" /></c:if>
<!DOCTYPE html>
<html>
<head>
<title>Đăng nhập</title>
</head>
<body>
	<div class="row justify-content-center">
		<div class="col-md-4">
			<div class="card shadow-sm">
				<div class="card-body">
					<h4 class="card-title mb-3 text-center">Đăng nhập</h4>
					<c:if test="${not empty message}">
						<div class="alert alert-success">${message}</div>
					</c:if>
					<c:if test="${not empty error}">
						<div class="alert alert-danger">${error}</div>
					</c:if>
					<form method="post" action="${pageContext.request.contextPath}/login">
						<div class="mb-2">
							<label class="form-label">Email</label>
							<input type="email" name="email" value="${email}" class="form-control" required>
						</div>
						<div class="mb-3">
							<label class="form-label">Mật khẩu</label>
							<input type="password" name="password" class="form-control" required>
						</div>
						<button class="btn btn-primary w-100">Đăng nhập</button>
					</form>
					<p class="mt-3 text-center">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
