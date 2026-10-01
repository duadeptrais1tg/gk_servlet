<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:if test="${empty message and not empty sessionScope.message}"><c:set var="message" value="${sessionScope.message}" /><c:remove var="message" scope="session" /></c:if>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>${book.title}</title>
</head>
<body>
	<c:if test="${not empty message}">
		<div class="alert alert-success">${message}</div>
	</c:if>

	<table class="table table-bordered bg-white">
		<!-- Thong tin sach -->
		<tr>
			<td style="width: 30%" class="text-center">
				<img class="detail-cover" src="${ctx}/image?fname=${book.coverImage}" alt="${book.title}">
			</td>
			<td>
				<p><strong>Tiêu đề:</strong> ${book.title}</p>
				<p><strong>Mã isbn:</strong> ${book.isbn}</p>
				<p><strong>Tác giả:</strong> ${book.authorNames}</p>
				<p><strong>Publisher:</strong> ${book.publisher}</p>
				<p><strong>Publisher_date:</strong> ${book.publishDate}</p>
				<p><strong>Quantity:</strong> ${book.quantity}</p><p><strong>Giá:</strong> ${book.price}</p>
				<p><strong>Reviews</strong> (${reviewCount})</p>
<c:choose>
    <c:when test="${book.quantity > 0 and book.price != null and book.price >= 0}">
        <form method="post" action="${ctx}/cart" class="d-flex flex-wrap gap-2 align-items-center mt-3">
            <input type="hidden" name="cartToken" value="${sessionScope.cartToken}">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="bookId" value="${book.bookid}">
            <label for="quantity-${book.bookid}">Số lượng</label>
            <input id="quantity-${book.bookid}" type="number" name="quantity" class="form-control" style="width: 6rem" min="1" max="${book.quantity}" step="1" value="1" required>
            <button class="btn btn-primary">Thêm vào giỏ hàng</button>
        </form>
    </c:when>
    <c:otherwise><p class="text-danger mt-3">Sách hiện không có sẵn để mua.</p></c:otherwise>
</c:choose>
			</td>
		</tr>

		<!-- Danh sach review -->
		<tr>
			<th colspan="2" class="table-light">Reviews</th>
		</tr>
		<tr>
			<td colspan="2">
				<c:forEach var="r" items="${reviews}">
					<p class="mb-2">
						<strong>${r.user.fullname}</strong>
						<span class="text-warning">
							<c:forEach begin="1" end="${r.rating}">&#9733;</c:forEach>
						</span>: ${r.reviewText}
					</p>
				</c:forEach>
				<c:if test="${empty reviews}">
					<p class="text-muted mb-0">Chưa có review nào.</p>
				</c:if>
			</td>
		</tr>

		<!-- Form them review -->
		<tr>
			<th colspan="2" class="table-light">Form thêm reviews</th>
		</tr>
		<tr>
			<td colspan="2">
				<c:choose>
					<c:when test="${sessionScope.account != null}">
						<form method="post" action="${ctx}/book/review">
							<input type="hidden" name="bookId" value="${book.bookid}">
							<div class="mb-2" style="max-width: 200px">
								<label class="form-label">Đánh giá</label>
								<select name="rating" class="form-select">
									<option value="5">5 sao</option>
									<option value="4">4 sao</option>
									<option value="3">3 sao</option>
									<option value="2">2 sao</option>
									<option value="1">1 sao</option>
								</select>
							</div>
							<div class="mb-2">
								<label class="form-label">Nội dung review</label>
								<textarea name="reviewText" rows="3" class="form-control" required></textarea>
							</div>
							<button class="btn btn-primary">Submit</button>
						</form>
					</c:when>
					<c:otherwise>
						<p class="mb-0">Vui lòng <a href="${ctx}/login">đăng nhập</a> để viết review.</p>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
	<a href="${ctx}/" class="btn btn-secondary">&laquo; Quay lại</a>
</body>
</html>
