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

public class RoleMenu {

    public static HashMap<Player, String> roleMenu = new HashMap<>();
    public static HashMap<Player, Integer> roleMenuPage = new HashMap<>();

    public static void openRoleMenu(Player p, int page) {
        EnterValue.removePlayerLists(p);
        if(page != 0) {
            Collection<String> roles = NewPermManager.getRoleList();
            int rolesSize = roles.size();
            double pagesDouble = (double) rolesSize / 21;
            String pagesString = String.valueOf(pagesDouble);
            if (pagesString.contains(".")) {
                pagesDouble = pagesDouble + 1;
            }

            int pages = (int) pagesDouble;

            if (pages >= page) {
                Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§7Role §8● §cMenü");

                List<Integer> glassSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 27, 35, 36, 37, 38, 42, 43, 44);
                for (int slot : glassSlots) {
                    inventory.setItem(slot, ItemBuilder.glass);
                }

                ItemStack greenPlus = ItemBuilder.getCustomSkull(ItemBuilder.greenPlus, "§7Füge eine neue §cRolle §7hinzu.", new ArrayList<>());
                inventory.setItem(26, greenPlus);
                ItemStack leftArrow = ItemBuilder.getCustomSkull(ItemBuilder.redArrowLeftLink, "§7Vorherige Seite §8(§a" + (page == 1 ? "1" : page - 1) + "§8/§a" + pages + "§8)", new ArrayList<>());
                inventory.setItem(39, leftArrow);
                ItemStack rightArrow = ItemBuilder.getCustomSkull(ItemBuilder.redArrowRightLink, "§7Nächste Seite §8(§a" + (page == pages ? pages : page + 1) + "§8/§a" + pages + "§8)", new ArrayList<>());
                inventory.setItem(41, rightArrow);
                ItemStack currentPage = new ItemBuilder(Material.COMPASS, page).setName("§7Aktuelle Seite: §a" + page + "§8/§a" + pages).build();
                inventory.setItem(40, currentPage);

                List<String> rolesList = new ArrayList<>(roles);
                if (rolesList.size() < 22) {
                    for (int i = 0; i < 22; i++) {
                        if (rolesList.size() > i) {
                            ItemStack roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("", "§cStandard §8» §7" + (NewPermManager.isRoleDefault(rolesList.get(i)) ? "§7Ja" : "§7Nein"), "", "§cPrefix §8» " + NewPermManager.getRolePrefix(rolesList.get(i)), "§cSuffix §8» " + NewPermManager.getRoleSuffix(rolesList.get(i)), "", "§7Diese §cRolle §7besitzt §c" + NewPermManager.getRolePermissions(rolesList.get(i)).size() + " Permissions§7.", "§7Diese §cRolle §7besitzt §c" + NewPermManager.getRoleInheritance(rolesList.get(i)).size() + " §cVererbungen§7.", "", "§7Linksklick, um die §cRolle §7zu verändern.", "§7Rechtsklick, um die §cRolle §7zu löschen.")).build();
                            inventory.addItem(roleItem);
                        }
                    }
                } else {
                    if (page == 1) {
                        for (int i = 0; i < 22; i++) {
                            if (rolesList.size() > i) {
                                ItemStack roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("", "§cStandard §8» §7" + (NewPermManager.isRoleDefault(rolesList.get(i)) ? "§7Ja" : "§7Nein"), "", "§cPrefix §8» " + NewPermManager.getRolePrefix(rolesList.get(i)), "§cSuffix §8» " + NewPermManager.getRoleSuffix(rolesList.get(i)), "", "§7Diese §cRolle §7besitzt §c" + NewPermManager.getRolePermissions(rolesList.get(i)).size() + " Permissions§7.", "§7Diese §cRolle §7besitzt §c" + NewPermManager.getRoleInheritance(rolesList.get(i)).size() + " §cVererbungen§7.", "", "§7Linksklick, um die §cRolle §7zu verändern.", "§7Rechtsklick, um die §cRolle §7zu löschen.")).build();
                                inventory.addItem(roleItem);
                            }
                        }
                    } else {
                        for (int i = ((page - 1) * 21); i < (((page - 1) * 21) + 22); i++) {
                            if (rolesList.size() > i) {
                                ItemStack roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("", "§cStandard §8» §7" + (NewPermManager.isRoleDefault(rolesList.get(i)) ? "§7Ja" : "§7Nein"), "", "§cPrefix §8» " + NewPermManager.getRolePrefix(rolesList.get(i)), "§cSuffix §8» " + NewPermManager.getRoleSuffix(rolesList.get(i)), "", "§7Diese §cRolle §7besitzt §c" + NewPermManager.getRolePermissions(rolesList.get(i)).size() + " Permissions§7.", "§7Diese §cRolle §7besitzt §c" + NewPermManager.getRoleInheritance(rolesList.get(i)).size() + " §cVererbungen§7.", "", "§7Linksklick, um die §cRolle §7zu verändern.", "§7Rechtsklick, um die §cRolle §7zu löschen.")).build();
                                inventory.addItem(roleItem);
                            }
                        }
                    }
                }

                p.openInventory(inventory);
                roleMenu.put(p, "");
                roleMenuPage.put(p, page);
            } else {
                p.sendMessage(NewPerm.prefix + " §cDie folgende Seite existiert nicht!");
            }
        } else {
            p.sendMessage(NewPerm.prefix + " §cDie folgende Seite existiert nicht!");
        }
    }

}
