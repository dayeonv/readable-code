package cleancode.minesweeper.tobe.gamelevel;

// 구체 클래스 : VeryBeginner
public class VeryBeginner implements GameLevel {

    @Override
    public int getRowSize() {
        return 4;
    }

    @Override
    public int getColSize() {
        return 5;
    }

    @Override
    public int getLandMineCount() {
        return 2;
    }
}
