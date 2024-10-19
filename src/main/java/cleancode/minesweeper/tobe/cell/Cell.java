package cleancode.minesweeper.tobe.cell;

// 셀을 추출해서 관심사를 한 데로 모으게 되면서 새로운 도메인 지식을 얻음
// 열렸다/닫혔다, 체크했다/안했다 를 구분해서 보기

// 상속과 조합 : 추상 클래스에서 인터페이스로 바꾸기
public interface Cell {

    // 상수 : 매직 스트링 4개 추출
    String FLAG_SIGN = "⚑";
    String UNCHECKED_SIGN = "□"; // closed_cell_sign

    // Cell이 가진 속성 : 근처 지뢰 숫자, 지뢰 여부
    // Cell의 상태 : 깃발 유무, 열렸다/닫혔다, 사용자가 확인함(지뢰있을 것 같아!하고 깃발을 꽂은 셀)

    // 강사님은 보통 생성자로 new Cell 이렇게 생성하는 것 보다
    // 정적 팩토리 메서드를 즐겨씀 -> 이름을 별도로 줄 수 있어서!

    // 추상 메서드
    boolean isLandMine(); // 너 지뢰야?

    boolean hasLandMineCount();

    String getSign(); // 지금 현재 셀의 맞는 sign은 뭐야? 지금 셀 상태를 getSign()에서 결정하도록 함

    // 객체를 만들 때 getter, setter 는 처음부터 만들지 않는다!!
    // 공개 메서드로 외부 세계와 소통하기! getter 쓰지 않기
    // 부정연산자(!)를 지우고, 메서드로

    void flag();

    void open();

    boolean isChecked();

    boolean isOpened();

}
