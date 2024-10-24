package cleancode.minesweeper.tobe.minesweeper.io;

import cleancode.minesweeper.tobe.minesweeper.board.GameBoard;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.io.sign.CellSignFinder;
import cleancode.minesweeper.tobe.minesweeper.io.sign.CellSignProvider;
import cleancode.studycafe.asis.exception.AppException;

import java.util.List;
import java.util.stream.IntStream;

public class ConsoleOutputHandler implements OutputHandler {

    private final CellSignFinder cellSignFinder = new CellSignFinder();

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

                CellSnapshot snapshot = board.getSnapshot(cellPosition); // 스냅샷 줄 테니까
                //String cellSign = cellSignFinder.findCellSignFrom(snapshot);
                // cellSignFinder는 구현체 5개 다 갖고 있는 친구임
                // -> 상태가 하나 추가될 때마다 구현체도 만들고 구현체를 등록도 해줘야 함
                // -> 이걸 CellSignProvider enum 하나로 만들었음
                // enum 한테 스냅샷 줄 테니까 알아서 cell sign 찾아줘!
                String cellSign = CellSignProvider.findCellSignFrom(snapshot); // sell sign 좀 찾아줘

                System.out.print(cellSign + " "); // 이럴때 getter 사용!! 너 데이터 줘! 내놔
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
    public void showExceptionMessage(AppException e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void showSimpleMessage(String message) {
        System.out.println(message);
    }

}
