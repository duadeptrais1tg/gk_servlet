<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="isNew" value="${book.bookid == null}" />
<!DOCTYPE html>
<html>
<head>
<title>${isNew ? 'Thêm sách' : 'Sửa sách'}</title>
</head>
<body>
	<h3 class="mb-3">${isNew ? 'Thêm sách mới' : 'Sửa sách #'.concat(book.bookid)}</h3>

	<form method="post" action="${ctx}/admin/books/save" enctype="multipart/form-data" class="card card-body">
		<c:if test="${!isNew}">
			<input type="hidden" name="bookid" value="${book.bookid}">
		</c:if>
		<div class="row g-3">
			<div class="col-md-8">
				<label class="form-label">Tiêu đề</label>
				<input type="text" name="title" value="${book.title}" class="form-control" maxlength="200" required>
			</div>
			<div class="col-md-4">
				<label class="form-label">ISBN</label>
				<input type="number" name="isbn" value="${book.isbn}" class="form-control" max="2147483647">
			</div>
			<div class="col-md-4">
				<label class="form-label">Publisher</label>
				<input type="text" name="publisher" value="${book.publisher}" class="form-control" maxlength="100">
			</div>
			<div class="col-md-3">
				<label class="form-label">Ngày xuất bản</label>
				<input type="date" name="publishDate" value="${book.publishDate}" class="form-control">
			</div>
			<div class="col-md-3">
				<label class="form-label">Giá</label>
				<input type="number" name="price" value="${book.price}" class="form-control" step="0.01" min="0" max="9999.99">
			</div>
			<div class="col-md-2">
				<label class="form-label">Số lượng</label>
				<input type="number" name="quantity" value="${book.quantity}" class="form-control" min="0">
			</div>
			<div class="col-12">
				<label class="form-label">Mô tả</label>
				<textarea name="description" rows="3" class="form-control">${book.description}</textarea>
			</div>
			<div class="col-md-6">
				<label class="form-label">Tác giả</label>
				<div class="border rounded p-2" style="max-height: 180px; overflow-y: auto">
					<c:forEach var="a" items="${authors}">
						<div class="form-check">
							<input class="form-check-input" type="checkbox" name="authorIdList" value="${a.authorId}" id="a${a.authorId}"
								${book.authorIds.contains(a.authorId) ? 'checked' : ''}>
							<label class="form-check-label" for="a${a.authorId}">${a.authorName}</label>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="col-md-6">
				<label class="form-label">Ảnh bìa ${isNew ? '' : '(để trống nếu giữ ảnh cũ)'}</label>
				<input type="file" name="coverFile" accept="image/*" class="form-control">
				<c:if test="${not empty book.coverImage}">
					<img src="${ctx}/image?fname=${book.coverImage}" class="mt-2" style="height: 120px">
				</c:if>
			</div>
		</div>
		<div class="mt-3">
			<button class="btn btn-primary">Lưu</button>
			<a href="${ctx}/admin/books" class="btn btn-secondary">Hủy</a>
		</div>
	</form>
</body>
</html>
