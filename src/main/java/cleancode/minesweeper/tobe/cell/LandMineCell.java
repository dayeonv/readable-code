package cleancode.minesweeper.tobe.cell;

public class LandMineCell implements Cell {

    private static final String LAND_MINE_SIGN = "☼";

    private final CellState cellState = CellState.initialize(); // 조합으로 만들기


    @Override
    public boolean isLandMine() { // 너 지뢰셀이야?
        return true;
    }

    @Override
    public boolean hasLandMineCount() { // 너 지뢰 카운트 가지고 있어?
        return false;
    }

    @Override
    public String getSign() {
        if (cellState.isOpened()) {
            return LAND_MINE_SIGN;
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
