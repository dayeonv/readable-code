package cleancode.minesweeper.tobe.minesweeper.io;

import cleancode.studycafe.asis.exception.AppException;

// 셀 인풋 스트링을 받아서 인덱스를 각각 변환해주는 친구
public class BoardIndexConverter {

    public static final char BASE_CHAR_FOR_COL = 'a';

    public int getSelectedRowIndex(String cellInput) {
        String cellInputRow = cellInput.substring(1);
        return convertRowFrom(cellInputRow);
    }

    public int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private int convertRowFrom(String cellInputRow) { // "10"
        int rowIndex = Integer.parseInt(cellInputRow) - 1; // 10이 들어오면 인덱스로는 9가 나올 것임
        // 0부터 시작하는 숫자로 바꾸기 위해서 배열[0,0]
        if (rowIndex < 0) {
            throw new AppException("잘못된 입력입니다.");
        }

        return rowIndex;
    }

    private int convertColFrom(char cellInputCol) { // 'a'
        int colIndex = cellInputCol - BASE_CHAR_FOR_COL; // a가 들어가면 0, b가 들어가면 1이 나올 것
        if (colIndex < 0) {
            throw new AppException("잘못된 입력입니다."); // 예외 처리를 명확하게 하자
        }

        return colIndex;
    }
}
