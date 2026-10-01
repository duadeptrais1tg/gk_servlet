<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head><title>Giỏ hàng</title></head>
<body>
    <h3>Giỏ hàng</h3>
    <c:if test="${not empty cartMessage}"><div class="alert alert-success" role="status"><c:out value="${cartMessage}" /></div></c:if>
    <c:if test="${not empty cartError}"><div class="alert alert-danger" role="alert"><c:out value="${cartError}" /></div></c:if>
    <c:if test="${not empty cartWarning}"><div class="alert alert-warning" role="status"><c:out value="${cartWarning}" /></div></c:if>
    <c:choose>
        <c:when test="${empty cartItems}">
            <p class="alert alert-info">Giỏ hàng đang trống.</p>
        </c:when>
        <c:otherwise>
            <p class="text-muted">Số lượng mỗi sách từ 1 đến tồn kho hiện tại. Thêm vào giỏ chưa giữ hàng.</p>
            <div class="table-responsive">
                <table class="table table-bordered align-middle bg-white">
                    <thead><tr><th scope="col">Sách</th><th scope="col">Đơn giá</th><th scope="col">Số lượng</th><th scope="col">Thành tiền</th><th scope="col">Thao tác</th></tr></thead>
                    <tbody>
                        <c:forEach var="item" items="${cartItems}">
                            <tr>
                                <td><a href="${ctx}/book?id=${item.bookId}"><c:out value="${item.title}" /></a></td>
                                <td><fmt:formatNumber value="${item.price}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                <td>
                                    <form method="post" action="${ctx}/cart" class="d-flex flex-wrap gap-2 align-items-center">
                                        <input type="hidden" name="cartToken" value="${sessionScope.cartToken}">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="bookId" value="${item.bookId}">
                                        <button type="button" class="btn btn-outline-secondary" data-quantity-step="-1" aria-label="Giảm số lượng" ${item.quantity <= 1 ? 'disabled' : ''}>−</button>
                                        <input type="number" name="quantity" class="form-control" style="width: 6rem" min="1" max="${item.stock}" step="1" value="${item.quantity}" required aria-label="Số lượng sách ${item.bookId}">
                                        <button type="button" class="btn btn-outline-secondary" data-quantity-step="1" aria-label="Tăng số lượng" ${item.quantity >= item.stock ? 'disabled' : ''}>+</button>
                                        <button class="btn btn-primary">Cập nhật</button>
                                    </form>
                                    <small class="text-muted">Còn ${item.stock} cuốn</small>
                                </td>
                                <td><fmt:formatNumber value="${item.subtotal}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                <td>
                                    <form method="post" action="${ctx}/cart">
                                        <input type="hidden" name="cartToken" value="${sessionScope.cartToken}">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="bookId" value="${item.bookId}">
                                        <button class="btn btn-outline-danger">Xóa</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot><tr><th colspan="3" class="text-end">Tổng tiền</th><td colspan="2" class="fw-bold"><fmt:formatNumber value="${cartTotal}" minFractionDigits="2" maxFractionDigits="2" /></td></tr></tfoot>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
    <a href="${ctx}/books" class="btn btn-secondary">Tiếp tục chọn sách</a>
    <script>
    document.querySelectorAll('form input[name="quantity"]').forEach(function (input) {
        var form = input.form;
        function updateButtons() {
            var valid = input.validity.valid;
            form.querySelector('[data-quantity-step="-1"]').disabled = !valid || Number(input.value) <= Number(input.min);
            form.querySelector('[data-quantity-step="1"]').disabled = !valid || Number(input.value) >= Number(input.max);
        }
        input.addEventListener('input', updateButtons);
        form.querySelectorAll('[data-quantity-step]').forEach(function (button) {
            button.addEventListener('click', function () {
                if (!input.reportValidity()) return;
                input.stepUp(Number(button.dataset.quantityStep));
                updateButtons();
                form.requestSubmit();
            });
        });
    });
    </script>
</body>
</html>
