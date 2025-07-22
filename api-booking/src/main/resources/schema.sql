CREATE DATABASE IF NOT EXISTS `booking_db`;
USE `booking_db`;

DROP TABLE IF EXISTS `booking`;
CREATE TABLE `booking` (
        `booking_id` BIGINT NOT NULL AUTO_INCREMENT,
        `booking_reference` VARCHAR(10) UNIQUE NOT NULL, --wt001234
        `user_id` BIGINT NOT NULL,
        `flight_id` BIGINT NOT NULL,
        `payment_id` BIGINT NOT NULL,
        `booking_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        `travel_date` DATE NOT NULL,
        `return_date` DATE, --Null si es solo un día
        `adult_passengers` INT DEFAULT 1,
        `child_passengers` INT DEFAULT 0, --2 a 11 años
        `infant_passengers` INT DEFAULT 0, --0 a 23 meses
        `total_passengers` INT GENERATED ALWAYS AS (adult_passengers + child_passengers + infant_passengers),
        `total_amount` DECIMAL(10,2) NOT NULL,
        `currency` VARCHAR(3) DEFAULT 'USD',
        `booking_status` VARCHAR(20) NOT NULL,
        `special_requests` TEXT,
        `booking_notes` TEXT,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `expires_at` TIMESTAMP --para reservas temporales
);