<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:if test="${empty message and not empty sessionScope.message}"><c:set var="message" value="${sessionScope.message}" /><c:remove var="message" scope="session" /></c:if>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Quản lý sách</title>
</head>
<body>
	<div class="d-flex justify-content-between align-items-center mb-3">
		<h3 class="mb-0">Quản lý sách <small class="text-muted fs-6">(${totalItems} sách)</small></h3>
		<a href="${ctx}/admin/books/new" class="btn btn-success">+ Thêm sách</a>
	</div>
	<c:if test="${not empty message}">
		<div class="alert alert-success">${message}</div>
	</c:if>

	<table class="table table-bordered table-hover align-middle bg-white">
		<thead class="table-danger">
			<tr>
				<th>ID</th><th>Ảnh bìa</th><th>Tiêu đề</th><th>ISBN</th><th>Tác giả</th>
				<th>Publisher</th><th>Ngày XB</th><th>Giá</th><th>SL</th><th style="width: 190px">Thao tác</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="b" items="${books}">
				<tr>
					<td>${b.bookid}</td>
					<td><img src="${ctx}/image?fname=${b.coverImage}" style="width: 50px; height: 66px; object-fit: cover"></td>
					<td>${b.title}</td>
					<td>${b.isbn}</td>
					<td>${b.authorNames}</td>
					<td>${b.publisher}</td>
					<td>${b.publishDate}</td>
					<td>${b.price}</td>
					<td>${b.quantity}</td>
					<td>
						<a href="${ctx}/admin/books/view?id=${b.bookid}" class="btn btn-sm btn-info">Xem</a>
						<a href="${ctx}/admin/books/edit?id=${b.bookid}" class="btn btn-sm btn-warning">Sửa</a>
						<form method="post" action="${ctx}/admin/books/delete" class="d-inline"
							onsubmit="return confirm('Xóa sách &quot;${b.title}&quot;?')">
							<input type="hidden" name="id" value="${b.bookid}">
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
