<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:if test="${empty message and not empty sessionScope.message}"><c:set var="message" value="${sessionScope.message}" /><c:remove var="message" scope="session" /></c:if>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Quản lý tác giả</title>
</head>
<body>
	<div class="d-flex justify-content-between align-items-center mb-3">
		<h3 class="mb-0">Quản lý tác giả <small class="text-muted fs-6">(${totalItems} tác giả)</small></h3>
		<a href="${ctx}/admin/authors/new" class="btn btn-success">+ Thêm tác giả</a>
	</div>
	<c:if test="${not empty message}">
		<div class="alert alert-success">${message}</div>
	</c:if>

	<table class="table table-bordered table-hover align-middle bg-white">
		<thead class="table-danger">
			<tr><th>ID</th><th>Tên tác giả</th><th>Ngày sinh</th><th>Số sách</th><th style="width: 190px">Thao tác</th></tr>
		</thead>
		<tbody>
			<c:forEach var="a" items="${authors}">
				<tr>
					<td>${a.authorId}</td>
					<td>${a.authorName}</td>
					<td>${a.dateOfBirth}</td>
					<td>${bookCounts[a.authorId] != null ? bookCounts[a.authorId] : 0}</td>
					<td>
						<a href="${ctx}/admin/authors/view?id=${a.authorId}" class="btn btn-sm btn-info">Xem</a>
						<a href="${ctx}/admin/authors/edit?id=${a.authorId}" class="btn btn-sm btn-warning">Sửa</a>
						<form method="post" action="${ctx}/admin/authors/delete" class="d-inline"
							onsubmit="return confirm('Xóa tác giả &quot;${a.authorName}&quot;?')">
							<input type="hidden" name="id" value="${a.authorId}">
							<input type="hidden" name="page" value="${currentPage}">
							<button class="btn btn-sm btn-danger">Xóa</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- Phan trang -->
	<c:if test="${totalPages > 1}">
		<ul class="pagination justify-content-center">
			<li class="page-item ${currentPage == 1 ? 'disabled' : ''}"><a class="page-link" href="?page=${currentPage - 1}">&laquo;</a></li>
			<c:forEach var="i" begin="1" end="${totalPages}">
				<li class="page-item ${i == currentPage ? 'active' : ''}"><a class="page-link" href="?page=${i}">${i}</a></li>
			</c:forEach>
			<li class="page-item ${currentPage == totalPages ? 'disabled' : ''}"><a class="page-link" href="?page=${currentPage + 1}">&raquo;</a></li>
		</ul>
	</c:if>
</body>
</html>
