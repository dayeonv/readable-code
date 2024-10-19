package cleancode.minesweeper.tobe.cell;

import java.util.Arrays;
import java.util.List;

// ** 일급 컬렉션 **
// 1. 필드가 반드시 컬렉션 하나다.
// 재활용성
public class Cells {

    private final List<Cell> cells;

    public Cells(List<Cell> cells) {
        this.cells = cells;
    }

    // 정적 팩토리 메서드
    public static Cells of(List<Cell> cells) {
        return new Cells(cells);
    }

    public static Cells from(Cell[][] cells) { // cells 이중배열이 들어왔을 때
        // 평탄화를 시켜주고
        List<Cell> cellList = Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .toList();
        return of(cellList);
        // 포장된 cells 일급 컬렉션으로 만들기
    }

    public boolean isAllChecked() {
        return cells.stream()
                .allMatch(Cell::isChecked);
    }
}
