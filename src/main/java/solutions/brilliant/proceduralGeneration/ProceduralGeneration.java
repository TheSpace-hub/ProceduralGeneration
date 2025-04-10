package solutions.brilliant.proceduralGeneration;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import solutions.brilliant.proceduralGeneration.commands.BSGCommandHandler;
import solutions.brilliant.proceduralGeneration.commands.CreateEmptyWorld;
import solutions.brilliant.proceduralGeneration.config.CustomField;
import solutions.brilliant.proceduralGeneration.listeners.PlayerHandler;

import java.util.logging.Level;

public final class ProceduralGeneration extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Plugin startup");

        saveDefaultConfig();
        CustomField.setup(this);

        getCommand("bs-generator").setExecutor(new BSGCommandHandler(this));
        getCommand("bs-generator").setTabCompleter(new BSGCommandHandler(this));

        getServer().getPluginManager().registerEvents(new PlayerHandler(this), this);

        if (Bukkit.getWorld("field") == null)
            CreateEmptyWorld.create();
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin disabled");
    }
}
