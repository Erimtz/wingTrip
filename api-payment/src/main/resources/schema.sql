DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment` (
    `payment_id` BIGINT NOT NULL AUTO_INCREMENT,
    `payment_type` VARCHAR(50) NOT NULL,
    `payment_status` VARCHAR(20) NOT NULL,
    `payment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `amount` DECIMAL(10,2) NOT NULL,
    `currency` VARCHAR(3) DEFAULT 'USD',
    `booking_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`payment_id`)
);