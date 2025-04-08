package solutions.brilliant.proceduralGeneration.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CustomField {
    private final Plugin plugin;
    private FileConfiguration config;
    private final String fileName;

    public CustomField(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName + ".yml";

        setup();
    }

    private void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File configFile = new File(new File(plugin.getDataFolder(), "fields"), fileName);

        if (!configFile.exists()) {
            plugin.getLogger().severe("Не существует поля с названием " + fileName);
            return;
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public List<List<Integer>> getFiled() {
        return (List<List<Integer>>) config.getList("field");
    }

    public static List<String> getAllFieldsFilesNames(Plugin plugin) {
        File fieldsDir = new File(plugin.getDataFolder(), "fields");
        if (!fieldsDir.exists())
            fieldsDir.mkdir();

        try {
            List<Path> files = Files.list(fieldsDir.toPath()).toList();

            List<String> filesNames = new ArrayList<>();
            for (Path file : files) {
                String name = file.getFileName().toString();
                if (name.split("\\.")[1].equals("yml"))
                    filesNames.add(name.split("\\.")[0]);
            }

            return filesNames;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
