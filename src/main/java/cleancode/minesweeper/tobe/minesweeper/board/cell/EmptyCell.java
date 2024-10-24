package cleancode.minesweeper.tobe.minesweeper.board.cell;

public class EmptyCell implements Cell {

    private static final String EMPTY_SIGN = "■"; // opened_cell_sign

    private final CellState cellState = CellState.initialize(); // 조합으로 만들기

    @Override
    public boolean isLandMine() {
        return false;
    }

    @Override
    public boolean hasLandMineCount() {
        return false;
    }

    @Override
    public CellSnapshot getSnapshot() {
        if (cellState.isOpened()) {
            return CellSnapshot.ofEmpty();
        }
        if (cellState.isFlagged()) {
            return CellSnapshot.ofFlag();
        }
        return CellSnapshot.ofUnchecked();
    }

    @Override
    public void flag() {
        cellState.flag();
    }

    @Override
    public void open() {
        cellState.open();
    }

    @Override
    public boolean isChecked() {
        return cellState.isOpened();
    }

    @Override
    public boolean isOpened() {
        return cellState.isOpened();
    }
}
