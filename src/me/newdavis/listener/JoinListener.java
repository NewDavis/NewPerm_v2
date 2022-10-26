package me.newdavis.listener;

import me.newdavis.manager.NewPermManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        NewPermManager.checkIfMigrated(p);
        NewPermManager.grantAll(p);
    }

}
