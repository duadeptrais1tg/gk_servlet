<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<title>Đăng ký</title>
</head>
<body>
	<div class="row justify-content-center">
		<div class="col-md-5">
			<div class="card shadow-sm">
				<div class="card-body">
					<h4 class="card-title mb-3 text-center">Đăng ký tài khoản</h4>
					<c:if test="${not empty error}">
						<div class="alert alert-danger">${error}</div>
					</c:if>
					<form method="post" action="${pageContext.request.contextPath}/register">
						<div class="mb-2">
							<label class="form-label">Email</label>
							<input type="email" name="email" value="${email}" class="form-control" maxlength="50" required>
						</div>
						<div class="mb-2">
							<label class="form-label">Họ tên</label>
							<input type="text" name="fullname" value="${fullname}" class="form-control" maxlength="50" required>
						</div>
						<div class="mb-2">
							<label class="form-label">Số điện thoại</label>
							<input type="number" name="phone" value="${phone}" class="form-control" max="2147483647">
						</div>
						<div class="mb-2">
							<label class="form-label">Mật khẩu</label>
							<input type="password" name="password" class="form-control" minlength="6" required>
						</div>
						<div class="mb-3">
							<label class="form-label">Nhập lại mật khẩu</label>
							<input type="password" name="confirmPassword" class="form-control" minlength="6" required>
						</div>
						<button class="btn btn-primary w-100">Đăng ký &amp; gửi mã OTP</button>
					</form>
					<p class="mt-3 text-center">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
