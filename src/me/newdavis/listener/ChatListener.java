package me.newdavis.listener;
//Plugin by NewDavis

import me.newdavis.other.EnterValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage().replace("&", "§");

        if(EnterValue.hasToEnterValue(p)) {
            e.setCancelled(true);
            EnterValue.setValue(p, msg);
        }
    }

}
