package roomescape.registration.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.Repository;
import roomescape.registration.reservation.domain.Reservation;

public interface ReservationRepository extends Repository<Reservation, Long> {

    Reservation save(Reservation reservation);

    List<Reservation> findAllByOrderByDateAscReservationTimeAsc();

    List<Reservation> findAllByThemeIdAndDate(long themeId, LocalDate date);

    List<Reservation> findAllByMemberIdAndThemeIdAndDateBetween(
            long memberId,
            long themeId,
            LocalDate fromDate,
            LocalDate toDate
    );

    List<Reservation> findByReservationTimeId(long timeId);

    List<Reservation> findByThemeId(long themeId);

    void deleteById(long reservationId);

    List<Reservation> findAllByMemberId(long id);

    boolean existsByDateAndThemeIdAndReservationTimeIdAndMemberId(LocalDate date, long themeId, long reservationTimeId, long memberId);
}
