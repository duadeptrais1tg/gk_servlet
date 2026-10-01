<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<title>Kích hoạt tài khoản</title>
</head>
<body>
	<div class="row justify-content-center">
		<div class="col-md-5">
			<div class="card shadow-sm">
				<div class="card-body">
					<h4 class="card-title mb-3 text-center">Nhập mã OTP kích hoạt</h4>
					<c:if test="${not empty message}">
						<div class="alert alert-info">${message}</div>
					</c:if>
					<c:if test="${not empty error}">
						<div class="alert alert-danger">${error}</div>
					</c:if>
					<form method="post" action="${pageContext.request.contextPath}/verify-otp">
						<div class="mb-3">
							<label class="form-label">Mã OTP (6 chữ số, hiệu lực 5 phút)</label>
							<input type="text" name="otp" class="form-control" maxlength="6" pattern="[0-9]{6}" required autofocus>
						</div>
						<button class="btn btn-success w-100">Kích hoạt</button>
					</form>
					<form method="post" action="${pageContext.request.contextPath}/resend-otp" class="mt-2">
						<button class="btn btn-link w-100">Gửi lại mã OTP</button>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
