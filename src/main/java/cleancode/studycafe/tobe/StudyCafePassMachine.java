package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.util.List;
import java.util.function.Predicate;

// *** 스터디 카페 이용권 선택 시스템 ***
public class StudyCafePassMachine {

    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();

    // 스터디 카페 이용권 목록 파일 가져오기
    StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
    List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePasses();

    public void run() {
        try {
            outputHandler.showWelcomeMessage();     // 환영인사
            outputHandler.showAnnouncement();       // 공지사항
            outputHandler.askPassTypeSelection();   // 이용권 선택 (시간권, 주권, 고정석)

            // 유저가 선택한 이용권에 대한 액션 가져오기
            StudyCafePassType studyCafePassType = inputHandler.getPassTypeSelectingUserAction();

            // 이용권 선택하기
            if (isHourlyPassSelected(studyCafePassType)) {          // 시간권
                StudyCafePass selectedPass = selectPassByFilter(studyCafePasses, pass -> isHourlyPassSelected(pass.getPassType()));
                outputHandler.showPassOrderSummary(selectedPass, null);

            } else if (isWeeklyPassSelected(studyCafePassType)) {   // 주권
                StudyCafePass selectedPass = selectPassByFilter(studyCafePasses, pass -> isWeeklyPassSelected(pass.getPassType()));
                outputHandler.showPassOrderSummary(selectedPass, null);

            } else if (isFixedPassSelected(studyCafePassType)) {    // 고정석
                StudyCafePass selectedPass = selectPassByFilter(studyCafePasses, pass -> isFixedPassSelected(pass.getPassType()));

                // 사물함 이용권 정보 찾기
                StudyCafeLockerPass lockerPass = matchLockerPass(selectedPass);

                // 사물함 이용권 사용 여부 확인
                boolean lockerSelection = isLockerSelected(lockerPass);

                // 사물함 이용 선택 : 삼항연산자 <lockerSelection> true -> lockerPass 전달, false -> null 전달
                outputHandler.showPassOrderSummary(selectedPass, lockerSelection ? lockerPass : null);

            }
        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }


    // 사물함 이용권 사용 여부 확인
    private boolean isLockerSelected(StudyCafeLockerPass lockerPass) {
        if (isLockerPassValid(lockerPass)) {
            outputHandler.askLockerPass(lockerPass);
            return inputHandler.getLockerSelection();
        }
        return false;
    }

    // 사물함 이용권 정보 찾기
    private StudyCafeLockerPass matchLockerPass(StudyCafePass selectedPass) {
        List<StudyCafeLockerPass> lockerPasses = studyCafeFileHandler.readLockerPasses();
        return lockerPasses.stream()
                .filter(option -> option.getPassType().equals(selectedPass.getPassType())
                        && option.getDuration() == selectedPass.getDuration())
                .findFirst()
                .orElse(null);
    }

    // 시간권, 주권, 고정석 공통 메서드 추출
    private StudyCafePass selectPassByFilter(List<StudyCafePass> passes, Predicate<StudyCafePass> filter) {
        List<StudyCafePass> filteredPasses = passes.stream()
                .filter(filter)
                .toList();
        outputHandler.showPassListForSelection(filteredPasses);
        return inputHandler.getSelectPass(filteredPasses);
    }

    // 사물함 이용권 여부 메서드
    private static boolean isLockerPassValid(StudyCafeLockerPass lockerPass) {
        return lockerPass != null;
    }

    // 3. 고정석 선택 메서드 추출
    private static boolean isFixedPassSelected(StudyCafePassType studyCafePassType) {
        return studyCafePassType == StudyCafePassType.FIXED;
    }

    // 2. 주권 선택 메서드 추출
    private static boolean isWeeklyPassSelected(StudyCafePassType studyCafePassType) {
        return studyCafePassType == StudyCafePassType.WEEKLY;
    }

    // 1. 시간권 선택 메서드 추출
    private static boolean isHourlyPassSelected(StudyCafePassType studyCafePassType) {
        return studyCafePassType == StudyCafePassType.HOURLY;
    }

}

/* ▶ 리팩토링 포인트 ◀
    - 추상화 레벨
    - 객체로 묶어볼만한 것은 없는지
    - 객체지향 패러다임에 맞게 객체들이 상호 협력하고 있는지
    - SRP : 책임에 따라 응집도 있게 객체가 잘 나뉘어져 있는지
    - DIP : 의존관계 역전을 적용할만한 곳은 없는지
    - 일급 컬렉션
 */
