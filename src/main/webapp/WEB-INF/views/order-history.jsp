<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head><title>Lịch sử đặt hàng</title></head>
<body>
    <h3 class="mb-3">Lịch sử đặt hàng</h3>
    <c:if test="${empty sessionScope.account}">
        <p class="text-muted">Bạn đang xem các đơn đặt khi chưa đăng nhập trong phiên hiện tại. <a href="${ctx}/login">Đăng nhập</a> để xem đơn đã đặt bằng tài khoản.</p>
    </c:if>
    <form method="get" action="${ctx}/orders" class="d-flex flex-wrap gap-2 align-items-center mb-3">
        <label for="order-status">Trạng thái đơn hàng</label>
        <select id="order-status" name="status" class="form-select w-auto">
            <option value="" ${empty selectedStatus ? 'selected' : ''}>Tất cả trạng thái</option>
            <c:forEach var="status" items="${statuses}">
                <option value="${status.code}" ${selectedStatus == status.code ? 'selected' : ''}><c:out value="${status.label}" /></option>
            </c:forEach>
        </select>
        <button class="btn btn-primary">Lọc đơn hàng</button>
        <a href="${ctx}/orders" class="btn btn-outline-secondary">Xem tất cả</a>
    </form>
    <p>Tìm thấy <strong>${totalOrders}</strong> đơn hàng.</p>
    <c:choose>
        <c:when test="${empty orders}">
            <div class="alert alert-info" role="status">Chưa có đơn hàng phù hợp với bộ lọc.</div>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table table-bordered align-middle bg-white">
                    <thead><tr><th scope="col">Mã đơn</th><th scope="col">Ngày đặt</th><th scope="col">Người nhận</th><th scope="col">Tổng tiền</th><th scope="col">Trạng thái</th><th scope="col">Thanh toán</th><th scope="col">Chi tiết</th></tr></thead>
                    <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td>#${order.id}</td>
                                <td><c:out value="${order.createdAtDisplay}" /></td>
                                <td><c:out value="${order.recipientName}" /></td>
                                <td><fmt:formatNumber value="${order.total}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                <td><span class="badge ${order.statusBadgeClass}"><c:out value="${order.statusLabel}" /></span></td>
                                <td><c:out value="${order.paymentMethod}" /> — <c:out value="${order.paymentStatusLabel}" /></td>
                                <td><a href="${ctx}/order?id=${order.id}" class="btn btn-sm btn-outline-primary" aria-label="Xem đơn hàng ${order.id}">Xem đơn</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
    <c:if test="${totalPages > 1}">
        <nav aria-label="Phân trang lịch sử đơn hàng" class="d-flex align-items-center gap-3 mb-3">
            <c:if test="${currentPage > 1}"><a class="btn btn-outline-primary" href="${ctx}/orders?status=${selectedStatus}&amp;page=${currentPage - 1}">Trang trước</a></c:if>
            <span>Trang ${currentPage} / ${totalPages}</span>
            <c:if test="${currentPage < totalPages}"><a class="btn btn-outline-primary" href="${ctx}/orders?status=${selectedStatus}&amp;page=${currentPage + 1}">Trang sau</a></c:if>
        </nav>
    </c:if>
    <a href="${ctx}/books" class="btn btn-secondary">Tiếp tục mua sách</a>
</body>
</html>
