package cleancode.minesweeper.tobe.minesweeper.config;

import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.minesweeper.io.InputHandler;
import cleancode.minesweeper.tobe.minesweeper.io.OutputHandler;

public class GameConfig {

    // ** [게임 설정 정보] 숨겨진 도메인 추출 ** 미래를 예측한..
    // -> 미래를 너무 내다 봐서 과도한 추상화를 하게 되면 오버 엔지니어링이 될 수 있음ㅎ
    // 우리가 minesweeper 객체로 만들어 놓고, 여기서 게임에 대한 비즈니스 룰을 수행을 함
    // 필요한 것들 3가지 : 게임 레벨, 인풋 핸들러, 아웃풋 핸들러 -> 게임을 어떤 난이도로 수행할건지, 인풋과 아웃풋은 어떻게 할 건지

    private final GameLevel gameLevel;
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    public GameConfig(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
        this.gameLevel = gameLevel;
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public OutputHandler getOutputHandler() {
        return outputHandler;
    }
}
