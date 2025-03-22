package solutions.brilliant.proceduralGeneration.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import solutions.brilliant.proceduralGeneration.config.Config;

import java.util.Random;
import java.util.logging.Level;

public class CreateImage {

    private final Plugin plugin;

    public CreateImage(Plugin plugin) {
        this.plugin = plugin;
    }

    public void executor(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (!player.hasPermission("bsg.admin")) {
                plugin.getLogger().log(Level.INFO, "У " + player.getName() + " недостаточно прав для создания образа.");
                player.sendMessage(
                        Component.text("У вас недостаточно прав для выполнения этой команды.")
                                .color(TextColor.color(0xff0000))
                );
                return;
            }
            if (strings.length != 3) {
                player.sendMessage(
                        Component.textOfChildren(
                                Component.text("Вы неправильно ввели команду! Необходимо: ")
                                        .color(TextColor.color(0xff0000)),
                                Component.text("/bsg create image ")
                                        .color(TextColor.color(0xff0000))
                                        .decorate(TextDecoration.ITALIC),
                                Component.text("имя-образа")
                                        .color(TextColor.color(0xff0000))
                                        .decorate(TextDecoration.ITALIC)
                                        .decorate(TextDecoration.UNDERLINED)
                        )
                );
                return;
            }

            plugin.getLogger().log(Level.INFO, player.getName() + " создал новый образ.");
            player.sendMessage(
                    Component.text("Готовим мир для образа...")
                            .color(TextColor.color(0x00ff00))
            );

            Config.GamesSettings.Image image = createImage(strings[2]);
            World world = Bukkit.getWorld(image.world());
            player.teleport(
                    new Location(world, 0, 50, 0)
            );
            player.setGameMode(GameMode.CREATIVE);

            player.sendMessage(
                    Component.text("Готово! Теперь вы можете заняться настройкой мира.")
                            .color(TextColor.color(0x00ff00))
            );
        } else {
            plugin.getLogger().log(Level.INFO, "Настройкой может заниматься только игрок, " +
                    "находясь непосредственно на сервере.");
        }
    }

    private Config.GamesSettings.Image createImage(String id) {
        Config.GamesSettings.Image image = new Config.GamesSettings.Image(
                id, id, "container_" + id,
                new Config.GamesSettings.Image.Generator(
                        70, 20,
                        new Config.GamesSettings.Image.Generator.BuildingStyle(
                                "birch_planks",
                                "acacia_planks",
                                "oak_planks",
                                "spruce_planks",
                                "glowstone"
                        )
                )
        );
        Config.GamesSettings.addImage(image);
        createWorldForImage(image.world());

        return image;
    }

    private void createWorldForImage(String name) {
        WorldCreator creator = new WorldCreator(name);
        creator.type(WorldType.FLAT);
        creator.generator(new EmptyChunkGenerator());

        World world = Bukkit.createWorld(creator);
        new Location(world, 0, 50, 0).getBlock()
                .setType(Material.STONE);
    }

    private static class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {

        }

        @Override
        public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {

        }

        @Override
        public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {

        }
    }

}
