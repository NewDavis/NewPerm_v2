package me.newdavis.plugin;

import me.newdavis.command.NewPermCmd;
import me.newdavis.file.SettingsFile;
import me.newdavis.listener.ChatListener;
import me.newdavis.listener.JoinListener;
import me.newdavis.listener.OnInventoryClickListener;
import me.newdavis.listener.OnInventoryCloseListener;
import me.newdavis.file.PermissionsFile;
import me.newdavis.manager.NewPermManager;
import me.newdavis.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NewPerm extends JavaPlugin {

    public static String prefix, noPerm, usage, usagePlayer, usageRole, error;
    private static MySQL mySQL;
    private static NewPerm instance;
    private static String pluginVersion;

    @Override
    public void onLoad() {
        //set Instance
        instance = this;
    }

    @Override
    public void onEnable() {
        pluginVersion = getDescription().getVersion();

        //loadAll
        loadFiles();

        //MySQL
        if(SettingsFile.getMySQLEnabled()) {
            String host = SettingsFile.getMySQLHost();
            int port = SettingsFile.getMySQLPort();
            String database = SettingsFile.getMySQLDatabase();
            String user = SettingsFile.getMySQLUser();
            String password = SettingsFile.getMySQLPassword();
            mySQL = new MySQL(host, port, database, user, password);
            if(!mySQL.connect()) {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(NewPerm.prefix + " §cIt couldn't create a connection to the MySQL database!");
                Bukkit.getPluginManager().disablePlugin(instance);
                return;
            }else {
                Bukkit.getConsoleSender().sendMessage("");
                Bukkit.getConsoleSender().sendMessage(NewPerm.prefix + " §7Connected to §f§lMySQL database §7successfully!");
                mySQL.createTables();
            }
        }

        loadCommandsAndListener();

        //check for Updates
        String newestVersion = updateChecker();
        if(newestVersion != null) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(prefix + " §cThere is a newer version of NewPerm! §8(§4" + newestVersion + "§8)");
        }

        //Start Message
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(prefix + " §aPlugin was §astarted successfully§7! §8(§b" + pluginVersion + "§8)");
        Bukkit.getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {
        //Stop Message
        Bukkit.getConsoleSender().sendMessage(prefix + " §aPlugin was §cstopped §asuccessfully§7! §8(§b" + pluginVersion + "§8)");
        Bukkit.getConsoleSender().sendMessage("");
    }

    public static NewPerm getInstance() {
        return instance;
    }

    public static void loadCommandsAndListener() {
        //Register Command NewPerm
        instance.getCommand("newperm").setExecutor(new NewPermCmd());

        //Register Events
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), instance);
        pm.registerEvents(new OnInventoryClickListener(), instance);
        pm.registerEvents(new OnInventoryCloseListener(), instance);
        pm.registerEvents(new ChatListener(), instance);

        //grantAll to everyone
        for(Player all : Bukkit.getOnlinePlayers()) {
            NewPermManager.grantAll(all);
        }
    }

    public static void loadFiles() {
        PermissionsFile.loadConfig();
        SettingsFile.loadConfig();
    }

    public String updateChecker() {
        try {
            URLConnection url = new URL("https://newdavis.me/plugin/update/index.php?plugin=newperm").openConnection();
            url.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            url.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
            String newestVersion = br.readLine();
            if(!pluginVersion.equalsIgnoreCase(newestVersion)) {
                return newestVersion;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static MySQL getMySQL() {
        return mySQL;
    }
}
