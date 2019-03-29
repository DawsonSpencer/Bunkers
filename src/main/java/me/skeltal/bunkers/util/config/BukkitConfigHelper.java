package me.skeltal.bunkers.util.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class BukkitConfigHelper {

    /* File */
    private File file;
    
    /* Strings */
    private String name, directory;
    
    /* Configuration */
    private YamlConfiguration configuration;

    /**
     * Bukkit Configuration Class
     *
     * @param name - Is the identifier for the configuration file and object.
     * @param directory - Directory.
     */
    public BukkitConfigHelper(JavaPlugin plugin, String name, String directory){
        /* Set the Name */
        setName(name);
        /* Set the Directory */
        setDirectory(directory);
        /* Set File */
        file = new File(directory, name + ".yml");
        /* If file does not already exist, then grab it internally from the resources folder */
        if (!file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }
        /* Load the files configuration */
        this.configuration = YamlConfiguration.loadConfiguration(this.getFile());
    }

    /**
     * Saves the configuration file from memory to storage
     */
    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
