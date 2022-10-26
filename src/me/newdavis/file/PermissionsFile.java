package me.newdavis.file;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PermissionsFile {

    private static final File file = new File("plugins/NewPerm/Permissions.yml");
    private static final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

    public static void loadConfig() {
        if(file.exists()) {
            try {
                yaml.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }else{
            saveConfig();
        }
    }

    public static void saveConfig() {
        if(file.exists()) {
            try {
                yaml.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            yaml.set("Role.Administrator.settings.prefix", "§4Administrator §8┃§4 ");
            yaml.set("Role.Administrator.settings.suffix", "§4");
            yaml.set("Role.Administrator.permissions", Arrays.asList("*", "NewPerm.*"));
            yaml.set("Role.VIP.settings.prefix", "§5VIP §8┃§5 ");
            yaml.set("Role.VIP.settings.suffix", "§5");
            yaml.set("Role.VIP.permissions", Collections.singletonList("system.vanish"));
            yaml.set("Role.VIP.inheritance", Collections.singletonList("Spieler"));
            yaml.set("Role.Spieler.settings.prefix", "§7Spieler §8┃§7 ");
            yaml.set("Role.Spieler.settings.suffix", "§7");
            yaml.set("Role.Spieler.settings.default", true);
            yaml.set("Role.Spieler.permissions", Arrays.asList("system.fly", "system.kit.NewSystem"));

            try {
                yaml.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setPath(String path, Object value) {
        yaml.set(path, value);
        saveConfig();
    }

    public static boolean isPathSet(String path) {
        return yaml.isSet(path);
    }

    public static String getString(String path) {
        String value = "";
        if(isPathSet(path)) {
            value = yaml.getString(path);
        }
        return value;
    }

    public static boolean getBoolean(String path) {
        boolean value = false;
        if(isPathSet(path)) {
            value = yaml.getBoolean(path);
        }
        return value;
    }

    public static Collection<String> getConfigurationSection(String path) {
        Collection<String> keys = new ArrayList<>();
        if(isPathSet(path)) {
            try {
                keys = yaml.getConfigurationSection(path).getKeys(false);
            }catch (ConcurrentModificationException ignored) {}
        }
        return keys;
    }

    public static List<String> getStringList(String path) {
        if(isPathSet(path)) {
            return yaml.getStringList(path);
        }
        return new ArrayList<>();
    }

}
