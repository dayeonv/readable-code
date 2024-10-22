package cleancode.minesweeper.tobe.minesweeper.io.sign;

import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;

public interface CellSignProvidable {

    boolean supports(CellSnapshot cellSnapshot); // 너 셀 상태에 맞게 지원해줄 수 있어?

    String provide(CellSnapshot cellSnapshot);
}
