package com.vn.thu.model;

import java.io.Serializable;

public class ShippingDetails_24162126 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String recipientName;
    private final String phone;
    private final String address;
    private final String note;

    public ShippingDetails_24162126(String recipientName, String phone, String address, String note) {
        this.recipientName = clean(recipientName);
        this.phone = clean(phone);
        this.address = clean(address);
        this.note = clean(note);
    }

    private static String clean(String value) { return value == null ? "" : value.strip(); }

    public void validate() {
        if (recipientName.length() < 2 || recipientName.length() > 100) {
            throw new IllegalArgumentException("Tên người nhận phải có từ 2 đến 100 ký tự.");
        }
        if (!phone.matches("\\+?[0-9]{9,15}")) {
            throw new IllegalArgumentException("Số điện thoại phải gồm 9 đến 15 chữ số, có thể bắt đầu bằng dấu +.");
        }
        if (address.length() < 10 || address.length() > 500) {
            throw new IllegalArgumentException("Địa chỉ nhận hàng phải có từ 10 đến 500 ký tự.");
        }
        if (note.length() > 1000) {
            throw new IllegalArgumentException("Ghi chú không được vượt quá 1000 ký tự.");
        }
    }

    public String getRecipientName() { return recipientName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getNote() { return note; }
}
