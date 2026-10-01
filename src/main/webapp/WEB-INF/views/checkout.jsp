<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head><title>Thanh toán COD</title></head>
<body>
    <h3>Thanh toán khi nhận hàng (COD)</h3>
    <c:if test="${not empty checkoutError}"><div class="alert alert-danger" role="alert"><c:out value="${checkoutError}" /></div></c:if>
    <c:if test="${not empty checkoutWarning}"><div class="alert alert-warning" role="status"><c:out value="${checkoutWarning}" /></div></c:if>
    <div class="row g-4">
        <div class="col-lg-6">
            <h4>Thông tin nhận hàng</h4>
            <form method="post" action="${ctx}/checkout">
                <input type="hidden" name="cartToken" value="${sessionScope.cartToken}">
                <input type="hidden" name="checkoutToken" value="${draft.token}">
                <div class="mb-3">
                    <label for="recipientName" class="form-label">Họ tên người nhận</label>
                    <input id="recipientName" name="recipientName" class="form-control" autocomplete="name" minlength="2" maxlength="100" required value="<c:out value='${shipping.recipientName}' />">
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Số điện thoại</label>
                    <input id="phone" name="phone" type="tel" class="form-control" autocomplete="tel" pattern="\+?[0-9]{9,15}" maxlength="16" required aria-describedby="phoneHelp" value="<c:out value='${shipping.phone}' />">
                    <small id="phoneHelp" class="text-muted">Nhập 9–15 chữ số, có thể bắt đầu bằng dấu +.</small>
                </div>
                <div class="mb-3">
                    <label for="address" class="form-label">Địa chỉ nhận hàng</label>
                    <textarea id="address" name="address" class="form-control" autocomplete="street-address" rows="3" minlength="10" maxlength="500" required aria-describedby="addressHelp"><c:out value="${shipping.address}" /></textarea>
                    <small id="addressHelp" class="text-muted">Ghi rõ số nhà, đường, phường/xã và tỉnh/thành phố.</small>
                </div>
                <div class="mb-3">
                    <label for="note" class="form-label">Ghi chú (không bắt buộc)</label>
                    <textarea id="note" name="note" class="form-control" rows="2" maxlength="1000"><c:out value="${shipping.note}" /></textarea>
                </div>
                <p class="alert alert-info">Phương thức thanh toán: <strong>COD</strong>. Bạn thanh toán khi nhận hàng.</p>
                <button class="btn btn-success">Xác nhận đặt hàng COD</button>
                <a href="${ctx}/cart" class="btn btn-outline-secondary">Quay lại giỏ hàng</a>
            </form>
        </div>
        <div class="col-lg-6">
            <h4>Kiểm tra đơn hàng</h4>
            <div class="table-responsive">
                <table class="table table-bordered bg-white">
                    <thead><tr><th scope="col">Sách</th><th scope="col">Đơn giá</th><th scope="col">Số lượng</th><th scope="col">Thành tiền</th></tr></thead>
                    <tbody>
                        <c:forEach var="item" items="${draft.items}">
                            <tr>
                                <td><c:out value="${item.title}" /></td>
                                <td><fmt:formatNumber value="${item.price}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                <td>${item.quantity}</td>
                                <td><fmt:formatNumber value="${item.subtotal}" minFractionDigits="2" maxFractionDigits="2" /></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot><tr><th colspan="3">Tổng tiền thanh toán</th><td class="fw-bold"><fmt:formatNumber value="${draft.total}" minFractionDigits="2" maxFractionDigits="2" /></td></tr></tfoot>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
