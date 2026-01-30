INSERT INTO booking (booking_reference, user_id, flight_id, payment_id, travel_date, return_date, adult_passengers, child_passengers, infant_passengers, total_amount, currency, booking_status, special_requests, booking_notes, expires_at)
VALUES
('WT001234', 1, 101, 1001, '2025-08-15', '2025-08-22', 2, 0, 0, 450.00, 'USD', 'CONFIRMED', 'Window seats preferred', 'Corporate booking', NULL),
('WT001235', 2, 102, NULL, '2025-08-20', NULL, 1, 1, 0, 320.50, 'USD', 'PENDING', 'Vegetarian meal', 'Family trip', '2025-07-25 10:00:00'),
('WT001236', 3, 103, 1002, '2025-09-01', '2025-09-15', 1, 0, 1, 280.75, 'USD', 'PAID', NULL, 'Vacation booking', NULL),
('WT001237', 1, 104, 1003, '2025-09-10', NULL, 3, 2, 0, 890.00, 'USD', 'CONFIRMED', 'Extra legroom', 'Group booking', NULL),
('WT001238', 4, 105, NULL, '2025-09-15', NULL, 1, 0, 0, 199.99, 'USD', 'CANCELLED', NULL, 'Cancelled by user', NULL);