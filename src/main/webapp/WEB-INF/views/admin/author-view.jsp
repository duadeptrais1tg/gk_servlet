<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Chi tiết tác giả</title>
</head>
<body>
	<h3 class="mb-3">Chi tiết tác giả #${author.authorId}</h3>
	<div class="card card-body" style="max-width: 700px">
		<table class="table table-sm">
			<tr><th style="width: 160px">Tên tác giả</th><td>${author.authorName}</td></tr>
			<tr><th>Ngày sinh</th><td>${author.dateOfBirth}</td></tr>
			<tr>
				<th>Sách đã viết</th>
				<td>
					<c:forEach var="b" items="${author.books}">
						<div><a href="${ctx}/admin/books/view?id=${b.bookid}">${b.title}</a></div>
					</c:forEach>
					<c:if test="${empty author.books}"><span class="text-muted">Chưa có</span></c:if>
				</td>
			</tr>
		</table>
	</div>
	<div class="mt-3">
		<a href="${ctx}/admin/authors/edit?id=${author.authorId}" class="btn btn-warning">Sửa</a>
		<a href="${ctx}/admin/authors" class="btn btn-secondary">&laquo; Danh sách</a>
	</div>
</body>
</html>
