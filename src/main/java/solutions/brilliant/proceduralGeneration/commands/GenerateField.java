package solutions.brilliant.proceduralGeneration.commands;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import solutions.brilliant.proceduralGeneration.config.CustomField;

import java.util.List;
import java.util.Map;

public class GenerateField {
    private static GenerateField instance;

    private final Plugin plugin;

    private GenerateField(Plugin plugin) {
        this.plugin = plugin;
    }

    public static GenerateField getInstance(Plugin plugin) {
        if (instance == null) {
            instance = new GenerateField(plugin);
        }
        return instance;
    }

    public void generate(String name) {
        clear();
        List<List<Integer>> field = new CustomField(plugin, name).getFiled();
        fill(field);
    }

    private void clear() {
        World world = Bukkit.getWorld("field");
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                for (int z = 0; z < 5; z++) {
                    new Location(world, x, z, y).getBlock().setType(
                            Material.AIR
                    );
                }
            }
        }
    }

    private void fill(List<List<Integer>> field) {
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
