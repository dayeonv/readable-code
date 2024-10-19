package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.studycafe.asis.exception.GameException;

// 점진적 리팩토링 중요! 기존꺼 복붙해서 하기
// 메서드 바로가기 ctrl + b
// 상수 : 매직 넘버 추출 ctrl + alt + c, 얘는 중요한 숫자야! 유지 보수할 때 주의 깊게 봐야돼
public class Minesweeper implements GameInitializable, GameRunnable {

    private final GameBoard gameBoard;
    private final InputHandler inputHandler; // 입력 로직 담당
    private final OutputHandler outputHandler; // 출력 로직 담당

    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
        gameBoard = new GameBoard(gameLevel);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame(); // 게임 초기화
    }

    @Override
    public void run() {
        outputHandler.showGameStartComments(); // ctrl + alt + m 게임 시작 멘트, 메서드로 추출하기

        while (true) { // 지뢰판 그리기 시작
            try {
                outputHandler.showBoard(gameBoard); // 보드판 생성

                if (doesUserWinTheGame()) { // 1이면 승리
                    outputHandler.showGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) { // -1이면 종료
                    outputHandler.showGameLosingComment();
                    break;
                }
                System.out.println();

                CellPosition cellPosition = getCellInputFromUser(); // 유저로부터 셀 입력값을 받겠다
                String userActionInput = getUserActionInputFromUser(); // 액션 입력값
                actOnCell(cellPosition, userActionInput); // 셀의 좌표와 유저 액션을 입력 받는 메서드
            } catch (GameException e) {
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
                //e.printStackTrace(); // 실무에서는 잘 안씀
            }
        }
    }

    private void actOnCell(CellPosition cellPosition, String userActionInput) {
        if (doesUserChooseToPlantFlag(userActionInput)) { // 2번 깃발 꽂기를 선택하는 경우
            gameBoard.flagAt(cellPosition);
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) { // 1번을 선택했을 때
            if (gameBoard.isLandMineCellAt(cellPosition)) { // 지뢰 셀을 밟은 경우
                gameBoard.openAt(cellPosition); // 셀을 열었으므로, 오픈 처리 해준 다음에
                changeGameStatusToLose(); // 게임 종료하기
                return;
            }

            // 일반 셀을 밟았다면
            gameBoard.openSurroundedCells(cellPosition); // 셀 오픈하기
            checkIfGameIsOver();
            return;
        }
        // 이상한 것을 입력했을 때 예외 처리
        //System.out.println("잘못된 번호를 선택하셨습니다.");
        throw new GameException("잘못된 번호를 선택하셨습니다."); // 우리가 의도한 예외사항이니까 다시 입력해주세요. 라고 사용자에게 메시지
    }

    private void changeGameStatusToLose() {
        gameStatus = -1; // 게임 종료
    }

    private boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private String getUserActionInputFromUser() {
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserInput();
    }

    private CellPosition getCellInputFromUser() {
        outputHandler.showCommentForSelectingCell();
        CellPosition cellPosition = inputHandler.getCellPositionFromUser();
        if (gameBoard.isInvalidCellPosition(cellPosition)) { // 보드가 있는 쪽에서 자연스럽게 검증을 해보자
            throw new GameException("잘못된 좌표를 선택하셨습니다.");
        }

        return cellPosition;
    }

    private boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private void checkIfGameIsOver() {
        if (gameBoard.isAllCellChecked()) { // open이 true로 바뀌면, 지뢰를 밟지 않은 걸로 생각하고
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = 1; // 게임 승리
    }
}
