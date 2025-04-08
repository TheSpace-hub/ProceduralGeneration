package solutions.brilliant.proceduralGeneration.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import solutions.brilliant.proceduralGeneration.config.CustomField;

import java.util.List;

public class BSGCommandHandler implements CommandExecutor, TabCompleter {

    private final Plugin plugin;

    public BSGCommandHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player)
            return false;

        switch (strings[0]) {
            case "generate" -> new GenerateField(plugin).executor(commandSender, command, s, strings);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player)
            return List.of();

        if (strings.length == 1) {
            return List.of("generate");
        } else if (strings.length == 2 && strings[0].equals("generate")) {
            return CustomField.getAllFieldsFilesNames(plugin);
        }
        return List.of();
    }

}
