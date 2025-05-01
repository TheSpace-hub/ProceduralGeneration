package solutions.brilliant.proceduralGeneration;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import solutions.brilliant.proceduralGeneration.commands.BSGCommandHandler;
import solutions.brilliant.proceduralGeneration.commands.CreateEmptyWorld;
import solutions.brilliant.proceduralGeneration.config.CustomField;
import solutions.brilliant.proceduralGeneration.game.RuleExecutor;
import solutions.brilliant.proceduralGeneration.listeners.RulesEventHandler;

import java.util.logging.Level;

public final class ProceduralGeneration extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Plugin startup");

        saveDefaultConfig();
        CustomField.setup(this);

        getCommand("bs-generator").setExecutor(new BSGCommandHandler(this));
        getCommand("bs-generator").setTabCompleter(new BSGCommandHandler(this));

        getServer().getPluginManager().registerEvents(new RulesEventHandler(this), this);

        if (Bukkit.getWorld("field") == null)
            CreateEmptyWorld.create();

        if (CustomField.getAllFieldsFilesNames(this).isEmpty())
            getLogger().severe("Не существует ни одного поля!");

        RuleExecutor.getInstance(this).changeState(RuleExecutor.State.PAUSE);

        Bukkit.getScheduler().runTaskTimer(this, RuleExecutor.getInstance(this), 0, 1);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin disabled");
    }
}
