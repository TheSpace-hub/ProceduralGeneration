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
        GamesSettings.Image.Generator generator = getImageGenerator(section);

        return new GamesSettings.Image(name, generator);
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
                String name,
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
    }

}
