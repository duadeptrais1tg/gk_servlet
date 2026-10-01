package com.vn.thu.model;

public enum OrderStatus_24162126 {
    PENDING("Đơn hàng mới", "text-bg-primary"),
    CONFIRMED("Đã xác nhận", "text-bg-info"),
    PREPARING("Chuẩn bị hàng", "text-bg-warning"),
    SHIPPING("Vận chuyển", "text-bg-info"),
    DELIVERING("Giao hàng", "text-bg-warning"),
    DELIVERED("Đã giao", "text-bg-success"),
    CANCELLED("Đơn hàng hủy", "text-bg-danger"),
    RETURNED("Đơn hàng hoàn", "text-bg-secondary");

    private final String label;
    private final String badgeClass;
    OrderStatus_24162126(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }
    public String getCode() { return name(); }
    public String getLabel() { return label; }
    public String getBadgeClass() { return badgeClass; }
    public static OrderStatus_24162126 fromCode(String code) {
        if (code == null) return null;
        try { return valueOf(code); }
        catch (IllegalArgumentException ignored) { return null; }
    }
}
