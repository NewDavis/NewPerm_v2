package me.newdavis.manager.player;
//Plugin by NewDavis

import me.newdavis.manager.NewPermManager;
import me.newdavis.other.EnterValue;
import me.newdavis.other.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerMenu {

    public static HashMap<Player, OfflinePlayer> playerMenu = new HashMap<>();

    public static void openPlayerMenu(Player p, OfflinePlayer t) {
        EnterValue.removeRoleLists(p);
        Inventory inventory = Bukkit.createInventory(null, 9*5, "§7" + t.getName() + " §8● §cMenü");

        for (int i = 0; i < 9*5; i++) {
            inventory.setItem(i, ItemBuilder.glass);
        }

        String prefix = NewPermManager.getPlayerPrefix(t) + t.getName();
        String suffix = NewPermManager.getPlayerSuffix(t);
        String role = NewPermManager.getPlayerRole(t);
        Collection<String> permissions = NewPermManager.getPlayerPermissions(t);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7" + t.getName() + " ist in der Rolle §c" + role + "§7.");
        lore.add("");
        lore.add("§7" + t.getName() + " besitzt den §cPrefix §7" + prefix + "§7.");
        lore.add("§7" + t.getName() + " besitzt den §cSuffix §7" + suffix.replace("§", "&") + "§7.");
        lore.add("");
        if(!permissions.isEmpty()) {
            if(permissions.size() < 10) {
                lore.add("§7" + t.getName() + " besitzt folgende §cPermissions§7:");
                for (String perm : permissions) {
                    lore.add("§8- '§c" + perm + "§8'");
                }
            }else{
                lore.add("§7" + t.getName() + " besitzt §c" + permissions.size() + " Permissions§7.");
            }
        }else{
            lore.add("§cDieser Spieler besitzt keine Permissions!");
        }

        ItemStack playerHead = ItemBuilder.getSkull("§7Übersicht von " + prefix, lore, t.getName());
        ItemStack permHead = ItemBuilder.getCustomSkull(ItemBuilder.redAttentionLink, "§cPermissions §7ändern", Arrays.asList("", "§aSetze §7oder §cLösche Permissions§7 von " + prefix + "§7."));
        ItemStack roleHead = ItemBuilder.getCustomSkull(ItemBuilder.redLetterRLink, "§cRolle §7ändern", Arrays.asList("", "§7Ändere die §cRolle §7von " + prefix + "§7."));
        ItemStack playerPrefix = new ItemBuilder(Material.NAME_TAG).setName("§cPrefix §7ändern").setLore(Arrays.asList("", "§7Ändere den §cPrefix §7von §c" + t.getName())).build();
        ItemStack playerSuffix = new ItemBuilder(Material.NAME_TAG).setName("§cSuffix §7ändern").setLore(Arrays.asList("", "§7Ändere den §cSuffix §7von §c" + t.getName())).build();

        inventory.setItem(11, permHead);
        inventory.setItem(15, roleHead);
        inventory.setItem(22, playerHead);
        inventory.setItem(29, playerPrefix);
        inventory.setItem(33, playerSuffix);

        p.openInventory(inventory);
        playerMenu.put(p, t);
    }

}