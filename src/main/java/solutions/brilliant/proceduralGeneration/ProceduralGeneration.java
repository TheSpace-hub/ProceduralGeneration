package solutions.brilliant.proceduralGeneration;

import org.bukkit.plugin.java.JavaPlugin;
import solutions.brilliant.proceduralGeneration.commands.BSGCommandHandler;
import solutions.brilliant.proceduralGeneration.listeners.BlockHandler;
import solutions.brilliant.proceduralGeneration.listeners.PlayerHandler;

import java.util.logging.Level;

public final class ProceduralGeneration extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Plugin startup");

        getCommand("bs-generator").setExecutor(new BSGCommandHandler(this));
        getCommand("bs-generator").setTabCompleter(new BSGCommandHandler(this));

        getServer().getPluginManager().registerEvents(new BlockHandler(this), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin disabled");
    }
}
