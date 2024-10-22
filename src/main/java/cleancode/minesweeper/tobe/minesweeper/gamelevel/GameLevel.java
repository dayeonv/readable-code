package cleancode.minesweeper.tobe.minesweeper.gamelevel;

// 인터페이스 : 추상화를 다이렉트로 표현한 구조
public interface GameLevel {

    // 유동적으로 변하는 3가지
    int getRowSize(); // 세로

    int getColSize(); // 가로

    int getLandMineCount(); // 지뢰 개수

}
