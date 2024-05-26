package roomescape.registration.domain.waiting.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.RoomEscapeException;
import roomescape.exception.model.WaitingExceptionCode;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.registration.domain.reservation.repository.ReservationRepository;
import roomescape.registration.domain.waiting.domain.Waiting;
import roomescape.registration.domain.waiting.domain.WaitingWithRank;
import roomescape.registration.domain.waiting.dto.WaitingRequest;
import roomescape.registration.domain.waiting.dto.WaitingResponse;
import roomescape.registration.domain.waiting.repository.WaitingRepository;
import roomescape.registration.dto.RegistrationRequest;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public WaitingService(WaitingRepository waitingRepository, ReservationRepository reservationRepository,
                          ReservationTimeRepository reservationTimeRepository,
                          ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public WaitingResponse addWaiting(WaitingRequest waitingRequest, long memberId) {
        validateAlreadyReservation(waitingRequest, memberId);
        validateAlreadyWaiting(waitingRequest, memberId);

        ReservationTime reservationTime = reservationTimeRepository.findById(waitingRequest.themeId())
                .orElseThrow(() -> new RoomEscapeException(WaitingExceptionCode.WAITING_TIME_IS_PAST_EXCEPTION));
        Theme theme = themeRepository.findById(waitingRequest.themeId())
                .orElseThrow(() -> new RoomEscapeException(WaitingExceptionCode.THEME_INFO_IS_NULL_EXCEPTION));
        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new RoomEscapeException(WaitingExceptionCode.MEMBER_INFO_IS_NULL_EXCEPTION));

        Waiting unSavedWaiting = new Waiting(waitingRequest.date(), theme, reservationTime, member);

        return WaitingResponse.from(waitingRepository.save(unSavedWaiting));
    }

    public List<WaitingResponse> findWaitings() {
        List<Waiting> waitings = waitingRepository.findAll();

        return waitings.stream()
                .map(WaitingResponse::from)
                .toList();
    }

    public Waiting findWaitingById(long id) {
        return waitingRepository.findById(id)
                .orElseThrow(() -> new RoomEscapeException(WaitingExceptionCode.WAITING_NOT_EXIST_EXCEPTION));
    }

    public List<WaitingWithRank> findMemberWaitingWithRank(long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }

    public long countWaitingRank(RegistrationRequest registrationRequest) {
        Waiting waiting = waitingRepository.findByDateAndThemeIdAndReservationTimeIdAndMemberId(
                registrationRequest.date(),
                registrationRequest.themeId(),
                registrationRequest.timeId(),
                registrationRequest.memberId()
        );

        return waitingRepository.countWaitingRankByDateAndThemeIdAndReservationTimeId(
                waiting.getId(),
                registrationRequest.date(),
                registrationRequest.themeId(),
                registrationRequest.timeId()
        );
    }

    public void removeWaiting(long waitingId) {
        waitingRepository.deleteById(waitingId);
    }

    private void validateAlreadyReservation(WaitingRequest waitingRequest, long memberId) {
        boolean existReservation = reservationRepository.existsByDateAndThemeIdAndReservationTimeIdAndMemberId(
                waitingRequest.date(),
                waitingRequest.themeId(),
                waitingRequest.timeId(),
                memberId
        );

        if (existReservation) {
            throw new RoomEscapeException(WaitingExceptionCode.ALREADY_REGISTRATION_EXCEPTION);
        }
    }

    private void validateAlreadyWaiting(WaitingRequest waitingRequest, long memberId) {
        boolean existWaiting = waitingRepository.existsByDateAndThemeIdAndReservationTimeIdAndMemberId(
                waitingRequest.date(),
                waitingRequest.themeId(),
                waitingRequest.timeId(),
                memberId
        );

        if (existWaiting) {
            throw new RoomEscapeException(WaitingExceptionCode.ALREADY_REGISTRATION_EXCEPTION);
        }
    }
}
