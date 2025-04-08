package solutions.brilliant.proceduralGeneration.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CustomField {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    private final String fileName;

    public CustomField(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать файл " + fileName);
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration get() {
        return config;
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить файл " + fileName);
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
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
