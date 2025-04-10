package solutions.brilliant.proceduralGeneration.game;

import org.bukkit.event.Event;
import solutions.brilliant.proceduralGeneration.game.rules.RulePause;

import java.util.Map;

public class RuleExecutor {
    private static RuleExecutor instance;
    private State state = State.PAUSE;

    private final Map<State, Rule> executors = Map.of(
            State.PAUSE, new RulePause()
    );

    private RuleExecutor() {
    }

    public static RuleExecutor getInstance() {
        if (instance == null) {
            instance = new RuleExecutor();
        }
        return instance;
    }

    public void changeState(State state) {
        this.state = state;
        executors.get(state).enter();
    }

    public void tick() {
        tick(state);
    }

    public void tick(State override) {
        executors.get(state).tick();
    }

    public void event(Event event) {
        event(state, event);
    }

    public void event(State override, Event event) {
        executors.get(state).event(event);
    }

    public State getState() {
        return state;
    }

    public enum State {
        PAUSE,
        PREPARING_FOR_GAME,
    }

}
