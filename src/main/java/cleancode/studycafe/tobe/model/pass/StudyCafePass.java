package cleancode.studycafe.tobe.model.pass;

public interface StudyCafePass {

    StudyCafePassType getPassType(); // 패스 타입

    int getDuration(); // 기간

    int getPrice(); // 가격
}
