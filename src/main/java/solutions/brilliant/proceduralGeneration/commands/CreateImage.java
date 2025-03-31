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

import java.util.Random;
import java.util.logging.Level;

public class CreateImage {

    private final Plugin plugin;

    public CreateImage(Plugin plugin) {
        this.plugin = plugin;
    }

    public void executor(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            plugin.getLogger().log(Level.INFO, "Настройкой может заниматься только игрок, " +
                    "находясь непосредственно на сервере.");
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

        World world = createWorldForImage("image_" + strings[2]);
        player.teleport(
                new Location(world, 0, 50, 0)
        );
        player.setGameMode(GameMode.CREATIVE);

        player.sendMessage(
                Component.text("Готово!")
                        .color(TextColor.color(0x00ff00))
        );
    }

    private World createWorldForImage(String name) {
        WorldCreator creator = new WorldCreator(name);
        creator.type(WorldType.FLAT);
        creator.generator(new EmptyChunkGenerator());

        World world = Bukkit.createWorld(creator);
        new Location(world, 0, 50, 0).getBlock()
                .setType(Material.STONE);

        return world;
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
