package solutions.brilliant.proceduralGeneration.game.rules;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.commands.GenerateField;
import solutions.brilliant.proceduralGeneration.config.CustomField;
import solutions.brilliant.proceduralGeneration.game.Rule;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;

public class RulePreparingForGame implements Rule {

    private final Plugin plugin;

    public RulePreparingForGame(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enter() {
        GenerateField.getInstance(plugin).generate(
                CustomField.getAllFieldsFilesNames(plugin).get(0)
        );
    }

    @Override
    public void event(Event event) {
        RuleExecutor.getInstance(plugin).event(RuleExecutor.State.PAUSE, event);
    }

    @Override
    public void tick() {

    }
}
