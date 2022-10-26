package me.newdavis.listener;
//Plugin by NewDavis

import me.newdavis.manager.NewPermManager;
import me.newdavis.manager.player.PlayerMenu;
import me.newdavis.manager.player.PlayerPermissionMenu;
import me.newdavis.manager.player.PlayerRoleMenu;
import me.newdavis.manager.role.RoleInheritanceMenu;
import me.newdavis.manager.role.RoleMenu;
import me.newdavis.manager.role.RoleMenuSpecificRole;
import me.newdavis.manager.role.RolePermissionMenu;
import me.newdavis.other.EnterValue;
import me.newdavis.plugin.NewPerm;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;

public class OnInventoryClickListener implements Listener {

    private final String PERM_PLAYER = "newperm.player", PERM_ROLE = "newperm.role";

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getInventory().getType() == InventoryType.CHEST) {
            ItemStack clickedItem = e.getCurrentItem();
            if (e.getView().getTitle().contains(" ")) {
                if (PlayerMenu.playerMenu.containsKey(p)) {
                    if (p.getOpenInventory().getTitle().contains("§cMenü") || p.getOpenInventory().getTitle().contains("§cPerm")
                            || p.getOpenInventory().getTitle().contains("§cRole") || p.getOpenInventory().getTitle().contains("§cInheritance")) {
                        String[] invTitle = e.getView().getTitle().split(" ");
                        int clickedSlot = e.getSlot();
                        HashMap<Player, OfflinePlayer> playerMenu = PlayerMenu.playerMenu;
                        //Menü
                        if (invTitle[2].equalsIgnoreCase("§cMenü")) {
                            e.setCancelled(true);
                            if (NewPermManager.playerHasPermission(p, PERM_PLAYER)) {
                                if (clickedSlot == 11) {
                                    PlayerPermissionMenu.openPermissionMenu(p, playerMenu.get(p), 1);
                                } else if (clickedSlot == 15) {
                                    PlayerRoleMenu.openPlayerRoleMenu(p, playerMenu.get(p), 1);
                                } else if (clickedSlot == 29) {
                                    EnterValue.forWhat.put(p, "prefix");
                                    EnterValue.sendChatMessage(p);
                                    p.closeInventory();
                                } else if (clickedSlot == 33) {
                                    EnterValue.forWhat.put(p, "suffix");
                                    EnterValue.sendChatMessage(p);
                                    p.closeInventory();
                                }
                            } else {
                                EnterValue.removeAll(p);
                            }
                            //Perm
                        } else if (invTitle[2].equalsIgnoreCase("§cPerm")) {
                            if (NewPermManager.playerHasPermission(p, PERM_PLAYER)) {
                                String username = invTitle[0].replace("§7", "");
                                OfflinePlayer t = Bukkit.getOfflinePlayer(username);
                                e.setCancelled(true);
                                if (clickedSlot == 26) {
                                    EnterValue.forWhat.put(p, "perm");
                                    EnterValue.sendChatMessage(p);
                                    p.closeInventory();
                                } else if (clickedSlot == 36) {
                                    PlayerPermissionMenu.playerPermPage.remove(p);
                                    PlayerMenu.openPlayerMenu(p, t);
                                } else if (clickedSlot == 39) {
                                    int page = PlayerPermissionMenu.playerPermPage.get(p);
                                    PlayerPermissionMenu.openPermissionMenu(p, t, page - 1);
                                } else if (clickedSlot == 41) {
                                    int page = PlayerPermissionMenu.playerPermPage.get(p);
                                    PlayerPermissionMenu.openPermissionMenu(p, t, page + 1);
                                } else if (clickedItem.getType() == Material.PAPER) {
                                    String perm = clickedItem.getItemMeta().getDisplayName().replace("§c", "");
                                    if (NewPermManager.removePlayerPermission(t, perm)) {
                                        p.sendMessage(NewPerm.prefix + " §7Die Permission §c" + perm + " §7wurde §centfernt§7.");
                                        PlayerPermissionMenu.openPermissionMenu(p, t, PlayerPermissionMenu.playerPermPage.get(p));
                                    } else {
                                        p.sendMessage(NewPerm.prefix + " §c" + t.getName() + " §cbesitzt die Permission " + perm + " nicht!");
                                    }
                                }
                            } else {
                                EnterValue.removeAll(p);
                            }
                            //Role
                        } else if (invTitle[2].equalsIgnoreCase("§cRole")) {
                            if (NewPermManager.playerHasPermission(p, PERM_ROLE)) {
                                String username = invTitle[0].replace("§7", "");
                                OfflinePlayer t = Bukkit.getOfflinePlayer(username);
                                e.setCancelled(true);
                                if (clickedSlot == 26) {
                                    EnterValue.forWhat.put(p, "role");
                                    EnterValue.sendChatMessage(p);
                                    p.closeInventory();
                                } else if (clickedSlot == 36) {
                                    PlayerRoleMenu.playerRolePage.remove(p);
                                    PlayerMenu.openPlayerMenu(p, t);
                                } else if (clickedSlot == 39) {
                                    int page = PlayerRoleMenu.playerRolePage.get(p);
                                    PlayerRoleMenu.openPlayerRoleMenu(p, t, page - 1);
                                } else if (clickedSlot == 41) {
                                    int page = PlayerRoleMenu.playerRolePage.get(p);
                                    PlayerRoleMenu.openPlayerRoleMenu(p, t, page + 1);
                                } else if (clickedItem.getType() == Material.BOOK) {
                                    String role = clickedItem.getItemMeta().getDisplayName().replace("§c", "");
                                    if (!NewPermManager.getPlayerRole(t).equalsIgnoreCase(role)) {
                                        NewPermManager.setPlayerRole(t, role);
                                        p.sendMessage(NewPerm.prefix + " §7Die §cRolle §7von §c" + t.getName() + " §7wurde zu §c" + role + " §7geändert.");
                                        PlayerRoleMenu.openPlayerRoleMenu(p, t, PlayerRoleMenu.playerRolePage.get(p));
                                    } else {
                                        p.sendMessage(NewPerm.prefix + " §c" + t.getName() + " ist bereits in der Rolle!");
                                    }
                                }
                            } else {
                                EnterValue.removeAll(p);
                            }
                        }
                    } else {
                        PlayerMenu.playerMenu.remove(p);
                    }
                } else if (RoleMenu.roleMenu.containsKey(p)) {
                    if (NewPermManager.playerHasPermission(p, PERM_ROLE)) {
                        String[] invTitle = e.getView().getTitle().split(" ");
                        int clickedSlot = e.getSlot();
                        if (invTitle[2].equalsIgnoreCase("§cPerm")) {
                            String role = RoleMenu.roleMenu.get(p);
                            e.setCancelled(true);
                            if (clickedSlot == 26) {
                                EnterValue.forWhat.put(p, "rolePerm");
                                EnterValue.sendChatMessage(p);
                                p.closeInventory();
                            } else if (clickedSlot == 36) {
                                RolePermissionMenu.rolePermissionPage.remove(p);
                                RoleMenuSpecificRole.openPlayerMenu(p, role);
                            } else if (clickedSlot == 39) {
                                int page = RolePermissionMenu.rolePermissionPage.get(p);
                                RolePermissionMenu.openPermissionMenu(p, role, page - 1);
                            } else if (clickedSlot == 41) {
                                int page = RolePermissionMenu.rolePermissionPage.get(p);
                                RolePermissionMenu.openPermissionMenu(p, role, page + 1);
                            } else if (clickedItem.getType() == Material.PAPER) {
                                int page = RolePermissionMenu.rolePermissionPage.get(p);
                                String perm = clickedItem.getItemMeta().getDisplayName().replace("§c", "");
                                if (NewPermManager.removeRolePermission(role, perm)) {
                                    p.sendMessage(NewPerm.prefix + " §7Die Permission §c" + perm + " §7wurde §centfernt§7.");
                                    RolePermissionMenu.openPermissionMenu(p, role, page);
                                } else {
                                    p.sendMessage(NewPerm.prefix + " §7Die Rolle §c" + role + " §cbesitzt die Permission " + perm + " nicht!");
                                }
                            }
                        } else if (invTitle[2].equalsIgnoreCase("§cInheritance")) {
                            String role = RoleMenu.roleMenu.get(p);
                            e.setCancelled(true);
                            if (clickedSlot == 36) {
                                RoleInheritanceMenu.roleInheritancePage.remove(p);
                                RoleMenuSpecificRole.openPlayerMenu(p, role);
                            } else if (clickedSlot == 39) {
                                int page = RoleInheritanceMenu.roleInheritancePage.get(p);
                                RoleInheritanceMenu.openInheritanceMenu(p, role, page - 1);
                            } else if (clickedSlot == 41) {
                                int page = RoleInheritanceMenu.roleInheritancePage.get(p);
                                RoleInheritanceMenu.openInheritanceMenu(p, role, page + 1);
                            } else if (clickedItem.getType() == Material.BOOK) {
                                int page = RoleInheritanceMenu.roleInheritancePage.get(p);
                                Collection<String> inheritances = NewPermManager.getRoleInheritance(role);
                                String clickedRole = clickedItem.getItemMeta().getDisplayName().replace("§c", "");
                                if (inheritances.contains(clickedRole)) {
                                    NewPermManager.removeInheritance(role, clickedRole);
                                    p.sendMessage(NewPerm.prefix + " §7Die Inheritance §c" + clickedRole + " §7wurde von §c" + role + " §7entfernt.");
                                } else {
                                    NewPermManager.addInheritance(role, clickedRole);
                                    p.sendMessage(NewPerm.prefix + " §7Die Inheritance §c" + clickedRole + " §7wurde zu §c" + role + " §7hinzugefügt.");
                                }
                                RoleInheritanceMenu.openInheritanceMenu(p, role, page);
                            }
                        } else if (invTitle[2].equalsIgnoreCase("§cMenü")) {
                            e.setCancelled(true);
                            if (clickedSlot == 26) {
                                EnterValue.forWhat.put(p, "role");
                                EnterValue.sendChatMessage(p);
                                p.closeInventory();
                            } else if (clickedSlot == 39) {
                                int page = RoleMenu.roleMenuPage.get(p);
                                RoleMenu.openRoleMenu(p, page - 1);
                            } else if (clickedSlot == 41) {
                                int page = RoleMenu.roleMenuPage.get(p);
                                RoleMenu.openRoleMenu(p, page + 1);
                            } else if (clickedItem.getType() == Material.BOOK) {
                                String role = clickedItem.getItemMeta().getDisplayName().replace("§c", "");
                                if (e.getClick().isLeftClick()) {
                                    if (NewPermManager.getRoleList().contains(role)) {
                                        RoleMenuSpecificRole.openPlayerMenu(p, role);
                                    } else {
                                        p.sendMessage(NewPerm.error);
                                    }
                                } else if (e.getClick().isRightClick()) {
                                    if (NewPermManager.getRoleList().contains(role)) {
                                        NewPermManager.removeRole(role);
                                        p.sendMessage(NewPerm.prefix + " §cDie Rolle " + role + " wurde entfernt!");
                                        int page = RoleMenu.roleMenuPage.get(p);
                                        RoleMenu.openRoleMenu(p, page);
                                    } else {
                                        p.sendMessage(NewPerm.error);
                                    }
                                }
                            }
                        } else {
                            String role = invTitle[2].replace("§c", "");
                            if (NewPermManager.getRoleList().contains(role)) {
                                e.setCancelled(true);
                                if (clickedSlot == 36) {
                                    int page = RoleMenu.roleMenuPage.get(p);
                                    RoleMenu.openRoleMenu(p, page);
                                } else if (clickedSlot == 11) {
                                    RolePermissionMenu.openPermissionMenu(p, role, 1);
                                } else if (clickedSlot == 15) {
                                    RoleInheritanceMenu.openInheritanceMenu(p, role, 1);
                                } else if (clickedSlot == 29) {
                                    EnterValue.forWhat.put(p, "rolePrefix");
                                    EnterValue.sendChatMessage(p);
                                    p.closeInventory();
                                } else if (clickedSlot == 33) {
                                    EnterValue.forWhat.put(p, "roleSuffix");
                                    EnterValue.sendChatMessage(p);
                                    p.closeInventory();
                                }
                            } else {
                                p.sendMessage(NewPerm.error);
                            }
                        }
                    } else {
                        EnterValue.removeAll(p);
                    }
                }
            }
        }
    }
}