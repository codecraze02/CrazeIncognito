package pl.codecraze.incognito.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import pl.codecraze.incognito.commands.IncognitoCommand;
import pl.codecraze.incognito.commands.ReloadConfigCommand;
import pl.codecraze.incognito.configurations.ConfigurationManager;
import pl.codecraze.incognito.configurations.configuration.ConfigConfiguration;
import pl.codecraze.incognito.listeners.PlayerJoinListener;
import pl.codecraze.incognito.listeners.PlayerQuitListener;
import pl.codecraze.incognito.nametag.NameTagManager;
import pl.codecraze.incognito.nametag.NameTagUpdateTask;
import pl.codecraze.incognito.user.IncognitoUserManager;

public final class IncognitoPlugin extends JavaPlugin {

    private static IncognitoPlugin instance;


    private ConfigurationManager configurationManager;
    private ConfigConfiguration configConfiguration;

    private IncognitoUserManager userManager;
    private NameTagManager nameTagManager;

    @Override
    public void onEnable() {
        this.configurationManager = new ConfigurationManager(getDataFolder());
        configurationManager.loadConfig();


        this.userManager = new IncognitoUserManager();

        this.nameTagManager = new NameTagManager(this);

        new PlayerJoinListener(this);
        new PlayerQuitListener(this);

        new NameTagUpdateTask(this);
        this.getCommand("incognito").setExecutor(new IncognitoCommand(this));
        this.getCommand("reloadincognito").setExecutor(new ReloadConfigCommand(this));


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static IncognitoPlugin getInstance() {
        return instance;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public ConfigConfiguration getConfigConfiguration() {
        return configConfiguration;
    }



    public IncognitoUserManager getUserManager() {
        return userManager;
    }

    public NameTagManager getNameTagManager() {
        return nameTagManager;
    }
}
