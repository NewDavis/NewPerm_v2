package me.newdavis.listener;
//Plugin by NewDavis

import me.newdavis.other.EnterValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class OnInventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(p.getOpenInventory().getTitle().contains(" ")) {
            String[] invTitle = p.getOpenInventory().getTitle().split(" ");
            if(invTitle.length > 2) {
                if (!(invTitle[2].contains("§c"))) {
                    EnterValue.removePlayerLists(p);
                    EnterValue.removeRoleLists(p);
                }
            }
        }
    }

}
