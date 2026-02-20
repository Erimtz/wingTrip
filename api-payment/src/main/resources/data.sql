INSERT INTO payment (payment_type, payment_status, payment_date, amount, currency, booking_id)
VALUES
    ('CREDIT_CARD', 'SUCCESS', '2025-07-01 10:00:00', 450.00, 'USD', 1),
    ('DEBIT_CARD', 'PENDING', '2025-07-02 11:00:00', 320.50, 'USD', 2),
    ('CREDIT_CARD', 'SUCCESS', '2025-07-03 12:00:00', 280.75, 'USD', 3),
    ('PAYPAL', 'SUCCESS', '2025-07-04 13:00:00', 890.00, 'USD', 4),
    ('CREDIT_CARD', 'FAILED', '2025-07-05 14:00:00', 199.99, 'USD', 5);