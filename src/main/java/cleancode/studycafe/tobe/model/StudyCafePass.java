package cleancode.studycafe.tobe.model;

public class StudyCafePass {

    private final StudyCafePassType passType;
    private final int duration;
    private final int price;
    private final double discountRate;

    private StudyCafePass(StudyCafePassType passType, int duration, int price, double discountRate) {
        this.passType = passType;
        this.duration = duration;
        this.price = price;
        this.discountRate = discountRate;
    }

    public static StudyCafePass of(StudyCafePassType passType, int duration, int price, double discountRate) {
        return new StudyCafePass(passType, duration, price, discountRate);
    }

    public boolean isSameDurationType(StudyCafeLockerPass lockerPass) {
        return lockerPass.isSamePassType(this.passType)
                && lockerPass.isSameDuration(this.duration);
    }

    public boolean isSamePassType(StudyCafePassType passType) {
        return this.passType == passType;
    }

    public StudyCafePassType getPassType() {
        return passType;
    }

    public int getDuration() {
        return duration;
    }

    public int getPrice() {
        return price;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public String display() {
        if (passType == StudyCafePassType.HOURLY) {
            return String.format("%s시간권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.WEEKLY) {
            return String.format("%s주권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.FIXED) {
            return String.format("%s주권 - %d원", duration, price);
        }
        return "";
    }

    // 지금 너의 이용권으로 사물함을 사용할 수 있어? -> 라고 높은 추상화 레벨로 질문을 함
    public boolean cannotUseLocker() {
        // 미래에 고정석 말고 다른 이용권이 추가될 수도 있음
        return this.passType.isNotLockerType(); // -> 다시 객체에 질문하기(메시지 보내기)
    }
}
