package solutions.brilliant.proceduralGeneration.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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
        switch (strings[0]) {
            case "generate" -> new GenerateField(plugin).executor(commandSender, command, s, strings);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 1 && commandSender.hasPermission("*")) {
            return List.of("generate");
        } else if (strings.length == 2 && strings[0].equals("generate") && commandSender.hasPermission("*")) {
            return CustomField.getAllFieldsFilesNames(plugin);
        }
        return List.of();
    }

}
