package roomescape.registration.domain.waiting.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import roomescape.registration.domain.waiting.domain.WaitingWithRank;
import roomescape.registration.domain.waiting.domain.Waiting;

public interface WaitingRepository extends CrudRepository<Waiting, Long> {

    List<Waiting> findAll();

    @Query("SELECT new roomescape.registration.domain.waiting.domain.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) + 1" +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme.name = w.theme.name " +
            "       AND w2.date = w.date " +
            "       AND w2.reservationTime.startAt = w.reservationTime.startAt " +
            "       AND w2.id < w.id))" +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(long memberId);

    void deleteById(long id);

    boolean existsByDateAndThemeIdAndReservationTimeIdAndMemberId(
            LocalDate date,
            long themeId,
            long reservationTimeId,
            long memberId
    );
}