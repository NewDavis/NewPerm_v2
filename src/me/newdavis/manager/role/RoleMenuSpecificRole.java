package me.newdavis.manager.role;
//Plugin by NewDavis

import me.newdavis.manager.NewPermManager;
import me.newdavis.other.EnterValue;
import me.newdavis.other.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RoleMenuSpecificRole {

    public static void openPlayerMenu(Player p, String role) {
        EnterValue.removePlayerLists(p);
        Inventory inventory = Bukkit.createInventory(null, 9*5, "§7Role §8● §c" + role);

        for (int i = 0; i < 9*5; i++) {
            inventory.setItem(i, ItemBuilder.glass);
        }

        String prefix = NewPermManager.getRolePrefix(role) + p.getName();
        String suffix = NewPermManager.getRoleSuffix(role);
        Collection<String> inheritances = NewPermManager.getRoleInheritance(role);
        Collection<String> permissions = NewPermManager.getRolePermissions(role);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7" + role + " besitzt den §cPrefix §7" + prefix + "§7.");
        lore.add("§7" + role + " besitzt den §cSuffix §7" + suffix.replace("§", "&") + "§7.");
        lore.add("");
        if(!permissions.isEmpty()) {
            if(permissions.size() < 10) {
                lore.add("§7" + role + " besitzt folgende §cPermissions§7:");
                for (String perm : permissions) {
                    lore.add("§8- '§c" + perm + "§8'");
                }
            }else{
                lore.add("§7" + role + " besitzt §c" + permissions.size() + " Permissions§7.");
            }
        }else{
            lore.add("§cDiese Rolle besitzt keine Permissions!");
        }
        lore.add("");
        if(!inheritances.isEmpty()) {
            if(permissions.size() < 10) {
                lore.add("§7" + role + " besitzt folgende §cVererbungen§7:");
                for (String inheritance : inheritances) {
                    lore.add("§8- '§c" + inheritance + "§8'");
                }
            }else{
                lore.add("§7" + role + " besitzt §c" + inheritances.size() + " Vererbungen§7.");
            }
        }else{
            lore.add("§cDiese Rolle besitzt keine Vererbungen!");
        }

        ItemStack backToMenu = ItemBuilder.getCustomSkull(ItemBuilder.lightGrayArrowLeftLink, "§7Zurück zum §cMenü§7.", new ArrayList<>());
        ItemStack roleHead = new ItemBuilder(Material.BOOK).setName("§7Übersicht der §cRolle " + role).setLore(lore).build();
        ItemStack rolePermHead = ItemBuilder.getCustomSkull(ItemBuilder.redAttentionLink, "§cPermissions §7ändern", Arrays.asList("", "§aSetze §7oder §cLösche Permissions§7 der §cRolle " + role + "§7."));
        ItemStack roleInheritanceHead = ItemBuilder.getCustomSkull(ItemBuilder.redLetterRLink, "§cInheritances §7ändern", Arrays.asList("", "§7Ändere die §cVererbungen §7der §cRolle " + role + "§7."));
        ItemStack rolePrefix = new ItemBuilder(Material.NAME_TAG).setName("§cPrefix §7ändern").setLore(Arrays.asList("", "§7Ändere den §cPrefix §7der §cRolle " + role + "§7.")).build();
        ItemStack roleSuffix = new ItemBuilder(Material.NAME_TAG).setName("§cSuffix §7ändern").setLore(Arrays.asList("", "§7Ändere den §cSuffix §7der §cRolle " + role + "§7.")).build();

        inventory.setItem(11, rolePermHead);
        inventory.setItem(15, roleInheritanceHead);
        inventory.setItem(22, roleHead);
        inventory.setItem(29, rolePrefix);
        inventory.setItem(33, roleSuffix);
        inventory.setItem(36, backToMenu);

        p.openInventory(inventory);
        RoleMenu.roleMenu.put(p, role);
    }

}
