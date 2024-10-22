package cleancode.minesweeper.tobe.minesweeper.board.cell;

public class CellState {
    // cell이 상속 구조에서 두 가지 필드를 cellstate라는 객체로 분리를 했음
    // 각각의 자식클래스들이 cellstate와 조합의 형태로 맺어지고,
    // cell을 인터페이스로 만들어 gameboard와 관련
    // 상속은 결합도가 굉장히 높다.

    private boolean isFlagged; // 깃발이 꽂혀있니?
    private boolean isOpened; // 셀이 열려있니?

    private CellState(boolean isFlagged, boolean isOpened) {
        this.isFlagged = isFlagged;
        this.isOpened = isOpened;
    }

    // 셀이 처음 탄생했을 때의 기본값
    public static CellState initialize() {
        return new CellState(false, false);
    }

    public void flag() {
        this.isFlagged = true; // 얘는 깃발이 꽂힌 상태이다
    }

    public void open() { // 셀 오픈 처리
        this.isOpened = true;
    }

    public boolean isChecked() { // 사용자가 셀을 체크했냐의 조건
        return isFlagged || isOpened; // flagged 거나 opened
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean isFlagged() {
        return isFlagged;
    }
}
