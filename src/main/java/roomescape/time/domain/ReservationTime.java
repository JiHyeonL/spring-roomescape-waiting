package roomescape.time.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.global.exception.model.RoomEscapeException;
import roomescape.time.exception.TimeExceptionCode;

@Entity
public class ReservationTime {

    private static final LocalTime OPEN_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(23, 0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "start_at", nullable = false)
    private LocalTime startAt;

    public ReservationTime() {
    }

    public ReservationTime(LocalTime startAt) {
        this(0, startAt);
        validation();
    }

    public ReservationTime(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(long id) {
        this(id, null);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public void setIdOnSave(long id) {
        this.id = id;
    }

    public void validation() {
        if (startAt == null) {
            throw new RoomEscapeException(TimeExceptionCode.FOUND_TIME_IS_NULL_EXCEPTION);
        }
        if (startAt.isBefore(OPEN_TIME) || startAt.isAfter(CLOSE_TIME)) {
            throw new RoomEscapeException(TimeExceptionCode.TIME_IS_OUT_OF_OPERATING_TIME);
        }
    }

    public boolean isBeforeTime(LocalTime time) {
        return startAt.isBefore(time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime time = (ReservationTime) o;
        return id == time.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}