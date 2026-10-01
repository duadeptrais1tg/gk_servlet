<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head><title>Chi tiết đơn hàng</title></head>
<body>
    <c:if test="${orderJustPlaced and order.status == 'PENDING'}"><div class="alert alert-success" role="status">Đặt hàng thành công! Bạn sẽ thanh toán khi nhận hàng.</div></c:if>
    <h3>Đơn hàng #${order.id}</h3>
    <p>Ngày đặt: <c:out value="${order.createdAtDisplay}" /></p>
    <p>Trạng thái đơn: <span class="badge ${order.statusBadgeClass}"><c:out value="${order.statusLabel}" /></span></p>
    <p>Thanh toán: <strong><c:out value="${order.paymentMethod}" /> — <c:out value="${order.paymentStatusLabel}" /></strong></p>
    <div class="card mb-3"><div class="card-body">
        <h4>Thông tin nhận hàng</h4>
        <p>Người nhận: <c:out value="${order.recipientName}" /></p>
        <p>Số điện thoại: <c:out value="${order.phone}" /></p>
        <p style="white-space: pre-line">Địa chỉ: <c:out value="${order.address}" /></p>
        <c:if test="${not empty order.note}"><p style="white-space: pre-line">Ghi chú: <c:out value="${order.note}" /></p></c:if>
    </div></div>
    <div class="table-responsive">
        <table class="table table-bordered bg-white">
            <thead><tr><th scope="col">Sách</th><th scope="col">Đơn giá</th><th scope="col">Số lượng</th><th scope="col">Thành tiền</th></tr></thead>
            <tbody>
                <c:forEach var="item" items="${order.items}">
                    <tr>
                        <td><c:out value="${item.title}" /></td>
                        <td><fmt:formatNumber value="${item.price}" minFractionDigits="2" maxFractionDigits="2" /></td>
                        <td>${item.quantity}</td>
                        <td><fmt:formatNumber value="${item.subtotal}" minFractionDigits="2" maxFractionDigits="2" /></td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot><tr><th colspan="3">Tổng tiền đơn hàng</th><td class="fw-bold"><fmt:formatNumber value="${order.total}" minFractionDigits="2" maxFractionDigits="2" /></td></tr></tfoot>
        </table>
    </div>
    <a href="${ctx}/orders" class="btn btn-outline-secondary">Lịch sử đặt hàng</a>
    <a href="${ctx}/books" class="btn btn-primary">Tiếp tục mua sách</a>
</body>
</html>
