package solutions.brilliant.proceduralGeneration.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class GenerateField {

    private final Plugin plugin;

    public GenerateField(Plugin plugin) {
        this.plugin = plugin;
    }

    public void executor(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            FileConfiguration configuration = plugin.getConfig();
            if (strings.length != 2) {
                plugin.getLogger().log(Level.WARNING, "Ты команду ввёл не правильно >:(");
                return;
            }
            List<List<Integer>> field = (List<List<Integer>>) configuration.getList("fields").get(
                    Integer.valueOf(strings[1])
            );

            World world = Bukkit.getWorld("field");
            for (int y = 0; y < field.size(); y++) {
                for (int x = 0; x < field.get(y).size(); x++) {
                    Map<Integer, Material> materials = Map.of(
                            1, Material.POLISHED_ANDESITE,
                            2, Material.STONE_BRICKS,
                            0, Material.AIR
                    );
                    int type = field.get(y).get(x);
                    new Location(world, x, 0, y).getBlock().setType(
                            materials.get(type)
                    );
                    if (type == 2) {
                        for (int z = 0; z < 5; z++) {
                            new Location(world, x, z, y).getBlock().setType(
                                    materials.get(type)
                            );
                        }
                    }
                }
            }
        }
    }


}
