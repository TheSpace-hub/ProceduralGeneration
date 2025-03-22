package solutions.brilliant.proceduralGeneration.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BSGCommandHandler implements CommandExecutor, TabCompleter {

    private final Plugin plugin;

    public BSGCommandHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        switch (strings[0]) {
            case "create" -> {
                switch (strings[1]) {
                    case "image" -> new CreateImage(plugin).executor(commandSender, command, s, strings);
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        switch (strings.length) {
            case 1:
                return List.of("create", "configure", "list", "join", "join-gui");
            case 2: {
                switch (strings[0]) {
                    case "create", "configure", "list":
                        return List.of("image");
                }
            }
        }
        return List.of();
    }

}
