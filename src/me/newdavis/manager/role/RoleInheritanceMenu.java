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

public class RoleInheritanceMenu {

    public static HashMap<Player, Integer> roleInheritancePage = new HashMap<>();

    public static void openInheritanceMenu(Player p, String role, int page) {
        EnterValue.removePlayerLists(p);
        if(page != 0) {
            Collection<String> roles = NewPermManager.getRoleList();
            Collection<String> inheritances = NewPermManager.getRoleInheritance(role);
            int rolesSize = roles.size();
            double pagesDouble = (double) rolesSize / 21;
            String pagesString = String.valueOf(pagesDouble);
            if (pagesString.contains(".")) {
                pagesDouble = pagesDouble + 1;
            }

            int pages = (int) pagesDouble;

            if (pages >= page) {
                Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§7" + role + " §8● §cInheritance");

                List<Integer> glassSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 37, 38, 42, 43, 44);
                for (int slot : glassSlots) {
                    inventory.setItem(slot, ItemBuilder.glass);
                }

                ItemStack backToMenu = ItemBuilder.getCustomSkull(ItemBuilder.lightGrayArrowLeftLink, "§7Zurück zum §cMenü§7.", new ArrayList<>());
                inventory.setItem(36, backToMenu);
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
                            if(!role.equalsIgnoreCase(rolesList.get(i))) {
                                ItemStack roleItem;
                                if (inheritances.contains(rolesList.get(i))) {
                                    roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("§cDiese Rolle wurde bereits hinzugefügt!", "§7Klicke, um die Rolle von der §cInheritance §7von §c" + rolesList.get(i) + " §7zu entfernen.")).build();
                                } else {
                                    roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("", "§7Klicke, um diese Rolle zu der §cInheritance §7von §c" + role + " §7hinzuzufügen.")).build();
                                }
                                inventory.addItem(roleItem);
                            }
                        }
                    }
                } else {
                    if (page == 1) {
                        for (int i = 0; i < 22; i++) {
                            if (rolesList.size() > i) {
                                if(!role.equalsIgnoreCase(rolesList.get(i))) {
                                    ItemStack roleItem;
                                    if (inheritances.contains(rolesList.get(i))) {
                                        roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("§cDiese Rolle wurde bereits hinzugefügt!", "§7Klicke, um die Rolle von der §cInheritance §7von §c" + rolesList.get(i) + " §7zu entfernen.")).build();
                                    } else {
                                        roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("", "§7Klicke, um diese Rolle zu der §cInheritance §7von §c" + role + " §7hinzuzufügen.")).build();
                                    }
                                    inventory.addItem(roleItem);
                                }
                            }
                        }
                    } else {
                        for (int i = ((page - 1) * 21); i < (((page - 1) * 21) + 22); i++) {
                            if (rolesList.size() > i) {
                                if(!role.equalsIgnoreCase(rolesList.get(i))) {
                                    ItemStack roleItem;
                                    if (inheritances.contains(rolesList.get(i))) {
                                        roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("§cDiese Rolle wurde bereits hinzugefügt!", "§7Klicke, um die Rolle von der §cInheritance §7von §c" + rolesList.get(i) + " §7zu entfernen.")).build();
                                    } else {
                                        roleItem = new ItemBuilder(Material.BOOK).setName("§c" + rolesList.get(i)).setLore(Arrays.asList("", "§7Klicke, um diese Rolle zu der §cInheritance §7von §c" + role + " §7hinzuzufügen.")).build();
                                    }
                                    inventory.addItem(roleItem);
                                }
                            }
                        }
                    }
                }

                p.openInventory(inventory);
                roleInheritancePage.put(p, page);
            } else {
                p.sendMessage(NewPerm.prefix + " §cDie folgende Seite existiert nicht!");
            }
        } else {
            p.sendMessage(NewPerm.prefix + " §cDie folgende Seite existiert nicht!");
        }
    }

}
