package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.BoardIndexConverter;
import cleancode.minesweeper.tobe.position.CellPosition;

import java.util.Scanner;

public class ConsoleInputHandler implements InputHandler {

    // 스캐너는 한 번 생성하고 계속 재사용할 수 있어서 상수로 만들어 버리기
    public static final Scanner SCANNER = new Scanner(System.in);

    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

    @Override
    public String getUserInput() {
        return SCANNER.nextLine();
    }

    @Override
    public CellPosition getCellPositionFromUser() {
        String userInput = SCANNER.nextLine();

        int colIndex = boardIndexConverter.getSelectedColIndex(userInput);
        int rowIndex = boardIndexConverter.getSelectedRowIndex(userInput);
        return CellPosition.of(rowIndex, colIndex);
    }
}
