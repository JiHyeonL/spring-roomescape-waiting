package roomescape.registration.domain.waiting.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Entity
public class Waiting {

    private static final int NULL_ID = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "theme_id", referencedColumnName = "id", nullable = false)
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "reservation_time_id", referencedColumnName = "id", nullable = false)
    private ReservationTime reservationTime;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    public Waiting() {
    }

    public Waiting(LocalDate date, Theme theme, ReservationTime reservationTime, Member member) {
        this.id = NULL_ID;
        this.date = date;
        this.theme = theme;
        this.reservationTime = reservationTime;
        this.member = member;
    }

    public Waiting(long id, LocalDate date, Theme theme, ReservationTime reservationTime, Member member) {
        this.id = id;
        this.date = date;
        this.theme = theme;
        this.reservationTime = reservationTime;
        this.member = member;
    }

    public long getId() {
        return id;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Member getMember() {
        return member;
    }
}