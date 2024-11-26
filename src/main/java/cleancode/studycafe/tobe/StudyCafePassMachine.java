package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.order.StudyCafePassOrder;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPasses;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPasses;
import cleancode.studycafe.tobe.provider.LockerPassProvider;
import cleancode.studycafe.tobe.provider.SeatPassProvider;

import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

    private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
    private final SeatPassProvider seatPassProvider;
    private final LockerPassProvider lockerPassProvider;

    public StudyCafePassMachine(SeatPassProvider seatPassProvider, LockerPassProvider lockerPassProvider) {
        this.seatPassProvider = seatPassProvider;
        this.lockerPassProvider = lockerPassProvider;
    }

    // ** 핵사고날 아키텍처 - 포트와 어댑터
    // 포트 : 지금처럼 인터페이스 이야기. 무엇이든 규격만 맞으면 꽂을 수 있는 플러그처럼 스펙을 얘기함.
    // 포트와 어댑터는 포트에 맞는 구현체를 말함!
    // => 상위 레벨의 추상화 된 스펙을 뽑아서 인터페이스화 해놓고 구현체를 필요에 따라서 바꿔 끼울 수 있는 것.

    public void run() {
        try {
            ioHandler.showWelcomeMessage();
            ioHandler.showAnnouncement();

            StudyCafeSeatPass selectedPass = selectPass(); // 이용권 선택하는 과정을 메서드 하나로 합침
            Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass); // 사물함 로직 메서드로 추출
            StudyCafePassOrder passOrder = StudyCafePassOrder.of(
                    selectedPass,
                    optionalLockerPass.orElse(null)
            );

            ioHandler.showPassOrderSummary(passOrder);

        } catch (AppException e) {
            ioHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private StudyCafeSeatPass selectPass() {
        StudyCafePassType passType = ioHandler.askPassTypeSelecting();
        List<StudyCafeSeatPass> passCandidates = findPassCandidatesBy(passType); // 이용권 후보 고르는 메서드

        return ioHandler.askPassSelecting(passCandidates);
    }

    private List<StudyCafeSeatPass> findPassCandidatesBy(StudyCafePassType studyCafePassType) {
        StudyCafeSeatPasses allPasses = seatPassProvider.getSeatPasses();
        return allPasses.findPassBy(studyCafePassType);
    }

    private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafeSeatPass selectedPass) {
        // 고정석이 아니면 -> 낮은 추상화 레벨
        // 사물함 옵션을 사용할 수 있는 타입이 아니면 -> 높은 추상화 레벨
        if (selectedPass.cannotUseLocker()) { // 고정석이 아니면
            return Optional.empty(); // null 사용을 자제하자. 웬만하면 optional 쓰기
        }

        Optional<StudyCafeLockerPass> lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

        if (lockerPassCandidate.isPresent()) { // 사물함권이 있다면
            StudyCafeLockerPass lockerPass = lockerPassCandidate.get();

            boolean isLockerSelected = ioHandler.askLockerPass(lockerPass);
            if (isLockerSelected) { // 선택한다고 했다면
                return Optional.of(lockerPass); // 걔를 반환함
            }
        }

        return Optional.empty();
    }

    private Optional<StudyCafeLockerPass> findLockerPassCandidateBy(StudyCafeSeatPass pass) {
        StudyCafeLockerPasses allLockerPasses = lockerPassProvider.getLockerPasses();

        // 타입과 기간이 같은 사물함 이용권을 찾아서 반환, 없으면 null
        return allLockerPasses.findLockerPassBy(pass);
    }

}
