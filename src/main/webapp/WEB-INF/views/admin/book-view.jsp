<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Chi tiết sách</title>
</head>
<body>
	<h3 class="mb-3">Chi tiết sách #${book.bookid}</h3>
	<div class="card card-body">
		<div class="row">
			<div class="col-md-3 text-center">
				<img class="detail-cover" src="${ctx}/image?fname=${book.coverImage}" alt="${book.title}">
			</div>
			<div class="col-md-9">
				<table class="table table-sm">
					<tr><th style="width: 160px">Tiêu đề</th><td>${book.title}</td></tr>
					<tr><th>ISBN</th><td>${book.isbn}</td></tr>
					<tr><th>Tác giả</th><td>${book.authorNames}</td></tr>
					<tr><th>Publisher</th><td>${book.publisher}</td></tr>
					<tr><th>Ngày xuất bản</th><td>${book.publishDate}</td></tr>
					<tr><th>Giá</th><td>${book.price}</td></tr>
					<tr><th>Số lượng</th><td>${book.quantity}</td></tr>
					<tr><th>Mô tả</th><td>${book.description}</td></tr>
					<tr><th>Số review</th><td>${reviewCount}</td></tr>
				</table>
			</div>
		</div>
	</div>
	<div class="mt-3">
		<a href="${ctx}/admin/books/edit?id=${book.bookid}" class="btn btn-warning">Sửa</a>
		<a href="${ctx}/admin/books" class="btn btn-secondary">&laquo; Danh sách</a>
	</div>
</body>
</html>
