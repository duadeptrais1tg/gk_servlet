<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="isNew" value="${author.authorId == null}" />
<!DOCTYPE html>
<html>
<head>
<title>${isNew ? 'Thêm tác giả' : 'Sửa tác giả'}</title>
</head>
<body>
	<h3 class="mb-3">${isNew ? 'Thêm tác giả mới' : 'Sửa tác giả #'.concat(author.authorId)}</h3>

	<form method="post" action="${ctx}/admin/authors/save" class="card card-body" style="max-width: 600px">
		<c:if test="${!isNew}">
			<input type="hidden" name="authorId" value="${author.authorId}">
		</c:if>
		<div class="mb-3">
			<label class="form-label">Tên tác giả</label>
			<input type="text" name="authorName" value="${author.authorName}" class="form-control" maxlength="100" required>
		</div>
		<div class="mb-3">
			<label class="form-label">Ngày sinh</label>
			<input type="date" name="dateOfBirth" value="${author.dateOfBirth}" class="form-control">
		</div>
		<div>
			<button class="btn btn-primary">Lưu</button>
			<a href="${ctx}/admin/authors" class="btn btn-secondary">Hủy</a>
		</div>
	</form>
</body>
</html>
