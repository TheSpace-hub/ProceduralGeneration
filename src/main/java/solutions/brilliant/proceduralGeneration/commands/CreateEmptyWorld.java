package solutions.brilliant.proceduralGeneration.commands;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CreateEmptyWorld {

    private final Plugin plugin;

    public CreateEmptyWorld(Plugin plugin) {
        this.plugin = plugin;
    }

    public static void create() {
        WorldCreator creator = new WorldCreator("field");
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
