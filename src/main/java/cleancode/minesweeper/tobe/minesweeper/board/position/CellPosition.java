package cleancode.minesweeper.tobe.minesweeper.board.position;

import java.util.Objects;

public class CellPosition {

    // ** Value Object **
    // 원래 항상 같이 다니던 row, col이 따로 놀고 있던 거를
    // CellPosition이라는 Value Object를 만듦
    // 재귀함수 로직에서 CellPosition이 유효성 검증의 로직을 가졌는데 상대 좌표에 관한건 어떻게 처리하지?
    // 또 다른 개념인 RelativePosition 상대좌표를 추출해냄
    // 1. 불변성 2. 동등성 3. 유효성 검증

    // 불변성
    private final int rowIndex;
    private final int colIndex;

    public CellPosition(int rowIndex, int colIndex) {
        // 개발자가 확인해주세요. 라는 형태로 예외 메시지를 발행
        if (rowIndex < 0 || colIndex < 0) {
            throw new IllegalArgumentException("올바르지 않은 좌표입니다.");
        }
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public static CellPosition of(int rowIndex, int colIndex) {
        return new CellPosition(rowIndex, colIndex);
    }

    // equals와 hashcode를 재정의해서 동등성을 보장해주자
    // 서로 다른 두 인스턴스는 메모리 주소가 달라도 깉은 동등한 객체로 인식하게 됨
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellPosition that = (CellPosition) o;
        return rowIndex == that.rowIndex && colIndex == that.colIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, colIndex);
    }

    public boolean isRowIndexMoreThanOrEqual(int rowIndex) {
        return this.rowIndex >= rowIndex;
    }

    public boolean isColIndexMoreThanOrEqual(int colIndex) {
        return this.colIndex >= colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    // 릴레이티브 포지션이 주어졌을 때 포지션을 계산할 수 있니?
    // = 셀 포지션을 만들었을 때 예외가 발생하지 않는지를 보는 것
    public boolean canCalculatePositionBy(RelativePosition relativePosition) {
        return this.rowIndex + relativePosition.getDeltaRow() >= 0
                && this.colIndex + relativePosition.getDeltaCol() >= 0;
    }

    public CellPosition calculatePositionBy(RelativePosition relativePosition) {
        if (this.canCalculatePositionBy(relativePosition)) { // 이 메서드가 혹시 단독으로 호출됐을 때를 위한 보험
            return CellPosition.of(
                    this.rowIndex + relativePosition.getDeltaRow(),
                    this.colIndex + relativePosition.getDeltaCol()
            );
        }
        throw new IllegalArgumentException("움직일 수 있는 좌표가 아닙니다.");
    }

    public boolean isRowIndexLessThan(int rowIndex) {
        return this.rowIndex < rowIndex;
    }

    public boolean isColIndexLessThan(int colIndex) {
        return this.colIndex < colIndex;
    }
}
