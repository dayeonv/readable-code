package cleancode.minesweeper.tobe.minesweeper.board;

public enum GameStatus {

    // 원래는 int gamestatus = 0, 1, -1로 관리 했었음

    IN_PROGRESS("진행중"),
    WIN("승리"),
    LOSE("패배"),
    ;

    private final String description;

    GameStatus(String description) {
        this.description = description;
    }
}
