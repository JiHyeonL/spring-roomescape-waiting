package roomescape.reservation.status;

public enum ReservationStatus {

    RESERVED("예약");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}