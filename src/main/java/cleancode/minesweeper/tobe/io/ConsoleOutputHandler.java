package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.studycafe.asis.exception.GameException;

import java.util.List;
import java.util.stream.IntStream;

public class ConsoleOutputHandler implements OutputHandler {

    @Override
    public void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void showBoard(GameBoard board) {
        String alphabets = generateColAlphabets(board);

        System.out.println("    " + alphabets);
        for (int row = 0; row < board.getRowSize(); row++) { // row size
            System.out.printf("%2d  ", row + 1); // %2d 공백 맞추기
            for (int col = 0; col < board.getColSize(); col++) { // col size
                CellPosition cellPosition = CellPosition.of(row, col);
                System.out.print(board.getSign(cellPosition) + " "); // 이럴때 getter 사용!! 너 데이터 줘! 내놔
            }
            System.out.println();
        }
    }

    private String generateColAlphabets(GameBoard board) {
        // 아스키코드 연산을 통해 'a'부터 시작하는 알파벳들을 만들기
        List<String> alphabets = IntStream.range(0, board.getColSize())
                .mapToObj(index -> (char) ('a' + index))
                .map(Object::toString)
                .toList();
        return String.join(" ", alphabets);
    }

    @Override
    public void showGameWinningComment() {
        System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
    }

    @Override
    public void showGameLosingComment() {
        System.out.println("지뢰를 밟았습니다. GAME OVER!");
    }

    @Override
    public void showCommentForSelectingCell() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
    }

    @Override
    public void showCommentForUserAction() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
    }

    @Override
    public void showExceptionMessage(GameException e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void showSimpleMessage(String message) {
        System.out.println(message);
    }

}
