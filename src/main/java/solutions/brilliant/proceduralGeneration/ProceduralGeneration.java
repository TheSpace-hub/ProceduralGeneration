package solutions.brilliant.proceduralGeneration;

import org.bukkit.plugin.java.JavaPlugin;
import solutions.brilliant.proceduralGeneration.commands.BSGCommandHandler;

import java.util.logging.Level;

public final class ProceduralGeneration extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Plugin startup");
        saveDefaultConfig();
        getCommand("bs-generator").setExecutor(new BSGCommandHandler());
        getCommand("bs-generator").setTabCompleter(new BSGCommandHandler());
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Plugin disabled");
    }
}
