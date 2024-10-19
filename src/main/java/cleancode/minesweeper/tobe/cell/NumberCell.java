package cleancode.minesweeper.tobe.cell;

public class NumberCell implements Cell {

    private final int nearbyLandMineCount; // 주변 셀 지뢰 수

    private final CellState cellState = CellState.initialize(); // 조합으로 만들기

    public NumberCell(int nearbyLandMineCount) {
        this.nearbyLandMineCount = nearbyLandMineCount;
    }

    @Override
    public boolean isLandMine() {
        return false;
    }

    @Override
    public boolean hasLandMineCount() {
        return true;
    }

    @Override
    public String getSign() {
        if (cellState.isOpened()) {
            return String.valueOf(nearbyLandMineCount);
        }
        if (cellState.isFlagged()) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
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
        return cellState.isChecked();
    }

    @Override
    public boolean isOpened() {
        return cellState.isOpened();
    }
}
