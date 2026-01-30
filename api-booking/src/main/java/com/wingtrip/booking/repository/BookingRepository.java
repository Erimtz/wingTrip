package com.wingtrip.booking.repository;

import com.wingtrip.booking.model.BookingEntity;
import com.wingtrip.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Optional<BookingEntity> findByBookingReference(String reference);
    List<BookingEntity> findByUserId(Long userId);
    List<BookingEntity> findByUserIdAndBookingStatus(Long userId, BookingStatus bookingStatus);
    List<BookingEntity> findByFlightId(Long flightId);

    // Para expiradas (útil para endpoint admin)
    List<BookingEntity> findByExpiresAtBefore(LocalDateTime now);

    // (Opcional) Filtro flexible para futuras mejoras
    @Query("""
    SELECT b FROM BookingEntity b
    WHERE (userId IS NULL OR b.userId = userId)
    AND (status IS NULL OR b.bookingStatus = status)
    AND (from IS NULL OR b.travelDate >= from)
    AND (to IS NULL OR b.travelDate <= to)
""")
    List<BookingEntity> findAllWithFilters(
            @Param("userId") Long userId,
            @Param("status") BookingStatus status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
