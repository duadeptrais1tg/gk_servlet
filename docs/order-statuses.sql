-- Chay tren MySQL. Mo /orders de xem lich su, chon trang thai va bam Loc don hang.
USE bookstore_24162126;

-- Cac gia tri hop le cua customer_orders.status:
-- PENDING    : Don hang moi (giu nguyen ma cua cac don COD da tao)
-- CONFIRMED  : Da xac nhan
-- PREPARING  : Chuan bi hang
-- SHIPPING   : Van chuyen (dang luan chuyen giua cac diem)
-- DELIVERING : Giao hang (dang giao den nguoi nhan)
-- DELIVERED  : Da giao
-- CANCELLED  : Don hang huy
-- RETURNED   : Don hang hoan

SELECT id, user_id, recipient_name, created_at, status, payment_status, total
FROM customer_orders ORDER BY created_at DESC, id DESC;

-- Thay NULL bang ma don can thu, vi du SET @order_id = 1;
-- Chon mot ma trang thai trong danh sach tren.
SET @order_id = NULL;
SET @new_status = 'CONFIRMED';
UPDATE customer_orders
SET status = @new_status
WHERE id = @order_id
  AND @new_status IN ('PENDING', 'CONFIRMED', 'PREPARING', 'SHIPPING',
                      'DELIVERING', 'DELIVERED', 'CANCELLED', 'RETURNED');
COMMIT;
SELECT id, status, payment_status FROM customer_orders WHERE id = @order_id;

-- Tai lai /orders hoac /order?id=... sau khi cap nhat.
-- Don se roi bo loc cu va xuat hien o bo loc moi; khong can khoi dong lai ung dung.
-- Don co user_id: dang nhap dung tai khoan. Don khach: dung phien da dat hang.
-- status va payment_status doc lap. Sua status chi phuc vu quan sat trang thai,
-- khong tu dong hoan ton kho, thu tien hay hoan tien.
-- Neu muon mo phong da thu tien COD, sua rieng payment_status thanh 'PAID'.
-- payment_status ho tro: UNPAID, PAID, REFUNDED.
