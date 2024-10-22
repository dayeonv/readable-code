package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.minesweeper.Minesweeper;
import cleancode.minesweeper.tobe.minesweeper.config.GameConfig;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.Beginner;
import cleancode.minesweeper.tobe.minesweeper.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.minesweeper.io.ConsoleOutputHandler;

// 클래스 이름을 MinesweeperGame에서 GameApplication으로 변경
// -> 게임이 여러가지일 때, 이 메인함수에서 지뢰찾기도 실행하고, 다른 게임도 실행해볼 수 있는 관점도 생긴다.

public class GameApplication {

    public static void main(String[] args) {
        GameConfig gameConfig = new GameConfig(
                new Beginner(),
                new ConsoleInputHandler(),
                new ConsoleOutputHandler()
        );

        Minesweeper minesweeper = new Minesweeper(gameConfig);
        minesweeper.initialize();
        minesweeper.run();
    }

}

/* SRP 단일 책임 원칙 : 메인에서는 프로그램을 실행하는 역할만 하게 하고, 지뢰 찾기 도메인 클래스를 별도로 둬보자
    1. 프로그램을 실행하는 진입점의 역할을 하는 부분과 실제 지뢰 찾기 도메인을 가지고 지뢰 찾기 게임을 실행하는 부분으로 나눔
    2. 입출력에 대한 것은 별개의 책임이 아닐까? 질문을 시작으로 입력과 출력을 담당하는 클래스로 나누고, 더 높은 추상화레벨에서 로직이 돌아갈 수 있게 함
    콘솔 아웃풋 핸들러와 마인스위퍼가 협력을 하고 있음
    3. 게임보드도 분리를 함.
*/

/* OCP 개방-폐쇄 원칙
    1. 새로운 요구 사항이 들어왔다.
        - 게임의 난이도를 변경할 수 있어야 한다.
        - 난이도
            - 매우 쉬움 : 가로 5, 세로 4, 지뢰 2
            - 초급 : 가로 10, 세로 8, 지뢰 10
            - 중급 : 가로 18, 세로 14, 지뢰 40
            - 고급 : 가로 24, 세로 20, 지뢰 99
    2. 어떤 난이도가 들어와도 유동적으로 게임을 진행시킬 수 있다.
*/

/* LSP 리스코프 치환 원칙
    1. Cell 부모와 자식
        - 지뢰 Cell
        - 숫자 Cell
        - 빈 Cell
    2. LSP 위반 행위
        - 부모가 동작할 거라고 기대했던 행동을 자식이 안하고 있음
        - 부모 클래스에 정의된 기능을 예상한 대로 동작을 하지 않고 있음
        - 상속 구조에서는 타입 체크가 필요없는 것이 정상
*/

/* ISP 인터페이스 분리 원칙
    1. 인터페이스를 기능 단위로 잘게 쪼개라
*/

/* DIP 의존성 역전 원칙 Dependency Inversion Principle (순방향, 역방향)
    - 고수준 모듈과 저수준 모듈이 직접적으로 의존하는 것이 아니라 추상화에 서로 의존해야 한다.
    - 인터페이스나 추상화를 통해 돌아가야 한다.

    스프링 3대 요소 IOC/DI, PSA, AOP
        DI : Dependency Injection 의존성 주입
            - 필요한 의존성을 외부에서 주입받겠다.
            - "3" : 제3자가 항상 두 객체간 의존성을 맺어주고 주입해준다.
        IOC : Inversion of Control 제어의 역전
            - 프로그램의 흐름을 개발자가 아닌 프레임워크가 담당하도록 하는 것
            - 프레임워크가 메인, 내 코드는 프레임워크의 일부로 동작하게 되면서 '제어'가 프레임워크 쪽으로 넘어감
            - 프로그랭믜 제어의 주도권이 프레임워크로 가는 것. 개발자가 아니라!
            ex) 스프링 프레임워크 : 우리가 어떤 코드를 입력했을 때 스프링이 제공하는 여러가지 기능들을 쓰면서
            그 규격에 맞춰 코딩하는 것. -> 이게 제어의 역전
            - IOC 컨테이너가 객체를 직접적으로 생성해주고, 생명주기관리를 해줌

*/