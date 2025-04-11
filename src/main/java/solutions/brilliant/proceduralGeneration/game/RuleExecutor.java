package solutions.brilliant.proceduralGeneration.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.game.rules.DefaultRules;
import solutions.brilliant.proceduralGeneration.game.rules.RuleGame;
import solutions.brilliant.proceduralGeneration.game.rules.RulePause;
import solutions.brilliant.proceduralGeneration.game.rules.RulePreparingForGame;

import java.util.HashMap;
import java.util.Map;

public class RuleExecutor implements Runnable {
    private static RuleExecutor instance;

    private final Plugin plugin;
    private State state = State.PAUSE;

    private final Map<State, Rule> executors;
    private final Map<Player, Role> roles;

    private RuleExecutor(Plugin plugin) {
        this.plugin = plugin;

        executors = Map.of(
                State.DEFAULT, new DefaultRules(plugin),
                State.PAUSE, new RulePause(plugin),
                State.PREPARING_FOR_GAME, new RulePreparingForGame(plugin),
                State.GAME, new RuleGame(plugin)
        );

        roles = new HashMap<>();
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

    @Override
    public void run() {
        tick();
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
        GAME,
    }

    public void setPlayerRole(Player player, Role role) {
        roles.put(player, role);
    }

    public Role getPlayerRole(Player player) {
        return roles.get(player);
    }

}
