package solutions.brilliant.proceduralGeneration.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final Plugin plugin = Bukkit.getPluginManager().getPlugin("ProceduralGeneration");

    private static double version;

    public static void reload() {
        Config.version = plugin.getConfig().getDouble("version");
        ConfigurationSection gameSettingsSection = plugin.getConfig().getConfigurationSection("games-settings");

        GamesSettings.images = new ArrayList<>();
        for (String section : gameSettingsSection.getKeys(false)) {
            GamesSettings.images.add(getImage(
                    gameSettingsSection.getConfigurationSection(section)
            ));
        }
    }

    private static GamesSettings.Image getImage(ConfigurationSection section) {
        String name = section.getString("name");
        String world = section.getString("world");
        GamesSettings.Image.Generator generator = getImageGenerator(section);

        return new GamesSettings.Image(section.getName(), name, world, generator);
    }

    private static GamesSettings.Image.Generator getImageGenerator(ConfigurationSection imageSection) {
        ConfigurationSection section = imageSection.getConfigurationSection("generator");

        int initialNumberOfRooms = section.getInt("initial-number-of-rooms");
        int percentageOfAdditionalEdges = section.getInt("percentage-of-additional-edges");
        GamesSettings.Image.Generator.BuildingStyle buildingStyle = getImageGeneratorBuildingStyle(imageSection);

        return new GamesSettings.Image.Generator(
                initialNumberOfRooms,
                percentageOfAdditionalEdges,
                buildingStyle
        );
    }

    private static GamesSettings.Image.Generator.BuildingStyle getImageGeneratorBuildingStyle(ConfigurationSection imageSection) {
        ConfigurationSection section = imageSection.getConfigurationSection("generator")
                .getConfigurationSection("building-style");

        String floor = section.getString("floor");
        String baseboard = section.getString("baseboard");
        String wall = section.getString("wall");
        String ceiling = section.getString("ceiling");
        String light = section.getString("light");

        return new GamesSettings.Image.Generator.BuildingStyle(
                floor,
                baseboard,
                wall,
                ceiling,
                light
        );
    }

    public static double getVersion() {
        return version;
    }

    public static class GamesSettings {
        public static List<Image> images;

        public record Image(
                String id,
                String name,
                String world,
                GamesSettings.Image.Generator generator) {

            public record Generator(
                    int initialNumberOfRooms,
                    int percentageOfAdditionalEdges,
                    BuildingStyle buildingStyle) {

                public record BuildingStyle(
                        String floor,
                        String baseboard,
                        String wall,
                        String ceiling,
                        String light) {
                }
            }
        }

        public static void addImage(Image image) {
            GamesSettings.images.add(image);
            ConfigurationSection gamesSettings = plugin.getConfig().getConfigurationSection("games-settings");
            ConfigurationSection section = gamesSettings.createSection(image.id);
            section.set("name", image.name);
            section.set("world", image.world);
            ConfigurationSection generator = section.createSection("generator");
            generator.set("initial-number-of-rooms", image.generator.initialNumberOfRooms);
            generator.set("percentage-of-additional-edges", image.generator.percentageOfAdditionalEdges);
            ConfigurationSection buildingStyle = generator.createSection("building-style");
            buildingStyle.set("floor", image.generator.buildingStyle.floor);
            buildingStyle.set("baseboard", image.generator.buildingStyle.baseboard);
            buildingStyle.set("wall", image.generator.buildingStyle.wall);
            buildingStyle.set("ceiling", image.generator.buildingStyle.ceiling);
            buildingStyle.set("light", image.generator.buildingStyle.light);

            plugin.saveConfig();
        }

    }

}
