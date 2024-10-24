package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();
    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

    public void run() {
        try {
            outputHandler.showWelcomeMessage();
            outputHandler.showAnnouncement();

            StudyCafePass selectedPass = selectPass(); // 이용권 선택하는 과정을 메서드 하나로 합침
            Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass); // 사물함 로직 메서드로 추출

            optionalLockerPass.ifPresentOrElse(
                    lockerPass -> outputHandler.showPassOrderSummary(selectedPass, lockerPass),
                    () -> outputHandler.showPassOrderSummary(selectedPass)
            );

        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private StudyCafePass selectPass() {
        outputHandler.askPassTypeSelection();
        StudyCafePassType passType = inputHandler.getPassTypeSelectingUserAction();

        List<StudyCafePass> passCandidates = findPassCandidatesBy(passType); // 이용권 후보 고르는 메서드

        outputHandler.showPassListForSelection(passCandidates);
        return inputHandler.getSelectPass(passCandidates);
    }

    private List<StudyCafePass> findPassCandidatesBy(StudyCafePassType studyCafePassType) {
        List<StudyCafePass> allPasses = studyCafeFileHandler.readStudyCafePasses();

        return allPasses.stream()
                .filter(studyCafePass -> studyCafePass.isSamePassType(studyCafePassType))
                .toList();
    }

    private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafePass selectedPass) {
        // 고정석이 아니면 -> 낮은 추상화 레벨
        // 사물함 옵션을 사용할 수 있는 타입이 아니면 -> 높은 추상화 레벨
        if (selectedPass.cannotUseLocker()) { // 고정석이 아니면
            return Optional.empty(); // null 사용을 자제하자. 웬만하면 optional 쓰기
        }

        StudyCafeLockerPass lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

        if (lockerPassCandidate != null) { // 사물함권이 있다면
            outputHandler.askLockerPass(lockerPassCandidate); // 사용자한테 물어보고
            boolean isLockerSelected = inputHandler.getLockerSelection();

            if (isLockerSelected) { // 선택한다고 했다면
                return Optional.of(lockerPassCandidate); // 걔를 반환함
            }
        }

        return Optional.empty();
    }

    private StudyCafeLockerPass findLockerPassCandidateBy(StudyCafePass pass) {
        List<StudyCafeLockerPass> allLockerPasses = studyCafeFileHandler.readLockerPasses();

        // 타입과 기간이 같은 사물함 이용권을 찾아서 반환, 없으면 null
        return allLockerPasses.stream()
                .filter(pass::isSameDurationType)
                .findFirst()
                .orElse(null);
    }

}
