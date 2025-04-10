package solutions.brilliant.proceduralGeneration.game.rules;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.commands.GenerateField;
import solutions.brilliant.proceduralGeneration.config.CustomField;
import solutions.brilliant.proceduralGeneration.game.Rule;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;

import java.util.logging.Level;

public class RulePreparingForGame implements Rule {

    private final Plugin plugin;

    private int countdown;
    private final int delay = 20;

    public RulePreparingForGame(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enter() {
        GenerateField.getInstance(plugin).generate(
                CustomField.getAllFieldsFilesNames(plugin).get(0)
        );
        countdown = delay * 20;
    }

    @Override
    public void event(Event event) {

    }

    @Override
    public void tick() {
        countdown--;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(countdown / 20);
            player.setExp((float) countdown / (delay * 20));
        }
        if (countdown == 0) {
            RuleExecutor.getInstance(plugin).changeState(RuleExecutor.State.PREPARATORY);
        }
    }
}
