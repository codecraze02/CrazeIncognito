package pl.codecraze.incognito.configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.codecraze.incognito.configurations.configuration.ConfigConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * @author CodeCraze (ComplexHub.pl) on 16.06.2025
 **/
public class ConfigurationManager {

    private final File file;
    private final Gson gson;
    private ConfigConfiguration config;

    public ConfigurationManager(File dataFolder) {
        this.file = new File(dataFolder, "config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void loadConfig() {
        try {
            if (!file.exists()) {
                saveDefaultConfig();
            }
            try (FileReader reader = new FileReader(file)) {
                this.config = gson.fromJson(reader, ConfigConfiguration.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() throws IOException {
        ConfigConfiguration defaultConfig = new ConfigConfiguration();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(defaultConfig, writer);
        }
    }

    public ConfigConfiguration getConfig() {
        return config;
    }
}
