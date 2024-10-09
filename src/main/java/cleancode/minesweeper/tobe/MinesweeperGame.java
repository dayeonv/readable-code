package cleancode.minesweeper.tobe;

import cleancode.studycafe.asis.exception.AppException;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    // 메서드 바로가기 ctrl + b
    // 상수 : 매직 넘버 추출 ctrl + alt + c, 얘는 중요한 숫자야! 유지 보수할 때 주의 깊게 봐야돼
    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final int LAND_MINE_COUNT = 10;

    // 스캐너는 한 번 생성하고 계속 재사용할 수 있어서 상수로 만들어 버리기
    public static final Scanner SCANNER = new Scanner(System.in);

    private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE]; // 게임판
    private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE]; // 지뢰 숫자
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE]; // 지뢰들 (지뢰 유무)

    // 상수 : 매직 스트링 4개 추출
    public static final String FLAG_SIGN = "⚑";
    public static final String LAND_MINE_SIGN = "☼";
    public static final String CLOSED_CELL_SIGN = "□";
    public static final String OPENED_CELL_SIGN = "■";

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments(); // ctrl + alt + m 게임 시작 멘트, 메서드로 추출하기
        initializeGame(); // 게임 초기화

        while (true) { // 지뢰판 그리기 시작
            try {
                showBoard(); // 보드판 생성

                if (doesUserWinTheGame()) { // 1이면 승리
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) { // -1이면 종료
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                System.out.println();
                String cellInput = getCellInputFromUser(); // 유저로부터 셀 입력값을 받겠다
                String userActionInput = getUserActionInputFromUser(); // 액션 입력값

                actOnCell(cellInput, userActionInput); // 셀의 좌표와 유저 액션을 입력 받는 메서드
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("프로그램에 문제가 생겼습니다.");
                //e.printStackTrace(); // 실무에서는 잘 안씀
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        // cellInput 분리 -> a1을 a와 1로
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) { // 2번 깃발 꽂기를 선택하는 경우
            BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN; // 얘도 매직스트링
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) { // 1번을 선택했을 때
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) { // 지뢰 셀을 밟은 경우
                BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN; // 보드에 지뢰 표시
                changeGameStatusToLose();
                return;
            }

            // 일반 셀을 밟았다면
            open(selectedRowIndex, selectedColIndex); // 셀 오픈하기
            checkIfGameIsOver();
            return;
        }
        // 이상한 것을 입력했을 때 예외 처리
        //System.out.println("잘못된 번호를 선택하셨습니다.");
        throw new AppException("잘못된 번호를 선택하셨습니다."); // 우리가 의도한 예외사항이니까 다시 입력해주세요. 라고 사용자에게 메시지
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return LAND_MINES[selectedRowIndex][selectedColIndex];
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1; // 게임 종료
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }


    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened();
        if (isAllOpened) { // open이 true로 바뀌면, 지뢰를 밟지 않은 걸로 생각하고
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1; // 게임 승리
    }

    // 3중 뎁스 로직을 이렇게 리팩토링 함
    private static boolean isAllCellOpened() { // stream 하나가 cell
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .noneMatch(CLOSED_CELL_SIGN::equals); // cell에 대해서 closed_cell_sing과 같은 녀석이 없는지 체크하는 것
        // null이 아닌 확정적인 값에서 메서드로 호출하는 습관을 들이자!
    }


    private static int convertRowFrom(char cellInputRow) {
        // 0부터 시작하는 숫자로 바꾸기 위해서 배열[0,0]
        int rowIndex = Character.getNumericValue(cellInputRow) - 1; // ctrl + alt + n
        if (rowIndex > BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }
        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) { // 알파벳을 컬럼 숫자로 치환
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                // 예외 처리를 명확하게 하자
                throw new AppException("잘못된 입력입니다.");
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = CLOSED_CELL_SIGN;
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true; // true면 지뢰가 있는 것. false면 지뢰 없음. 지뢰 세팅
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) { // 지뢰인 칸에서는!
                    NEARBY_LAND_MINE_COUNTS[row][col] = 0; // 현재 지뢰 칸인 경우에는 0으로
                    continue;
                }
                int count = countNearbyLandMines(row, col); // 근방의 지뢰들을 세는 과정의 메서드 -> 내 주변 8칸을 도는 if문
                NEARBY_LAND_MINE_COUNTS[row][col] = count; // 현재 칸 기준으로 주변에 지뢰가 몇 개 있는지 센 카운트 수
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) { // 내 왼쪽 위 대각선에 있는 칸에 지뢰가 있는지?
            count++; // 있으면 카운트를 하나씩 올려줌
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    // 메소드
    private static void open(int row, int col) {
        // 재귀를 사용할 때에는 여러 개의 종단 조거늘 사용해야 함
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) { // 보드 인덱스를 벗어난 것
            return; // 패스
        }
        if (!BOARD[row][col].equals(CLOSED_CELL_SIGN)) { // 이미 열렸는 지를 보는 것
            return; // 패스
        }
        if (isLandMineCell(row, col)) { // 지뢰 셀이면
            return; // 패스
        }
        if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) { // 지뢰 카운트를 갖고 있는 판이라면
            BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]); // 보드에 지뢰가 몇 개 있는지 표기한 후
            return; // 패스
        } else { // 아무 것도 아니라면
            BOARD[row][col] = OPENED_CELL_SIGN; // 열린 빈 셀로 표기해준다
        }
        // 재귀 : 자기 자신을 호출하는 함수
        // 자기 주변에 있는 8개의 셀들을 재귀 함수로 다 탐색함
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
