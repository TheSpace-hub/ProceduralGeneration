package solutions.brilliant.proceduralGeneration.game;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.rules.DefaultRules;
import solutions.brilliant.proceduralGeneration.game.rules.RulePause;

import java.util.Map;

public class RuleExecutor {
    private static RuleExecutor instance;

    private final Plugin plugin;
    private State state = State.PAUSE;

    private final Map<State, Rule> executors;

    private RuleExecutor(Plugin plugin) {
        this.plugin = plugin;

        executors = Map.of(
                State.DEFAULT, new DefaultRules(plugin),
                State.PAUSE, new RulePause(plugin)
        );
    }

    public static RuleExecutor getInstance(Plugin plugin) {
        if (instance == null) {
            instance = new RuleExecutor(plugin);
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
        executors.get(State.DEFAULT).tick();
        executors.get(state).tick();
    }

    public void event(Event event) {
        event(state, event);
    }

    public void event(State override, Event event) {
        executors.get(State.DEFAULT).event(event);
        executors.get(state).event(event);
    }

    public State getState() {
        return state;
    }

    public enum State {
        DEFAULT,
        PAUSE,
        PREPARING_FOR_GAME,
    }

}
