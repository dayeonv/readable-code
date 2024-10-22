package cleancode.minesweeper.tobe.minesweeper.board;

import cleancode.minesweeper.tobe.minesweeper.board.cell.*;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;

import java.util.List;

// 실질적인 지뢰 찾기 도메인 로직을 담당
public class GameBoard {

    private final Cell[][] board; // 게임판
    private final int landMineCount; // 지뢰 개수
    private GameStatus gameStatus;

    // 게임보드 입장에서 게임 레벨을 전달받았는데, 인터페이스여서 런타임시점에 어떤 구현체가 들어올 지는 모르지만
    // 추상화 스펙은 알고 있기 때문에, 거기서 rowSize와 colSize를 받아서 언제든지 유동적으로 실행 가능함
    public GameBoard(GameLevel gameLevel) {
        int rowSize = gameLevel.getRowSize();
        int colSize = gameLevel.getColSize();
        board = new Cell[rowSize][colSize];

        landMineCount = gameLevel.getLandMineCount();
        initializeGameStatus();
    }


    // ** 상태 변경 메서드 **

    // 보드를 초기화해서 새 보드판을 만드는 과정
    public void initializeGame() {
        initializeGameStatus();

        // 특정 보드가 들어왔을 때 모든 셀 포지션들의 묶음을 만들고 싶음.
        CellPositions cellPositions = CellPositions.from(board); // 보드로부터 전체 셀 포지션을 만들어줘

        // 아무것도 없는 빈 셀 만들기
        initializeEmptyCells(cellPositions);

        // 지뢰를 설치하는 과정
        List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
        initializeLandMineCells(landMinePositions);

        // 너가 가진 거에서 파라미터로 주어진 애들을 뺀 나머지 셀 포지션을 줘!
        List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
        initializeNumberCells(numberPositionCandidates);
    }

    public void openAt(CellPosition cellPosition) {
        if (isLandMineCellAt(cellPosition)) { // 지뢰 셀을 밟은 경우
            openOneCellAt(cellPosition); // 셀을 열었으므로, 오픈 처리 해준 다음에
            changeGameStatusToLose(); // 게임 종료하기
            return;
        }

        // 일반 셀을 밟았다면
        openSurroundedCells(cellPosition); // 셀 오픈하기
        checkIfGameIsOver();
    }

    public void flagAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.flag();

        checkIfGameIsOver();
    }


    // ** 판별 **

    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        // 내가 어떤 숫자를 줄 텐데 혹시 너가 갖고있는 rowIndex가 rowSize보다 크거나 같니?
        return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
                || cellPosition.isColIndexMoreThanOrEqual(colSize);
    }

    public boolean isInProgress() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    // 이겼니
    public boolean isWinStatus() {
        return gameStatus == GameStatus.WIN;
    }

    // 졌니
    public boolean isLoseStatus() {
        return gameStatus == GameStatus.LOSE;
    }


    // ** 조회 **

    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSnapshot();
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }



    private void initializeGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getPositions();
        for (CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }

    private void initializeLandMineCells(List<CellPosition> landMinePositions) {
        for (CellPosition position : landMinePositions) {
            updateCellAt(position, new LandMineCell());
        }
    }

    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition candidatePosition : numberPositionCandidates) {
            int count = countNearbyLandMines(candidatePosition);
            if (count != 0) {
                updateCellAt(candidatePosition, new NumberCell(count)); // 현재 칸 기준으로 주변에 지뢰가 몇 개 있는지 센 카운트 수
            }
        }
    }

    private int countNearbyLandMines(CellPosition cellPosition) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        // 상대 좌표로 8개의 셀 포지션 갖고 오기
        // 1. 보드 사이즈 넘어가는지 체크
        // 2. 지뢰 셀인 애들만 카운팅 해줘 .count()
        long count = calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
                .filter(this::isLandMineCellAt)
                .count();

        return (int) count;
    }

    private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
        return RelativePosition.SURROUNDED_POSITIONS.stream()
                .filter(cellPosition::canCalculatePositionBy)
                .map(cellPosition::calculatePositionBy)
                .filter(position -> position.isRowIndexLessThan(rowSize))
                .filter(position -> position.isColIndexLessThan(colSize))
                .toList();
    }

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
        // board[landMinePosition.getRowIndex()][landMinePosition.getColIndex()] = new landMineCell();
    }

    // 재귀 함수
    private void openSurroundedCells(CellPosition cellPosition) {
        // 재귀를 사용할 때에는 여러 개의 종단 조건을 사용해야 함

        if (isOpenedCell(cellPosition)) { // 이미 열렸는 지를 보는 것
            return; // 패스
        }
        if (isLandMineCellAt(cellPosition)) { // 지뢰 셀이면
            return; // 패스
        }

        // 어? 여태까지 다 해당이 안돼?
        // 그럼
        openOneCellAt(cellPosition); // 셀 열어

        // 이때, getter로 LandMine 꺼내서 0이랑 비교하지 말고, 객체를 존중하는 마음으로 물어보자..
        if (doesCellHaveLandMineCount(cellPosition)) { // 열었는데 지뢰 카운트를 갖고 있는 판이라면
            //BOARD[row][col] = Cell.ofNearbyLandMineCount(NEARBY_LAND_MINE_COUNTS[row][col]); // 보드에 지뢰가 몇 개 있는지 표기한 후
            return; // 패스
        }

        // 재귀 : 자기 자신을 호출하는 함수
        // 자기 주변에 있는 8개의 셀들을 재귀 함수로 다 탐색함
        // 새로운 상대 좌표를 기반으로 한 재귀 로직이 돌게 됨
        // for문을 stream 로직으로 바꾸기
        List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
        surroundedPositions.forEach(this::openSurroundedCells);
    }

    private void openOneCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.open();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isOpened();
    }

    private boolean isLandMineCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isLandMine(); // 너 지뢰야? 라고 물어보기
    }

    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.hasLandMineCount();
    }

    private void checkIfGameIsOver() {
        if (isAllCellChecked()) { // open이 true로 바뀌면, 지뢰를 밟지 않은 걸로 생각하고
            changeGameStatusToWin();
        }
    }

    // 3중 뎁스 로직을 이렇게 리팩토링 함
    private boolean isAllCellChecked() { // stream 하나가 cell
        Cells cells = Cells.from(board); // board로 부터 cells 일급 컬렉션을 만들고
        return cells.isAllChecked(); // 모든 셀이 체크되었는지를 물어보기

        // null이 아닌 확정적인 값에서 메서드로 호출하는 습관을 들이자!
    }

    private void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN; // 게임 승리
    }

    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE; // 게임 종료
    }

    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }

}
