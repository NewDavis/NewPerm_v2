package me.newdavis.file;

import me.newdavis.command.NewPermCmd;
import me.newdavis.plugin.NewPerm;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SettingsFile {

    private static final File file = new File("plugins/NewPerm/Settings.yml");
    private static final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

    public static void loadConfig() {
        if(file.exists()) {
            try {
                yaml.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            NewPerm.prefix = yaml.getString("Prefix");
            NewPerm.noPerm = yaml.getString("NoPerm").replace("{Prefix}", NewPerm.prefix);
            NewPerm.error = yaml.getString("Error").replace("{Prefix}", NewPerm.prefix);
            NewPerm.usage = yaml.getString("Message.Usage").replace("{Prefix}", NewPerm.prefix);
            NewPerm.usagePlayer = yaml.getString("Message.UsagePlayer").replace("{Prefix}", NewPerm.prefix);
            NewPerm.usageRole = yaml.getString("Message.UsageRole").replace("{Prefix}", NewPerm.prefix);
            NewPermCmd.permRole = yaml.getString("Permission.Use");
            NewPermCmd.permPlayer = yaml.getString("Permission.Player");
            NewPermCmd.permRole = yaml.getString("Permission.Role");
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
            yaml.set("Prefix", "§8» §4§lN§c§lew§4§lP§c§lerm §8┃§7");
            yaml.set("NoPerm", "{Prefix} §cDazu hast du keine Rechte!");
            yaml.set("Error", "{Prefix} §cEs ist ein Fehler augetreten!");
            yaml.set("Message.Usage", "{Prefix} §8/§cNewPerm §8<§7Spieler§8/§7Rolle§8> §8<§7Spieler§8/§7Rolle§8> §8<§7Info§8>");
            yaml.set("Message.UsagePlayer", "{Prefix} §8/§cNewPerm §8<§7Spieler§8> <§7Spieler§8> <§7Info§8/§7Prefix§8/§7Suffix§8/§7Permission§8/§7Rolle§8>");
            yaml.set("Message.UsageRole", "{Prefix} §8/§cNewPerm §8<§7Rolle§8> <§7Rolle§8/§7List§8> <§7Info§8/§7Prefix§8/§7Suffix§8/§7Inheritance§8/§7Permission§8>");
            yaml.set("Permission.Use", "newperm.use");
            yaml.set("Permission.Role", "newperm.role");
            yaml.set("Permission.Player", "newperm.player");
            yaml.set("MySQL.Enabled", false);
            yaml.set("MySQL.useSSL", false);
            yaml.set("MySQL.host", "localhost");
            yaml.set("MySQL.port", 3306);
            yaml.set("MySQL.database", "newperm");
            yaml.set("MySQL.user", "root");
            yaml.set("MySQL.password", "");

            NewPerm.prefix = yaml.getString("Prefix");
            NewPerm.noPerm = yaml.getString("NoPerm").replace("{Prefix}", NewPerm.prefix);
            NewPerm.error = yaml.getString("Error").replace("{Prefix}", NewPerm.prefix);
            NewPerm.usage = yaml.getString("Message.Usage").replace("{Prefix}", NewPerm.prefix);
            NewPerm.usagePlayer = yaml.getString("Message.UsagePlayer").replace("{Prefix}", NewPerm.prefix);
            NewPerm.usageRole = yaml.getString("Message.UsageRole").replace("{Prefix}", NewPerm.prefix);
            NewPermCmd.permRole = yaml.getString("Permission.Use");
            NewPermCmd.permPlayer = yaml.getString("Permission.Player");
            NewPermCmd.permRole = yaml.getString("Permission.Role");
            try {
                yaml.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getMySQLEnabled() {
        return yaml.getBoolean("MySQL.Enabled");
    }
    public static boolean getMySQLUseSSL() {
        return yaml.getBoolean("MySQL.useSSL");
    }

    public static String getMySQLHost() {
        return yaml.getString("MySQL.host");
    }

    public static int getMySQLPort() {
        return yaml.getInt("MySQL.port");
    }

    public static String getMySQLDatabase() {
        return yaml.getString("MySQL.database");
    }

    public static String getMySQLUser() {
        return yaml.getString("MySQL.user");
    }

    public static String getMySQLPassword() {
        return yaml.getString("MySQL.password");
    }

}
