<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Trang chủ</title>
</head>
<body>
	<h3 class="mb-3">Danh sách sách</h3>

	<div class="row g-4">
		<c:forEach var="b" items="${books}">
			<div class="col-md-4">
				<div class="card h-100 book-card shadow-sm">
					<img class="card-img-top cover" src="${ctx}/image?fname=${b.coverImage}" alt="${b.title}">
					<div class="card-body">
						<p><strong>Tiêu đề:</strong> <a href="${ctx}/book?id=${b.bookid}">${b.title}</a></p>
						<p><strong>Mã isbn:</strong> ${b.isbn}</p>
						<p><strong>Tác giả:</strong> ${b.authorNames}</p>
						<p><strong>Publisher:</strong> ${b.publisher}</p>
						<p><strong>Publisher_date:</strong> ${b.publishDate}</p>
						<p><strong>Quantity:</strong> ${b.quantity}</p>
						<p><strong>Review</strong> (${reviewCounts[b.bookid] != null ? reviewCounts[b.bookid] : 0})</p>
					</div>
				</div>
			</div>
		</c:forEach>
		<c:if test="${empty books}">
			<p class="text-muted">Chưa có sách nào.</p>
		</c:if>
	</div>

	<!-- Phan trang -->
	<c:if test="${totalPages > 1}">
		<nav class="mt-4">
			<ul class="pagination justify-content-center">
				<li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
					<a class="page-link" href="?page=${currentPage - 1}">&laquo;</a>
				</li>
				<c:forEach var="i" begin="1" end="${totalPages}">
					<li class="page-item ${i == currentPage ? 'active' : ''}">
						<a class="page-link" href="?page=${i}">${i}</a>
					</li>
				</c:forEach>
				<li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
					<a class="page-link" href="?page=${currentPage + 1}">&raquo;</a>
				</li>
			</ul>
		</nav>
	</c:if>
</body>
</html>
