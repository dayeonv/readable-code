package cleancode.minesweeper.tobe.minesweeper;

import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.config.GameConfig;
import cleancode.minesweeper.tobe.minesweeper.io.InputHandler;
import cleancode.minesweeper.tobe.minesweeper.io.OutputHandler;
import cleancode.minesweeper.tobe.minesweeper.user.UserAction;
import cleancode.studycafe.asis.exception.AppException;

// 점진적 리팩토링 중요! 기존꺼 복붙해서 하기
// 메서드 바로가기 ctrl + b
// 상수 : 매직 넘버 추출 ctrl + alt + c, 얘는 중요한 숫자야! 유지 보수할 때 주의 깊게 봐야돼


// 게임을 실행하고, 시작하고, 여러가지 출력이나 입력을 중간에서 컨트롤러 하는 역할 = MVC구조 느낌
public class Minesweeper implements GameInitializable, GameRunnable {

    private final GameBoard gameBoard;
    private final InputHandler inputHandler; // 입력 로직 담당
    private final OutputHandler outputHandler; // 출력 로직 담당

    public Minesweeper(GameConfig gameConfig) {
        gameBoard = new GameBoard(gameConfig.getGameLevel());
        this.inputHandler = gameConfig.getInputHandler();
        this.outputHandler = gameConfig.getOutputHandler();
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame(); // 게임 초기화
    }

    @Override
    public void run() {
        outputHandler.showGameStartComments(); // ctrl + alt + m 게임 시작 멘트, 메서드로 추출하기

        while (gameBoard.isInProgress()) { // 지뢰판 그리기 시작
            try {
                outputHandler.showBoard(gameBoard); // 보드판 생성

                System.out.println();

                CellPosition cellPosition = getCellInputFromUser(); // 유저로부터 셀 입력값을 받겠다
                UserAction userAction = getUserActionInputFromUser(); // 액션 입력값
                actOnCell(cellPosition, userAction); // 셀의 좌표와 유저 액션을 입력 받는 메서드
            } catch (AppException e) {
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
                //e.printStackTrace(); // 실무에서는 잘 안씀
            }
        }

        outputHandler.showBoard(gameBoard); // 마지막에도 보드판 생성

        if (gameBoard.isWinStatus()) {
            outputHandler.showGameWinningComment();
        }
        if (gameBoard.isLoseStatus()) {
            outputHandler.showGameLosingComment();
        }

    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showCommentForSelectingCell();
        CellPosition cellPosition = inputHandler.getCellPositionFromUser();
        if (gameBoard.isInvalidCellPosition(cellPosition)) { // 보드가 있는 쪽에서 자연스럽게 검증을 해보자
            throw new AppException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
    }

    private UserAction getUserActionInputFromUser() {
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserActionFromUser();
    }

    private void actOnCell(CellPosition cellPosition, UserAction userAction) {
        if (doesUserChooseToPlantFlag(userAction)) { // 2번 깃발 꽂기를 선택하는 경우
            gameBoard.flagAt(cellPosition);
            return;
        }

        if (doesUserChooseToOpenCell(userAction)) { // 1번을 선택했을 때
            gameBoard.openAt(cellPosition); // 일단 열어줘
            return;
        }
        // 이상한 것을 입력했을 때 예외 처리
        //System.out.println("잘못된 번호를 선택하셨습니다.");
        throw new AppException("잘못된 번호를 선택하셨습니다."); // 우리가 의도한 예외사항이니까 다시 입력해주세요. 라고 사용자에게 메시지
    }

    private boolean doesUserChooseToPlantFlag(UserAction userAction) {
        return userAction == UserAction.FLAG;
    }

    private boolean doesUserChooseToOpenCell(UserAction userAction) {
        return userAction == UserAction.OPEN;
    }

}
