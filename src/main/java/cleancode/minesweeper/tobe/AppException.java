package cleancode.minesweeper.tobe;

// 우리가 만든 프로그램 내에서 발생하는 의도한 예외
public class AppException extends RuntimeException {
    // runtimeexception을 상속으로 받음
    public AppException (String message) {
        super(message);
    }
}