package me.newdavis.manager.role;
//Plugin by NewDavis

import me.newdavis.manager.NewPermManager;
import me.newdavis.other.EnterValue;
import me.newdavis.other.ItemBuilder;
import me.newdavis.plugin.NewPerm;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RolePermissionMenu {

    public static HashMap<Player, Integer> rolePermissionPage = new HashMap<>();

    public static void openPermissionMenu(Player p, String role, int page) {
        EnterValue.removePlayerLists(p);
        if(page != 0) {
            Collection<String> permissions = NewPermManager.getRolePermissions(role);
            int permSize = permissions.size();
            double pagesDouble = (double) permSize / 21;
            String pagesString = String.valueOf(pagesDouble);
            if (pagesString.contains(".")) {
                pagesDouble = pagesDouble + 1;
            }

            int pages = (int) pagesDouble;

            if (pages >= page) {
                Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§7" + role + " §8● §cPerm");

                List<Integer> glassSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 27, 35, 37, 38, 42, 43, 44);
                for (int slot : glassSlots) {
                    inventory.setItem(slot, ItemBuilder.glass);
                }

                ItemStack greenPlus = ItemBuilder.getCustomSkull(ItemBuilder.greenPlus, "§7Setze eine neue §cPermission§7.", new ArrayList<>());
                inventory.setItem(26, greenPlus);
                ItemStack backToMenu = ItemBuilder.getCustomSkull(ItemBuilder.lightGrayArrowLeftLink, "§7Zurück zum §cMenü§7.", new ArrayList<>());
                inventory.setItem(36, backToMenu);
                ItemStack leftArrow = ItemBuilder.getCustomSkull(ItemBuilder.redArrowLeftLink, "§7Vorherige Seite §8(§a" + (page == 1 ? "1" : page - 1) + "§8/§a" + pages + "§8)", new ArrayList<>());
                inventory.setItem(39, leftArrow);
                ItemStack rightArrow = ItemBuilder.getCustomSkull(ItemBuilder.redArrowRightLink, "§7Nächste Seite §8(§a" + (page == pages ? pages : page + 1) + "§8/§a" + pages + "§8)", new ArrayList<>());
                inventory.setItem(41, rightArrow);
                ItemStack currentPage = new ItemBuilder(Material.COMPASS, page).setName("§7Aktuelle Seite: §a" + page + "§8/§a" + pages).build();
                inventory.setItem(40, currentPage);

                List<String> permissionsList = (List<String>) permissions;
                if (permissionsList.size() < 22) {
                    for (int i = 0; i < 22; i++) {
                        if (permissionsList.size() > i) {
                            ItemStack permItem = new ItemBuilder(Material.PAPER).setName("§c" + permissionsList.get(i)).setLore(Collections.singletonList("§7Klicke, um die §cPermission §7zu entfernen.")).build();
                            inventory.addItem(permItem);
                        }
                    }
                } else {
                    if (page == 1) {
                        for (int i = 0; i < 22; i++) {
                            if (permissionsList.size() > i) {
                                ItemStack permItem = new ItemBuilder(Material.PAPER).setName("§c" + permissionsList.get(i)).setLore(Collections.singletonList("§7Klicke, um die §cPermission §7zu entfernen.")).build();
                                inventory.addItem(permItem);
                            }
                        }
                    } else {
                        for (int i = ((page - 1) * 21); i < (((page - 1) * 21) + 22); i++) {
                            if (permissionsList.size() > i) {
                                ItemStack permItem = new ItemBuilder(Material.PAPER).setName("§c" + permissionsList.get(i)).setLore(Collections.singletonList("§7Klicke, um die §cPermission §7zu entfernen.")).build();
                                inventory.addItem(permItem);
                            }
                        }
                    }
                }

                p.openInventory(inventory);
                rolePermissionPage.put(p, page);
            } else {
                p.sendMessage(NewPerm.prefix + " §cDie folgende Seite existiert nicht!");
            }
        } else {
            p.sendMessage(NewPerm.prefix + " §cDie folgende Seite existiert nicht!");
        }
    }

}
