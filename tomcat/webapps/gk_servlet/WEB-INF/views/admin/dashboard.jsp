<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Trang quản trị</title>
</head>
<body>
	<h3>Trang quản trị</h3>
	<div class="row g-3 mt-2">
		<div class="col-md-6">
			<a class="btn btn-outline-danger w-100 py-4" href="${pageContext.request.contextPath}/admin/books">Quản lý Sách</a>
		</div>
		<div class="col-md-6">
			<a class="btn btn-outline-danger w-100 py-4" href="${pageContext.request.contextPath}/admin/authors">Quản lý Tác giả</a>
		</div>
	</div>
</body>
</html>
