package com.wingtrip.booking.repository;

import com.wingtrip.booking.model.BookingEntity;
import com.wingtrip.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Optional<BookingEntity> findByBookingReference(String reference);
    List<BookingEntity> findByUserId(Long userId);
    List<BookingEntity> findByUserIdAndBookingStatus(Long userId, BookingStatus bookingStatus);
    List<BookingEntity> findByFlightId(Long flightId);

}
